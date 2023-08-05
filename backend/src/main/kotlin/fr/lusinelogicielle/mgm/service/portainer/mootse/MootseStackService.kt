package fr.lusinelogicielle.mgm.service.portainer.mootse

import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack

interface MootseStackService {

    fun createStack(mootse: MootseStack): Stack

}