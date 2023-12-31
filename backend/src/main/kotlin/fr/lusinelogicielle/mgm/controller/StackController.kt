package fr.lusinelogicielle.mgm.controller

import fr.lusinelogicielle.mgm.model.portainer.Stack
import fr.lusinelogicielle.mgm.service.portainer.stack.StackServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stacks")
class StackController(private val stackService: StackServiceImpl) {

    @GetMapping("/{stackId}")
    fun getStack(@PathVariable stackId: Int): Stack {
        return stackService.getStackById(stackId)
    }

    @GetMapping
    fun getStacks(): List<Stack> {
        return stackService.getAllStacks()
    }

    @DeleteMapping("/{stackId}")
    fun deleteStack(@PathVariable stackId: Int): ResponseEntity<Unit> {
        stackService.deleteStack(stackId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/stop/{stackId}")
    fun stopStack(@PathVariable stackId: Int): ResponseEntity<Unit> {
        stackService.stopStack(stackId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/start/{stackId}")
    fun startStack(@PathVariable stackId: Int): ResponseEntity<Unit> {
        stackService.startStack(stackId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
