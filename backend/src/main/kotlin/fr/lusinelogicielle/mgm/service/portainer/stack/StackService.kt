package fr.lusinelogicielle.mgm.service.portainer.stack

import fr.lusinelogicielle.mgm.model.portainer.Stack

interface StackService {

    fun getStackById(stackId: Int): Stack
    fun getAllStacks(): List<Stack>
    fun deleteStack(stackId: Int): Unit
    fun stopStack(stackId: Int): Unit
    fun startStack(stackId: Int): Unit
}
