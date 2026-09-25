package br.com.luizmatosdev.desafioveiculos.interfaces.service;

import br.com.luizmatosdev.desafioveiculos.exception.ErroWsException;
import java.math.BigDecimal;

public interface IValorDolarService {

    /**
     * Busca o valor atual do dólar em relação ao real.
     * Tenta buscar primeiro na API AwesomeAPI, caso falhe, tenta na API Frankfurter.
     * O resultado é armazenado em cache, utilizando Redis.
     *
     * @return Valor atual do dólar
     * @throws ErroWsException se ambas as APIs falharem
     */
    BigDecimal buscarValorAtual();
}
