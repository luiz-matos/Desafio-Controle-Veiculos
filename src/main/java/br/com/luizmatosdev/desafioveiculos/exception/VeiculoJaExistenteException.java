package br.com.luizmatosdev.desafioveiculos.exception;

import br.com.luizmatosdev.desafioveiculos.enums.Retorno;

public class VeiculoJaExistenteException extends GlobalException {
    public VeiculoJaExistenteException() {
        super(Retorno.PLACA_VEICULO_JA_EXISTE);
    }
}
