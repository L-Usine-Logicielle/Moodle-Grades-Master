package fr.lusinelogicielle.mgm.service.portainer.mootse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.lusinelogicielle.mgm.clients.PortainerApiClient
import fr.lusinelogicielle.mgm.clients.PortainerApiClientImpl
import fr.lusinelogicielle.mgm.model.mootse.MootseDatabase
import fr.lusinelogicielle.mgm.model.mootse.MootseRunner
import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack
import fr.lusinelogicielle.mgm.utils.forgeDockerComposeFile
import fr.lusinelogicielle.mgm.utils.splitBeforeAndAfterCharacter
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml

private val logger: Logger = LoggerFactory.getLogger(PortainerApiClientImpl::class.java)

@Service
class MootseStackServiceImpl(private val portainerApiClient: PortainerApiClient): MootseStackService {
    @Value("\${docker.mail.mailServer}")
    private lateinit var mailServer: String

    @Value("\${docker.mail.mailPort}")
    private lateinit var mailPort: String

    @Value("\${docker.mail.mailUsername}")
    private lateinit var mailUsername: String

    @Value("\${docker.mail.mailPassword}")
    private lateinit var mailPassword: String

    override fun getStackById(stackId: Int): MootseStack {
        val response = portainerApiClient.makeRequest(
                HttpMethod.GET,
                "/api/stacks/$stackId/file",
                null
        )

        val jsonResponse = JSONObject(response)
        val content = jsonResponse.getString("StackFileContent")

        val stackNameResponse = portainerApiClient.makeRequest(
                HttpMethod.GET,
                "/api/stacks/$stackId",
                null
        )
        val stackNameJsonResponse = JSONObject(stackNameResponse)
        val stackName = stackNameJsonResponse.getString("Name")

        val yaml = Yaml()
        val dockerComposeYaml: Map<String, Object> = yaml.load(content)

        val services = dockerComposeYaml.get("services") as Map<String, Object>

        val database = services.get("mootse-mariadb") as Map<String, Object>
        val databaseName = database.get("container_name") as String
        val databaseImage = database.get("image") as String
        var mootseDatabase = MootseDatabase(
                name = databaseName,
                containerImage = databaseImage
        )
        val runner = services.get("mootse-runner") as Map<String, Object>
        val runnerName = runner.get("container_name") as String
        val runnerImage = runner.get("image") as String
        var mootseRunner = MootseRunner(
                name = runnerName,
                containerImage = runnerImage,
        )

        val runnerEnv = runner["environment"] as List<String>
        for (env in runnerEnv) {
            val (variable, value) = splitBeforeAndAfterCharacter(env, '=')
            when (variable) {
                "SCAN_INTERVAL" -> mootseRunner.scanInterval = value.toInt()
                "MOOTSE_URL" -> mootseRunner.url = value
                "MOOTSE_USERNAME" -> mootseRunner.username = value
                "MOOTSE_PASSWORD" -> mootseRunner.password = value
                "MAIL_RECIPIENTS" -> mootseRunner.recipients = value
                "DISCORD_WEBHOOK_URL" -> mootseRunner.discordWebhookUrl = value
                "DB_PASSWORD" -> mootseDatabase.password = value
            }
        }

        val network = dockerComposeYaml.get("networks") as Map<String, Object>
        val networkName = network.keys.first()

        return MootseStack(
                name = stackName,
                network = networkName,
                runner = mootseRunner,
                database = mootseDatabase
        )

    }

    override fun getAllStacks(): List<MootseStack> {
        val response = portainerApiClient.makeRequest(
                HttpMethod.GET,
                "/api/stacks",
                null
        )
        val mapper = jacksonObjectMapper()

        val allStacks = mapper.readValue<List<Stack>>(response)
        val filteredStacks = allStacks.filter { it.name.startsWith("mootse") }

        val mootseStacks = mutableListOf<MootseStack>()

        for(stack in filteredStacks){
            mootseStacks.add(getStackById(stack.id))
        }

        return mootseStacks.toList()
    }

    override fun createStack(mootseStack: MootseStack): Stack {
        val dockerCompose = forgeDockerComposeFile(mootseStack, mailServer, mailPort, mailUsername, mailPassword)
        val jsonData = """
        {
            "fromAppTemplate": false,
            "name": "${mootseStack.name}",
            "stackFileContent": "${dockerCompose.trimMargin().replace("\n", "\\n")}"
        }
        """.trimIndent()

        val response = portainerApiClient.makeRequest(
                HttpMethod.POST,
                "/api/stacks?type=2&method=string&endpointId=2",
                jsonData
        )

        val mapper = jacksonObjectMapper()

        return mapper.readValue<Stack>(response)
    }

    override fun updateStack(stackId: Int, mootseStack: MootseStack): Stack {
        val dockerCompose = forgeDockerComposeFile(mootseStack, mailServer, mailPort, mailUsername, mailPassword)
        val jsonData = """
        {
            "prune": false,
            "pullImage": true,
            "stackFileContent": "${dockerCompose.trimMargin().replace("\n", "\\n")}"
        }
        """.trimIndent()

        val response = portainerApiClient.makeRequest(
                HttpMethod.PUT,
                "/api/stacks/$stackId?endpointId=2",
                jsonData
        )

        val mapper = jacksonObjectMapper()

        return mapper.readValue<Stack>(response)
    }

}