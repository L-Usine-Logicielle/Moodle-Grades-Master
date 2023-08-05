package fr.lusinelogicielle.mgm.controller

import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack
import fr.lusinelogicielle.mgm.service.portainer.mootse.MootseStackServiceImpl
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mootse")
class MootseStackController(private val mootseStackService: MootseStackServiceImpl) {

    @GetMapping("/{stackId}")
    fun getStack(@PathVariable stackId: Int): MootseStack {
        return mootseStackService.getStackById(stackId)
    }

    @GetMapping
    fun getStacks(): List<MootseStack> {
        return mootseStackService.getAllStacks()
    }

    @PostMapping()
    fun createStack(@RequestBody mootseStack: MootseStack): ResponseEntity<Stack> {
        val response = mootseStackService.createStack(mootseStack)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @PutMapping("/{stackId}")
    fun updateStack(@PathVariable stackId: Int, @RequestBody mootseStack: MootseStack): ResponseEntity<Stack> {
        val response = mootseStackService.updateStack(stackId, mootseStack)
        return ResponseEntity(response, HttpStatus.NO_CONTENT)
    }

}