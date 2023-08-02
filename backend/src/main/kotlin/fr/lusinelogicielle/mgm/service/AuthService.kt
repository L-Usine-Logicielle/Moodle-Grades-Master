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
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AuthService {

    @Value("\${portainer.api.url}")
    private lateinit var portainerApiUrl: String
    fun generateAuthToken(username: String, password: String): String {
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
}