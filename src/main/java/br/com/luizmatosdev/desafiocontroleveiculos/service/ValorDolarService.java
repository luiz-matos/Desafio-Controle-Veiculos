package br.com.luizmatosdev.desafiocontroleveiculos.service;

import br.com.luizmatosdev.desafiocontroleveiculos.client.EconomiaAwesomeApiWsClient;
import br.com.luizmatosdev.desafiocontroleveiculos.client.FrankfurterApiClient;
import br.com.luizmatosdev.desafiocontroleveiculos.exception.ErroWsException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValorDolarService {

    private final EconomiaAwesomeApiWsClient economiaAwesomeApiWsClient;
    private final FrankfurterApiClient frankfurterApiClient;

    /**
     * Busca o valor atual do dólar em relação ao real.
     * Tenta buscar primeiro na API AwesomeAPI, caso falhe, tenta na API Frankfurter.
     * O resultado é armazenado em cache, utilizando Redis.
     *
     * @return Valor atual do dólar
     * @throws ErroWsException se ambas as APIs falharem
     */
    @Cacheable(value = "dolar", key = "'valor-atual'")
    public BigDecimal buscarValorAtual() {
        BigDecimal valorAwesomeApi = economiaAwesomeApiWsClient.buscarValorDolarAgora();
        if (valorAwesomeApi != null) {
            return valorAwesomeApi;
        }

        BigDecimal valorFrankApi = frankfurterApiClient.buscarValorDolarAgora();
        if (valorFrankApi != null) {
            return valorFrankApi;
        }

        throw new ErroWsException();
    }
}
