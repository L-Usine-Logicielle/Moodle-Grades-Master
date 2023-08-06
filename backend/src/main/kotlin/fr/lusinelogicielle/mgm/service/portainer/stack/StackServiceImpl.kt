package fr.lusinelogicielle.mgm.service.portainer.stack

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.lusinelogicielle.mgm.clients.PortainerApiClient
import fr.lusinelogicielle.mgm.model.portainer.Stack
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

private val logger: Logger = LoggerFactory.getLogger(StackServiceImpl::class.java)

@Service
class StackServiceImpl(private val portainerApiClient: PortainerApiClient) : StackService {
    override fun getStackById(stackId: Int): Stack {
        val response = portainerApiClient.makeRequest(
            HttpMethod.GET,
            "/api/stacks/$stackId",
            null,
        )
        val mapper = jacksonObjectMapper()

        return mapper.readValue<Stack>(response)
    }

    override fun getAllStacks(): List<Stack> {
        val response = portainerApiClient.makeRequest(
            HttpMethod.GET,
            "/api/stacks",
            null,
        )
        val mapper = jacksonObjectMapper()

        return mapper.readValue<List<Stack>>(response)
    }

    override fun deleteStack(stackId: Int) {
        portainerApiClient.makeRequest(
            HttpMethod.DELETE,
            "/api/stacks/$stackId?endpointId=2",
            null,
        )
    }
}
