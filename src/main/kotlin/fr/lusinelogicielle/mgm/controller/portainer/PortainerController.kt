package fr.lusinelogicielle.mgm.controller.portainer

import org.apache.hc.client5.http.classic.HttpClient
import com.fasterxml.jackson.module.kotlin.*
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.socket.ConnectionSocketFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.lusinelogicielle.mgm.model.docker.DockerContainer
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.http.URIScheme
import org.apache.hc.core5.http.config.Registry
import org.apache.hc.core5.http.config.RegistryBuilder
import org.json.JSONObject
import org.apache.hc.core5.ssl.SSLContexts
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

private val logger: Logger = LoggerFactory.getLogger(PortainerController::class.java)

@RestController
@RequestMapping("/portainer")
class PortainerController {

    @Value("\${portainer.api.url}")
    private lateinit var portainerApiUrl: String

    @Value("\${portainer.api.username}")
    private lateinit var portainerApiUsername: String

    @Value("\${portainer.api.password}")
    private lateinit var portainerApiPassword: String

    @GetMapping("/stacks")
    fun getStacks(): ResponseEntity<String> {
        val portainerApiToken = generateAuthToken(portainerApiUsername, portainerApiPassword)
        val endpointUrl = "$portainerApiUrl/api/stacks"
        return makeApiRequest(endpointUrl, portainerApiToken)
    }

    @GetMapping("/containers/{endpointId}")
    fun getContainers(@PathVariable endpointId: Int): ResponseEntity<List<DockerContainer>> {
        val portainerApiToken = generateAuthToken(portainerApiUsername, portainerApiPassword)
        val endpointUrl = "$portainerApiUrl/api/endpoints/$endpointId/docker/containers/json"
        val response = makeApiRequest(endpointUrl, portainerApiToken ?: "")
        val mapper = jacksonObjectMapper()

        val containers: List<DockerContainer> = mapper.readValue(response.body!!)

        return ResponseEntity(containers, HttpStatus.OK)
    }

    private fun generateAuthToken(username: String, password: String): String {
        val authEndpoint = "$portainerApiUrl/api/auth"

        val jsonPayload = JSONObject()
        jsonPayload.put("Username", username)
        jsonPayload.put("Password", password)

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

        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json")
        val entity = HttpEntity(jsonPayload.toString(), headers)

        val response = restTemplate.exchange(authEndpoint, HttpMethod.POST, entity, String::class.java)
        val responseBody = response.body

        return responseBody?.let {
            val jsonResponse = JSONObject(it)
            jsonResponse.optString("jwt", null)
        } ?: throw IllegalStateException("Authentication token is null.")
    }

    private fun makeApiRequest(endpointUrl: String, token: String): ResponseEntity<String> {
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

        val entity = HttpEntity<String>(headers)

        logger.info("Request URL: $endpointUrl")

        return restTemplate.exchange(endpointUrl, HttpMethod.GET, entity, String::class.java)
    }

}