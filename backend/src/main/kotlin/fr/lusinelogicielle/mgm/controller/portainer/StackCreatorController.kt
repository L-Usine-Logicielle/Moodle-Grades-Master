package fr.lusinelogicielle.mgm.controller.portainer

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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

private val logger: Logger = LoggerFactory.getLogger(StackCreatorController::class.java)
@RestController
@RequestMapping("/stack")
class StackCreatorController {

    @Autowired
    private lateinit var authService: AuthService

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
    @PostMapping("/create")
    fun createMootseRunnerStack(
            @RequestParam stackName: String,
            @RequestParam runnerName: String,
            @RequestParam dbName: String,
            @RequestParam networkName: String,
            @RequestParam mootseUsername: String,
            @RequestParam mootsePassword: String,
            @RequestParam(required = false, defaultValue = "docker.io/library/mariadb:10.4") dbImage: String,
            @RequestParam(required = false, defaultValue = "always") dbRestartPolicy: String,
            @RequestParam(required = false, defaultValue = "ghcr.io/lucasperfeito/moodle-grades-scraper:0.0.1") runnerImage: String,
            @RequestParam(required = false, defaultValue = "always") runnerRestartPolicy: String,
            @RequestParam(required = false, defaultValue = "30") scanInterval: String,
            @RequestParam(required = false, defaultValue = "") mailRecipients: String,
            @RequestParam(required = false, defaultValue = "") discordWebhookUrl: String,

            ): ResponseEntity<String> {
        val result = "Received stackName: $stackName, runnerName: $runnerName, dbName: $dbName, networkName: $networkName"

        val yamlContent = """
        version: '2'
        services:
          $dbName:
            container_name: $dbName
            image: $dbImage
            restart: $dbRestartPolicy
            environment:
              - MYSQL_ROOT_PASSWORD=randomPassword
            networks:
              - $networkName
          $runnerName:
            container_name: $runnerName
            image: $runnerImage
            depends_on:
              - $dbName
            restart: $runnerRestartPolicy
            networks:
              - $networkName
            environment:
              - SCAN_INTERVAL=$scanInterval
              - MOOTSE_URL=$mootseUrl
              - MOOTSE_USERNAME=$mootseUsername
              - MOOTSE_PASSWORD=$mootsePassword
              - MAIL_USERNAME=$mailUsername
              - MAIL_PASSWORD=$mailPassword
              - MAIL_SERVER=$mailServer
              - MAIL_PORT=$mailPort
              - MAIL_RECIPIENTS=$mailRecipients
              - DISCORD_WEBHOOK_URL=$discordWebhookUrl
              - DB_HOST=$dbName
              - DB_USER=root
              - DB_PASSWORD=randomPassword
              - DB_PORT=3306
              - PROMO=dbPromo
        networks:
          $networkName:
            """.trimIndent()
        val onelineYamlContent = yamlContent.trimMargin().replace("\n", "\\n")
        createStack(stackName, onelineYamlContent)
        return ResponseEntity.ok(result)
    }

    private fun createStack(stackName: String, stackFileContent: String) {
        val token = authService.generateAuthToken(portainerApiUsername, portainerApiPassword)

        val endpointUrl = "$portainerApiUrl/api/stacks?type=2&method=string&endpointId=2"
        val jsonData = """
        {
            "fromAppTemplate": false,
            "name": "$stackName",
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

        val response = restTemplate.exchange(endpointUrl, HttpMethod.POST, entity, String::class.java)
        val responseBody = response.body

        logger.info("Response: $responseBody")
    }
}