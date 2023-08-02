package fr.lusinelogicielle.mgm.service

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
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

private val logger: Logger = LoggerFactory.getLogger(ApiService::class.java)

@Service
class ApiService {

    fun makeApiRequest(endpointUrl: String, token: String): ResponseEntity<String> {
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

        logger.info("Requested URL: $endpointUrl")

        return restTemplate.exchange(endpointUrl, HttpMethod.GET, entity, String::class.java)
    }
}