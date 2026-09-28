package br.com.luizmatosdev.desafioveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.com.luizmatosdev.desafioveiculos.client.EconomiaAwesomeApiWsClient;
import br.com.luizmatosdev.desafioveiculos.client.FrankfurterApiClient;
import br.com.luizmatosdev.desafioveiculos.interfaces.service.IValorDolarService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/** Redis apontando para uma porta sem nada: a cotação precisa sair mesmo sem o cache. */
@SpringBootTest(properties = "spring.data.redis.port=6399")
@ActiveProfiles("test")
class CacheSemRedisTest {

    @MockitoBean
    private EconomiaAwesomeApiWsClient awesomeApi;

    @MockitoBean
    private FrankfurterApiClient frankfurterApi;

    @Autowired
    private IValorDolarService valorDolarService;

    @Test
    void buscaACotacaoMesmoComORedisForaDoAr() {
        when(awesomeApi.buscarValorDolarAgora()).thenReturn(new BigDecimal("5.19"));

        assertEquals(new BigDecimal("5.19"), valorDolarService.buscarValorAtual());
    }
}
