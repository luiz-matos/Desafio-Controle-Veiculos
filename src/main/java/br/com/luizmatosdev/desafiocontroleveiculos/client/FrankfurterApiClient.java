package br.com.luizmatosdev.desafiocontroleveiculos.client;

import br.com.luizmatosdev.desafiocontroleveiculos.config.FrankfurterApiConfig;
import br.com.luizmatosdev.desafiocontroleveiculos.dto.FrankfurterApiResponse;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class FrankfurterApiClient {
    private final RestTemplate restTemplate;
    private final String urlApi;

    public FrankfurterApiClient(FrankfurterApiConfig config) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(config.getTimeout());
        requestFactory.setReadTimeout(config.getTimeout());
        this.restTemplate = new RestTemplate(requestFactory);
        this.urlApi = config.getUrl();
    }

    /** @return a cotação do dólar em reais, ou null se a API falhar */
    public BigDecimal buscarValorDolarAgora() {
        try {
            FrankfurterApiResponse response = restTemplate.getForObject(urlApi, FrankfurterApiResponse.class);
            if (Objects.isNull(response)
                    || Objects.isNull(response.rates())
                    || Objects.isNull(response.rates().BRL())) {
                return null;
            }
            return response.rates().BRL();
        } catch (RestClientException e) {
            log.warn("Frankfurter não respondeu a cotação: {}", e.getMessage());
            return null;
        }
    }
}
