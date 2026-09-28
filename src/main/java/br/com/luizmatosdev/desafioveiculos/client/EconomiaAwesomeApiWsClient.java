package br.com.luizmatosdev.desafioveiculos.client;

import br.com.luizmatosdev.desafioveiculos.config.EconomiaAwesomeApiWsConfig;
import br.com.luizmatosdev.desafioveiculos.dto.EconomiaAwesomeApiWsResponse;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class EconomiaAwesomeApiWsClient {
    private final RestTemplate restTemplate;
    private final String urlApi;

    public EconomiaAwesomeApiWsClient(EconomiaAwesomeApiWsConfig config) {
        this.restTemplate = new RestTemplate();
        this.urlApi = config.getUrl();
    }

    /** @return a cotação do dólar em reais, ou null se a API falhar */
    public BigDecimal buscarValorDolarAgora() {
        try {
            EconomiaAwesomeApiWsResponse response =
                    restTemplate.getForObject(urlApi, EconomiaAwesomeApiWsResponse.class);
            if (Objects.isNull(response) || Objects.isNull(response.USDBRL())) {
                return null;
            }
            return response.USDBRL().ask();
        } catch (RestClientException e) {
            log.warn("AwesomeAPI não respondeu a cotação: {}", e.getMessage());
            return null;
        }
    }
}
