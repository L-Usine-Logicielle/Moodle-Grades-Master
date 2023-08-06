package fr.lusinelogicielle.mgm.clients

import org.springframework.http.HttpMethod

interface PortainerApiClient {
    fun makeRequest(
        method: HttpMethod,
        path: String,
        requestBody: String? = null,
    ): String
}
