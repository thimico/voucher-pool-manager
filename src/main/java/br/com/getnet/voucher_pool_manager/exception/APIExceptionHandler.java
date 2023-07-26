/*
 * Plataforma 2.0
 * Copyright (c) 2023 CRDC - CENTRAL DE REGISTRO DE DIREITOS CREDITORIOS S.A.,
 * Inc. All Rights Reserved.
 *
 * Este software contém informações confidenciais e de propriedade da CRDC S.A.
 * ("Informações Confidenciais"). Você não deve divulgar qualquer tipo de
 * informações confidenciais e deve usá-las apenas, de acordo com os termos do
 * contrato de licença firmado com a CRDC.
 *
 * A CRDC não faz declarações ou garantias sobre a adequação do software,
 * expressa ou implicitamente, incluindo, mas não se limitando, a garantias de
 * comercialização, adequação para um determinado fim ou qualquer tipo de violação.
 *
 * A CENTRAL DE REGISTRO DE DIREITOS CREDITORIOS S.A. NÃO SERÁ RESPONSÁVEL POR
 * QUAISQUER DANOS SOFRIDOS PELO LICENCIADO EM DECORRÊNCIA DO USO, MODIFICAÇÃO OU
 * DISTRIBUIÇÃO DESTE SOFTWARE OU SEUS DERIVADOS.
 */

package br.com.getnet.voucher_pool_manager.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DateTimeParseException.class})
    protected ResponseEntity<ErrorResponse> handleDateParseException(DateTimeParseException ex) {
        String bodyOfResponse = "Formato de data obrigatório: dd.MM.yyyy";
        return _doHandle(bodyOfResponse, HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return _doHandle(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(value = { ValidationException.class})
    protected ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return _doHandle(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return _doHandle(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleThrowable(Throwable ex) {
        return _doHandle(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ErrorResponse> _doHandle(HttpStatus status, Throwable cause) {
        return _doHandle(cause.getMessage(), status, cause);
    }

    private ResponseEntity<ErrorResponse> _doHandle(String message, HttpStatus status, Throwable cause) {
        String code = status.getReasonPhrase();
        String description = cause.getClass().getSimpleName();
        ErrorResponse errorResponse = new ErrorResponse(code, description, message);
        return new ResponseEntity<>(errorResponse, status);
    }

}
