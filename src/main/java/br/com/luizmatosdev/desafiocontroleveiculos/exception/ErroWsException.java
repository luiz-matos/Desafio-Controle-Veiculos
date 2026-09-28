package br.com.luizmatosdev.desafiocontroleveiculos.exception;

import br.com.luizmatosdev.desafiocontroleveiculos.enums.Retorno;

public class ErroWsException extends GlobalException {
    public ErroWsException() {
        super(Retorno.ERRO_WS);
    }
}
