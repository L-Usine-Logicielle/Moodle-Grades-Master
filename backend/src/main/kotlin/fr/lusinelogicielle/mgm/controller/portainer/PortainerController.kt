package fr.lusinelogicielle.mgm.controller.portainer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*
import fr.lusinelogicielle.mgm.model.docker.DockerContainer
import fr.lusinelogicielle.mgm.model.docker.FileContentResponse
import fr.lusinelogicielle.mgm.model.docker.Stack
import fr.lusinelogicielle.mgm.service.ApiService
import fr.lusinelogicielle.mgm.service.AuthService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yaml.snakeyaml.Yaml

private val logger: Logger = LoggerFactory.getLogger(PortainerController::class.java)

@RestController
@RequestMapping("/portainer")
class PortainerController {

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var apiService: ApiService

    @Value("\${portainer.api.url}")
    private lateinit var portainerApiUrl: String

    @Value("\${portainer.api.username}")
    private lateinit var portainerApiUsername: String

    @Value("\${portainer.api.password}")
    private lateinit var portainerApiPassword: String

    @Value("\${docker.mail.mailServer}")
    private lateinit var mailServer: String

    @Value("\${docker.mail.mailPort}")
    private lateinit var mailPort: String

    @Value("\${docker.mail.mailUsername}")
    private lateinit var mailUsername: String

    @Value("\${docker.mail.mailPassword}")
    private lateinit var mailPassword: String

    @Value("\${docker.mootse.url}")
    private lateinit var mootseUrl: String

    @GetMapping("/stacks")
    fun getStacks(): ResponseEntity<List<Stack>> {
        val portainerApiToken = authService.generateAuthToken(portainerApiUsername, portainerApiPassword)
        val endpointUrl = "$portainerApiUrl/api/stacks"
        val response = apiService.makeApiRequest(endpointUrl, portainerApiToken)
        val mapper = jacksonObjectMapper()

        val stacks: List<Stack> = mapper.readValue(response.body!!)

        val jsonMapper = ObjectMapper()

        for(stack in stacks){
            val endpointStackUrl = "$portainerApiUrl/api/stacks/${stack.id}/file"
            val jsonResult = apiService.makeApiRequest(endpointStackUrl, portainerApiToken).body!!
            val fileContentResponse = jsonMapper.readValue(jsonResult, FileContentResponse::class.java)
            stack.fileContent = fileContentResponse
        }
        for(stack in stacks){
            if(stack.id == 10) {
                val yaml = Yaml()
                val data: Map<*, *> = yaml.load(stack.fileContent?.stackFileContent ?: "")

                val services = data["services"] as Map<*, *>

                val mariadbRegex = Regex("^mootse-mariadb(?:-[a-zA-Z0-9]+)*\$")
                val runnerRegex = Regex("^mootse-runner(?:-[a-zA-Z0-9]+)*\$")

                val mariadbService = services.entries.firstOrNull { it.key.toString().matches(mariadbRegex) }?.value as Map<*, *>

                val networkName = (mariadbService["networks"] as List<*>).first { it.toString().startsWith("mootse-network") } as String

                val mariaDbName = services.keys.filter { it.toString().matches(mariadbRegex) }.first()
                val mariaDbContainerName = mariadbService["container_name"] as String
                val mariaDbImage = mariadbService["image"] as String
                val mariaDbRestartPolicy = mariadbService["restart"] as String
                val mariaDbRootPassword = (mariadbService["environment"] as List<*>).first { it.toString().startsWith("MYSQL_ROOT_PASSWORD") } as String

                val runnerService = services.entries.firstOrNull { it.key.toString().matches(runnerRegex) }?.value as Map<*, *>

                val runnerName = services.keys.filter { it.toString().matches(runnerRegex) }.first()
                val runnerContainerName = runnerService["container_name"] as String
                val runnerImage = runnerService["image"] as String
                val runnerRestartPolicy = runnerService["restart"] as String
                val runnerScanInterval = (runnerService["environment"] as List<*>).first { it.toString().startsWith("SCAN_INTERVAL") } as String
                val runnerMootseUsernamme = (runnerService["environment"] as List<*>).first { it.toString().startsWith("MOOTSE_USERNAME") } as String
                val runnerMootsePassword = (runnerService["environment"] as List<*>).first { it.toString().startsWith("MOOTSE_PASSWORD") } as String
                val runnerMailRecipients = (runnerService["environment"] as List<*>).first { it.toString().startsWith("MAIL_RECIPIENTS") } as String
                val runnerDiscordWebhookUrl = (runnerService["environment"] as List<*>).first { it.toString().startsWith("DISCORD_WEBHOOK_URL") } as String
                val runnerDbHost = (runnerService["environment"] as List<*>).first { it.toString().startsWith("DB_HOST") } as String
                val runnerDbUser = (runnerService["environment"] as List<*>).first { it.toString().startsWith("DB_USER") } as String
                val runnerDbPassword = (runnerService["environment"] as List<*>).first { it.toString().startsWith("DB_PASSWORD") } as String
                val runnerDbPort = (runnerService["environment"] as List<*>).first { it.toString().startsWith("DB_PORT") } as String
                val runnerPromo = (runnerService["environment"] as List<*>).first { it.toString().startsWith("PROMO") } as String
            }
        }
        return ResponseEntity(stacks, HttpStatus.OK)
    }

    @GetMapping("/containers/{endpointId}")
    fun getContainers(@PathVariable endpointId: Int): ResponseEntity<List<DockerContainer>> {
        val portainerApiToken = authService.generateAuthToken(portainerApiUsername, portainerApiPassword)
        val endpointUrl = "$portainerApiUrl/api/endpoints/$endpointId/docker/containers/json"
        val response = apiService.makeApiRequest(endpointUrl, portainerApiToken)
        val mapper = jacksonObjectMapper()

        val containers: List<DockerContainer> = mapper.readValue(response.body!!)

        return ResponseEntity(containers, HttpStatus.OK)
    }

}