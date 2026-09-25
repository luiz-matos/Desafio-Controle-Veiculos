package br.com.luizmatosdev.desafioveiculos.exception;

import br.com.luizmatosdev.desafioveiculos.enums.Retorno;

public class VeiculoNaoExistenteException extends EntityNotFoundException {
    public VeiculoNaoExistenteException() {
        super(Retorno.VEICULO_NAO_EXISTENTE);
    }
}
