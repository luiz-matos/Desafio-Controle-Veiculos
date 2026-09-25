package br.com.luizmatosdev.desafioveiculos.exception;


import br.com.luizmatosdev.desafioveiculos.enums.Retorno;

public class ErroWsException extends GlobalException {
    public ErroWsException() {
        super(Retorno.ERRO_WS);
    }
}
