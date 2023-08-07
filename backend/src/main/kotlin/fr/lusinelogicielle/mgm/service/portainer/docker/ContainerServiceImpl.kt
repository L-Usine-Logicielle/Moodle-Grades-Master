package fr.lusinelogicielle.mgm.service.portainer.docker

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.lusinelogicielle.mgm.clients.PortainerApiClient
import fr.lusinelogicielle.mgm.model.docker.Container
import fr.lusinelogicielle.mgm.service.portainer.stack.StackServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

private val logger: Logger = LoggerFactory.getLogger(StackServiceImpl::class.java)

@Service
class ContainerServiceImpl(private val portainerApiClient: PortainerApiClient) : ContainerService {

    override fun getAllContainers(): List<Container> {
        val response = portainerApiClient.makeRequest(
            HttpMethod.GET,
            "/api/endpoints/2/docker/containers/json",
            null,
        )
        val mapper = jacksonObjectMapper()

        return mapper.readValue<List<Container>>(response)
    }
}
