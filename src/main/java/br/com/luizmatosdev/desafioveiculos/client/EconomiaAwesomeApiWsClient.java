package br.com.luizmatosdev.desafioveiculos.client;

import br.com.luizmatosdev.desafioveiculos.config.EconomiaAwesomeApiWsConfig;
import br.com.luizmatosdev.desafioveiculos.dto.EconomiaAwesomeApiWsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EconomiaAwesomeApiWsClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final EconomiaAwesomeApiWsConfig config;

    public BigDecimal buscarValorDolarAgora() {
        try {
            EconomiaAwesomeApiWsResponse response = restTemplate.getForObject(config.getUrl(), EconomiaAwesomeApiWsResponse.class);
            if (Objects.isNull(response) || Objects.isNull(response.USDBRL())) {
                return null;
            }
            return response.USDBRL().ask();
        } catch (Exception e) {
            return null;
        }
    }
}
