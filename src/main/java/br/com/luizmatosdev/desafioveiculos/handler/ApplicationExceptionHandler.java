package br.com.luizmatosdev.desafioveiculos.handler;

import br.com.luizmatosdev.desafioveiculos.dto.InputErrorDTO;
import br.com.luizmatosdev.desafioveiculos.enums.Retorno;
import br.com.luizmatosdev.desafioveiculos.exception.EntityNotFoundException;
import br.com.luizmatosdev.desafioveiculos.exception.ErroWsException;
import br.com.luizmatosdev.desafioveiculos.exception.GlobalException;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    protected ResponseEntity<ResponseService<Void>> handleGlobalException(GlobalException ex) {
        return montarResposta(ex.getRetorno(), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ErroWsException.class)
    public ResponseEntity<ResponseService<Void>> handleErroWsException(ErroWsException ex) {
        return montarResposta(ex.getRetorno(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseService<Void>> handleEntidadeNaoEncontrada(EntityNotFoundException ex) {
        return montarResposta(ex.getRetorno(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodValidationException(
            MethodValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.getParameterValidationResults().forEach(violation -> log.error(violation.toString()));
        Retorno retorno = Retorno.CAMPO_INVALIDO_OU_OBRIGATORIO;
        return new ResponseEntity<>(
                ResponseService.build(retorno.getCodigo(), retorno.getDescricao()),
                headers,
                HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseService<CampoInvalidoResponse>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        var errors = new ArrayList<InputErrorDTO>();

        ex.getConstraintViolations().forEach(violation -> {
            var error = new InputErrorDTO(violation.getPropertyPath().toString(), violation.getMessage());
            errors.add(error);
        });

        return montarRespostaCamposInvalidos(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new InputErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ResponseEntity<>(montarRespostaCamposInvalidos(errors).getBody(), headers, HttpStatus.BAD_REQUEST);
    }

    /** Os outros erros que o Spring MVC trata (JSON malformado, tipo errado, rota ou método inexistente). */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Retorno retorno = Retorno.doStatus(status.value());
        return new ResponseEntity<>(
                ResponseService.build(retorno.getCodigo(), retorno.getDescricao()), headers, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ResponseService<Void>> handleAuthenticationException(AuthenticationException ex) {
        return montarResposta(Retorno.NAO_AUTENTICADO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseService<Void>> handleErroInesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        return montarResposta(Retorno.ERRO_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseService<CampoInvalidoResponse>> montarRespostaCamposInvalidos(
            List<InputErrorDTO> errors) {
        var response = new CampoInvalidoResponse(errors);

        return new ResponseEntity<>(
                ResponseService.build(
                        response,
                        Retorno.CAMPO_INVALIDO_OU_OBRIGATORIO.getCodigo(),
                        Retorno.CAMPO_INVALIDO_OU_OBRIGATORIO.getDescricao()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    protected ResponseEntity<ResponseService<Void>> handlePropertyReferenceException(PropertyReferenceException ex) {
        log.warn(ex.getMessage());
        return montarResposta(Retorno.CAMPO_INVALIDO_OU_OBRIGATORIO, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ResponseService<Void>> montarResposta(Retorno retorno, HttpStatus httpStatus) {
        return new ResponseEntity<>(ResponseService.build(retorno.getCodigo(), retorno.getDescricao()), httpStatus);
    }
}
