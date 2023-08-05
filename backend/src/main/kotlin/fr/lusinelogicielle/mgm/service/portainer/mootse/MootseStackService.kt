package fr.lusinelogicielle.mgm.service.portainer.mootse

import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack

interface MootseStackService {

    fun getStackById(stackId: Int): MootseStack
    fun getAllStacks(): List<MootseStack>
    fun createStack(mootseStack: MootseStack): Stack
    fun updateStack(stackId: Int, mootseStack: MootseStack): Stack

}