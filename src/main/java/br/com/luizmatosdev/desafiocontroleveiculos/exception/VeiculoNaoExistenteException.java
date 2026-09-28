package br.com.luizmatosdev.desafiocontroleveiculos.exception;

import br.com.luizmatosdev.desafiocontroleveiculos.enums.Retorno;

public class VeiculoNaoExistenteException extends EntityNotFoundException {
    public VeiculoNaoExistenteException() {
        super(Retorno.VEICULO_NAO_EXISTENTE);
    }
}
