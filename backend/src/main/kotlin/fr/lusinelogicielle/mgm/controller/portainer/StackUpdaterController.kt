package fr.lusinelogicielle.mgm.controller.portainer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.lusinelogicielle.mgm.model.docker.Stack
import fr.lusinelogicielle.mgm.service.ApiService
import fr.lusinelogicielle.mgm.service.AuthService
import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.socket.ConnectionSocketFactory
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.http.URIScheme
import org.apache.hc.core5.http.config.Registry
import org.apache.hc.core5.http.config.RegistryBuilder
import org.apache.hc.core5.ssl.SSLContexts
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

private val logger: Logger = LoggerFactory.getLogger(StackCreatorController::class.java)

@RestController
@RequestMapping("/stackupdate")
class StackUpdaterController {

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

    @PostMapping("/update")
    fun updateMootseRunnerStack(
        @RequestParam stackName: String,
        @RequestParam networkName: String,
        @RequestParam mariaDbName: String,
        @RequestParam mariaDbContainerName: String,
        @RequestParam mariaDbImage: String,
        @RequestParam mariaDbRestartPolicy: String,
        @RequestParam mariaDbRootPassword: String,
        @RequestParam runnerName: String,
        @RequestParam runnerContainerName: String,
        @RequestParam runnerImage: String,
        @RequestParam runnerRestartPolicy: String,
        @RequestParam runnerScanInterval: String,
        @RequestParam runnerMootseUsername: String,
        @RequestParam runnerMootsePassword: String,
        @RequestParam runnerMailRecipients: String,
        @RequestParam runnerDiscordWebhookUrl: String,
        @RequestParam runnerDbHost: String,
        @RequestParam runnerDbUser: String,
        @RequestParam runnerDbPassword: String,
        @RequestParam runnerDbPort: String,
        @RequestParam runnerPromo: String

            ): ResponseEntity<String> {

        val yamlContent = """
        version: '2'
        services:
          $mariaDbName:
            container_name: $mariaDbContainerName
            image: $mariaDbImage
            restart: $mariaDbRestartPolicy
            environment:
              - $mariaDbRootPassword
            networks:
              - $networkName
          $runnerName:
            container_name: $runnerContainerName
            image: $runnerImage
            depends_on:
              - $mariaDbName
            restart: $runnerRestartPolicy
            networks:
              - $networkName
            environment:
              - $runnerScanInterval
              - $runnerMootseUsername
              - $runnerMootsePassword
              - MOOTSE_URL=$mootseUrl
              - MAIL_USERNAME=$mailUsername
              - MAIL_PASSWORD=$mailPassword
              - MAIL_SERVER=$mailServer
              - MAIL_PORT=$mailPort
              - $runnerMailRecipients
              - $runnerDiscordWebhookUrl
              - $runnerDbHost
              - $runnerDbPort
              - $runnerDbPassword
              - $runnerDbUser
              - $runnerPromo
        networks:
          $networkName:
            """.trimIndent()
        logger.info(yamlContent)
        val onelineYamlContent = yamlContent.trimMargin().replace("\n", "\\n")
        val stackId = getStackId(stackName)
        updateStack(stackId, onelineYamlContent)
        return ResponseEntity.ok("OK")
    }

    private fun getStackId(stackName: String): Int{
        val portainerApiToken = authService.generateAuthToken(portainerApiUsername, portainerApiPassword)
        val endpointUrl = "$portainerApiUrl/api/stacks"
        val response = apiService.makeApiRequest(endpointUrl, portainerApiToken)
        val mapper = jacksonObjectMapper()

        val stacks: List<Stack> = mapper.readValue(response.body!!)

        var stack = stacks.filter { it.name == stackName }.first()

        return stack.id
    }
    private fun updateStack(stackId: Int, stackFileContent: String) {
        val token = authService.generateAuthToken(portainerApiUsername, portainerApiPassword)

        val endpointUrl = "$portainerApiUrl/api/stacks/$stackId?endpointId=2"
        val jsonData = """
        {
            "fromAppTemplate": false,
            "prune": true,
            "pullImage": true,
            "stackFileContent": "$stackFileContent"
        }
        """.trimIndent()
        logger.info(jsonData)

        val sslContext: javax.net.ssl.SSLContext? = SSLContexts.custom()
                .loadTrustMaterial { _, _ -> true }
                .build()

        val socketRegistry: Registry<ConnectionSocketFactory> = RegistryBuilder.create<ConnectionSocketFactory>()
                .register(URIScheme.HTTPS.id, SSLConnectionSocketFactory(sslContext))
                .register(URIScheme.HTTP.id, PlainConnectionSocketFactory.INSTANCE)
                .build()

        val httpClient: HttpClient = HttpClientBuilder.create()
                .setConnectionManager(PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true)
                .build()

        val restTemplate = RestTemplate()
        restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)

        val authorizationHeader = "Bearer $token"
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader)
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json")

        val entity = HttpEntity(jsonData, headers)

        val response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, entity, String::class.java)
        val responseBody = response.body

        logger.info("Response: $responseBody")
    }
}