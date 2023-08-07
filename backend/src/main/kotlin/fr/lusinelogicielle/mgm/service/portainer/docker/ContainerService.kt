package fr.lusinelogicielle.mgm.service.portainer.docker

import fr.lusinelogicielle.mgm.model.docker.Container

interface ContainerService {

    fun getAllContainers(): List<Container>
}
