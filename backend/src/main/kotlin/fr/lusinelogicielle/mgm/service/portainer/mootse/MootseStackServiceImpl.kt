package fr.lusinelogicielle.mgm.service.portainer.mootse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.lusinelogicielle.mgm.clients.PortainerApiClient
import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack
import fr.lusinelogicielle.mgm.utils.forgeDockerComposeFile
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

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

    override fun createStack(mootse: MootseStack): Stack {
        val dockerCompose = forgeDockerComposeFile(mootse, mailServer, mailPort, mailUsername, mailPassword)
        val jsonData = """
        {
            "fromAppTemplate": false,
            "name": "${mootse.name}",
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

}