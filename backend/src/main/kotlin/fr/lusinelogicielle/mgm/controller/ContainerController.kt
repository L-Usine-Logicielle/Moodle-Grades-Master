package fr.lusinelogicielle.mgm.controller

import fr.lusinelogicielle.mgm.model.docker.Container
import fr.lusinelogicielle.mgm.service.portainer.docker.ContainerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/containers")
class ContainerController(private val containerService: ContainerService) {

    @GetMapping
    fun getContainers(): List<Container> {
        return containerService.getAllContainers()
    }
}
