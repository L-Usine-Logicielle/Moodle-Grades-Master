package fr.lusinelogicielle.mgm.clients

import fr.lusinelogicielle.mgm.exceptions.portainer.PortainerApiException
import fr.lusinelogicielle.mgm.exceptions.portainer.PortainerAuthenticationException
import fr.lusinelogicielle.mgm.model.portainer.Stack
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private val logger: Logger = LoggerFactory.getLogger(PortainerApiClientImpl::class.java)

@Component
class PortainerApiClientImpl : PortainerApiClient {

    private val httpClient = createUnsecureHttpClient()

    @Value("\${portainer.api.url}")
    private lateinit var portainerApiUrl: String

    @Value("\${portainer.api.username}")
    private lateinit var portainerApiUsername: String

    @Value("\${portainer.api.password}")
    private lateinit var portainerApiPassword: String

    override fun makeRequest(
            method: HttpMethod,
            path: String,
            requestBody: String?
    ): String {
        val jwt = login(portainerApiUsername, portainerApiPassword)
        val requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("$portainerApiUrl$path"))
                .header("Authorization", "Bearer $jwt")
                .method(method.name(), HttpRequest.BodyPublishers.ofString(requestBody ?: ""))
                .header("Content-Type", "application/json")

        val request = requestBuilder.build()
        val response = try {
            logger.info("${request.method()} : ${request.uri()}")
            httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (e: IOException) {
            throw PortainerApiException("Unable to make API request to Portainer: $e")
        }

        if (response.statusCode() in 200..299) {
            return response.body()
        } else {
            throw PortainerApiException("API request failed with status code: ${response.statusCode()}")
        }
    }

    private fun login(username: String, password: String): String {
        val loginData = """
            {
                "Username": "$username",
                "Password": "$password"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
                .uri(URI.create("$portainerApiUrl/api/auth"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginData))
                .build()

        val response = try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (e: IOException) {
            throw PortainerApiException("Unable to make API request to Portainer : $e")
        }

        if (response.statusCode() in 200..299) {
            val jsonResponse = JSONObject(response.body())
            return jsonResponse.getString("jwt")
        } else {
            throw PortainerAuthenticationException("Unable to authenticate to Portainer API")
        }

    }

    private fun createUnsecureHttpClient(): HttpClient {
        val trustAllCertificates: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
                }
        )

        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        return HttpClient.newBuilder()
                .sslContext(sslContext)
                .build()
    }

}

