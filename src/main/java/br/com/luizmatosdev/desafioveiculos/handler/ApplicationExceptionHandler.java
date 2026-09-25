package br.com.luizmatosdev.desafioveiculos.handler;

import br.com.luizmatosdev.desafioveiculos.dto.InputErrorDTO;
import br.com.luizmatosdev.desafioveiculos.enums.Retorno;
import br.com.luizmatosdev.desafioveiculos.exception.EntityNotFoundException;
import br.com.luizmatosdev.desafioveiculos.exception.GlobalException;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    protected ResponseEntity<ResponseService<Void>> handleAtivacaoException(GlobalException ex) {
        return montarResposta(ex.getRetorno(), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseService<Void>> handleEntidadeNaoEncontrada(EntityNotFoundException ex) {
        return montarResposta(ex.getRetorno(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodValidationException.class)
    protected ResponseEntity<ResponseService<Void>> handleAtivacaoException(MethodValidationException ex) {
        ex.getParameterValidationResults().forEach(violation -> log.error(violation.toString()));
        return montarResposta(Retorno.CAMPO_INVALIDO_OU_OBRIGATORIO, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseService<CampoInvalidoResponse>> handleConstraintViolationException(ConstraintViolationException ex) {
        var errors = new ArrayList<InputErrorDTO>();

        ex.getConstraintViolations().forEach(violation -> {
            var error = new InputErrorDTO(violation.getPropertyPath().toString(), violation.getMessage());
            errors.add(error);
        });

        return montarRespostaCamposInvalidos(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseService<CampoInvalidoResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new InputErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();

        return montarRespostaCamposInvalidos(errors);
    }

    private ResponseEntity<ResponseService<CampoInvalidoResponse>> montarRespostaCamposInvalidos(List<InputErrorDTO> errors) {
        var response = new CampoInvalidoResponse(errors);

        return new ResponseEntity<>(ResponseService.build(
                response,
                Retorno.CAMPO_INVALIDO_OU_OBRIGATORIO.getCodigo(),
                Retorno.CAMPO_INVALIDO_OU_OBRIGATORIO.getDescricao()),
                HttpStatus.BAD_REQUEST
        );
    }

    private ResponseEntity<ResponseService<Void>> montarResposta(Retorno retorno, HttpStatus httpStatus) {
        return new ResponseEntity<>(ResponseService.build(retorno.getCodigo(), retorno.getDescricao()), httpStatus);
    }
}
