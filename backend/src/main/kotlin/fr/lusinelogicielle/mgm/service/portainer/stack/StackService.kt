package fr.lusinelogicielle.mgm.service.portainer.stack

import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack

interface StackService {

    fun getStackById(stackId: Int): Stack
    fun getAllStacks(): List<Stack>
    fun deleteStack(stackId: Int): Unit

}