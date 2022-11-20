package reno.learn.kotlin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import reno.learn.kotlin.exception.AbbreviationNotFoundException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionController {

    @ExceptionHandler(AbbreviationNotFoundException::class)
    @Suppress("UnusedPrivateMember")
    fun handleAbbreviationNotFoundException(
        servletRequest: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<String> {
        return ResponseEntity("Abbreviation not found", HttpStatus.NOT_FOUND)
    }
}
