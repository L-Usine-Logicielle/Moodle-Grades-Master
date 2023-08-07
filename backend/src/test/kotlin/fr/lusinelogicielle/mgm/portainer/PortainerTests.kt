package fr.lusinelogicielle.mgm.portainer

import fr.lusinelogicielle.mgm.clients.PortainerApiClientImpl
import fr.lusinelogicielle.mgm.model.portainer.Stack
import fr.lusinelogicielle.mgm.service.portainer.stack.StackServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`mock`
import org.mockito.Mockito.`verify`
import org.mockito.Mockito.`when`
import org.springframework.http.HttpMethod

class PortainerTests {
    private lateinit var portainerApiClient: PortainerApiClientImpl
    private lateinit var stackService: StackServiceImpl

    @BeforeEach
    fun setUp() {
        portainerApiClient = mock(PortainerApiClientImpl::class.java)
        stackService = StackServiceImpl(portainerApiClient)
    }

    @Test
    fun `test getStackById`() {
        // Arrange
        val stackId = 53
        val stack = Stack(stackId, "mootse-test-1", 2, 2)
        val responseJson = "{\"Id\":53,\"Name\":\"mootse-test-1\",\"Type\":2,\"EndpointId\":2}"
        `when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks/$stackId", null))
            .thenReturn(responseJson)

        // Act
        val result = stackService.getStackById(stackId)

        // Assert
        assertEquals(stack, result)
    }

    @Test
    fun `test getAllStacks`() {
        // Arrange
        val stacks = listOf(
            Stack(4, "stack-test", 2, 2),
            Stack(58, "mootse-test-stack-2", 1, 2),
            Stack(62, "mootse-test-3", 2, 2),
        )
        val responseJson = """[
            |{"Id":4,"Name":"stack-test","Type":2,"EndpointId":2},
            |{"Id":58,"Name":"mootse-test-stack-2","Type":1,"EndpointId":2},
            |{"Id":62,"Name":"mootse-test-3","Type":2,"EndpointId":2}]
        """.trimMargin()

        `when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks", null))
            .thenReturn(responseJson)

        // Act
        val result = stackService.getAllStacks()

        // Assert
        assertEquals(stacks, result)
    }

    @Test
    fun `test deleteStack`() {
        // Arrange
        val stackId = 1

        // Act
        stackService.deleteStack(stackId)

        // Assert
        val expectedUrl = "/api/stacks/$stackId?endpointId=2"
        `when`(portainerApiClient.makeRequest(HttpMethod.DELETE, expectedUrl, null))
            .thenReturn("")

        // Verify that the method was called with the correct parameters
        `verify`(portainerApiClient).makeRequest(HttpMethod.DELETE, expectedUrl, null)
    }
}
