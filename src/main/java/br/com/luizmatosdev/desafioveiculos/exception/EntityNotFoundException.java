package br.com.luizmatosdev.desafioveiculos.exception;

import br.com.luizmatosdev.desafioveiculos.enums.Retorno;
import lombok.Getter;

@Getter
public abstract class EntityNotFoundException extends RuntimeException {
    private final Retorno retorno;

    protected EntityNotFoundException(Retorno retorno) {
        super(retorno.getDescricao());
        this.retorno = retorno;
    }
}
