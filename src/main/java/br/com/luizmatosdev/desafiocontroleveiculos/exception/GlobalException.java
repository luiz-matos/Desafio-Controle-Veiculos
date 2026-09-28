package br.com.luizmatosdev.desafiocontroleveiculos.exception;

import br.com.luizmatosdev.desafiocontroleveiculos.enums.Retorno;
import lombok.Getter;

@Getter
public abstract class GlobalException extends RuntimeException {
    private final Retorno retorno;

    protected GlobalException(Retorno retorno) {
        super(retorno.getDescricao());
        this.retorno = retorno;
    }
}
