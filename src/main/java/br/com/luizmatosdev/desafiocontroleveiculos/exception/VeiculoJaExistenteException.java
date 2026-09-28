package br.com.luizmatosdev.desafiocontroleveiculos.exception;

import br.com.luizmatosdev.desafiocontroleveiculos.enums.Retorno;

public class VeiculoJaExistenteException extends GlobalException {
    public VeiculoJaExistenteException() {
        super(Retorno.PLACA_VEICULO_JA_EXISTE);
    }
}
