package br.com.luizmatosdev.desafioveiculos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.luizmatosdev.desafioveiculos.client.EconomiaAwesomeApiWsClient;
import br.com.luizmatosdev.desafioveiculos.client.FrankfurterApiClient;
import br.com.luizmatosdev.desafioveiculos.exception.ErroWsException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValorDolarServiceTest {

    @Mock
    private EconomiaAwesomeApiWsClient awesomeApi;

    @Mock
    private FrankfurterApiClient frankfurterApi;

    @InjectMocks
    private ValorDolarService service;

    @Test
    void usaAAwesomeApiQuandoElaResponde() {
        when(awesomeApi.buscarValorDolarAgora()).thenReturn(new BigDecimal("5.19"));

        assertEquals(new BigDecimal("5.19"), service.buscarValorAtual());
        verify(frankfurterApi, never()).buscarValorDolarAgora();
    }

    @Test
    void usaAFrankfurterQuandoAAwesomeApiFalha() {
        when(frankfurterApi.buscarValorDolarAgora()).thenReturn(new BigDecimal("5.18"));

        assertEquals(new BigDecimal("5.18"), service.buscarValorAtual());
    }

    @Test
    void lancaErroWsQuandoAsDuasFalham() {
        assertThrows(ErroWsException.class, () -> service.buscarValorAtual());
    }
}
