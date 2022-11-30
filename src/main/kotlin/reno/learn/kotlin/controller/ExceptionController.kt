package reno.learn.kotlin.controller

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import reno.learn.kotlin.exception.AbbreviationNotFoundException
import reno.learn.kotlin.exception.AbbreviationSaveException
import reno.learn.kotlin.exception.InvalidRequestException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionController {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(AbbreviationNotFoundException::class)
    @Suppress("UnusedPrivateMember")
    fun handleAbbreviationNotFoundException(
        servletRequest: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<String> {
        logger.warn { exception.message }
        return ResponseEntity("Abbreviation not found", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AbbreviationSaveException::class)
    @Suppress("UnusedPrivateMember")
    fun handleAbbreviationSaveException(
        servletRequest: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<String> {
        logger.warn { exception.message }
        return ResponseEntity("Unable to delete abbreviation", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(InvalidRequestException::class)
    @Suppress("UnusedPrivateMember")
    fun handleInvalidRequestException(
        servletRequest: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<String> {
        logger.warn { exception.message }
        return ResponseEntity("Invalid request", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    @Suppress("UnusedPrivateMember")
    fun handleException(
        servletRequest: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<String> {
        logger.warn { exception.message }
        return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
