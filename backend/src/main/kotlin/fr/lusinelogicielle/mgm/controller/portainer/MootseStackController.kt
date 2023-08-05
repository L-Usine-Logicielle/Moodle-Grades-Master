package fr.lusinelogicielle.mgm.controller.portainer

import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack
import fr.lusinelogicielle.mgm.service.portainer.mootse.MootseStackServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mootse")
class MootseStackController(private val mootseStackService: MootseStackServiceImpl) {

    @PostMapping()
    fun createStack(@RequestBody mootseStack: MootseStack): ResponseEntity<Stack> {
        val response = mootseStackService.createStack(mootseStack)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

}