package reno.learn.kotlin.controller

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import reno.learn.kotlin.exception.AbbreviationNotFoundException
import reno.learn.kotlin.exception.AbbreviationSaveException
import reno.learn.kotlin.exception.InvalidRequestException
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

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

    @ExceptionHandler(value = [ConstraintViolationException::class, MissingServletRequestParameterException::class])
    @Suppress("UnusedPrivateMember")
    fun handleConstraintViolationException(
        servletRequest: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<String> {
        logger.warn { exception.message }
        return ResponseEntity(exception.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @Suppress("UnusedPrivateMember")
    fun handleHttpMessageNotReadableException(
        servletRequest: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<String> {
        logger.warn { exception.message }
        return ResponseEntity(
            "Invalid body, please refer the documentation at /swagger-ui/index.html#/",
            HttpStatus.BAD_REQUEST
        )
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
