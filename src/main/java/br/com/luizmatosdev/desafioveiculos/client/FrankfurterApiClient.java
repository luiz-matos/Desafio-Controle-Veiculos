package br.com.luizmatosdev.desafioveiculos.client;

import br.com.luizmatosdev.desafioveiculos.config.FrankfurterApiConfig;
import br.com.luizmatosdev.desafioveiculos.dto.FrankfurterApiResponse;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FrankfurterApiClient {
    private final RestTemplate restTemplate;
    private final String urlApi;

    public FrankfurterApiClient(FrankfurterApiConfig config) {
        this.restTemplate = new RestTemplate();
        this.urlApi = config.getUrl();
    }

    public BigDecimal buscarValorDolarAgora() {
        try {
            FrankfurterApiResponse response = restTemplate.getForObject(urlApi, FrankfurterApiResponse.class);
            if (Objects.isNull(response)
                    || Objects.isNull(response.rates())
                    || Objects.isNull(response.rates().BRL())) {
                return null;
            }
            return response.rates().BRL();
        } catch (Exception e) {
            return null;
        }
    }
}
