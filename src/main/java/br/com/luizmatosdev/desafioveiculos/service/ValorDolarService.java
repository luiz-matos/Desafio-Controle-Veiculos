package br.com.luizmatosdev.desafioveiculos.service;

import br.com.luizmatosdev.desafioveiculos.client.EconomiaAwesomeApiWsClient;
import br.com.luizmatosdev.desafioveiculos.client.FrankfurterApiClient;
import br.com.luizmatosdev.desafioveiculos.exception.ErroWsException;
import br.com.luizmatosdev.desafioveiculos.interfaces.service.IValorDolarService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValorDolarService implements IValorDolarService {

    private final EconomiaAwesomeApiWsClient economiaAwesomeApiWsClient;
    private final FrankfurterApiClient frankfurterApiClient;

    @Override
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
