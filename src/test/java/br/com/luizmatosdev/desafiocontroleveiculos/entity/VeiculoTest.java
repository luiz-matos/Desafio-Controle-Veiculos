package br.com.luizmatosdev.desafiocontroleveiculos.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class VeiculoTest {

    private final Veiculo civic =
            Veiculo.cadastrar(new DadosVeiculo("Civic", "Honda", 2020, "Sedan", new BigDecimal("10000"), "ABC1234"));

    @Test
    void alteraTodosOsCamposNaAlteracaoCompleta() {
        civic.alterar(new DadosVeiculo("Fit", "Honda", 2019, null, new BigDecimal("8000"), "DEF5678"));

        assertEquals("Fit", civic.getVeiculo());
        assertEquals(2019, civic.getAno());
        assertNull(civic.getDescricao());
        assertEquals("DEF5678", civic.getPlaca());
    }

    @Test
    void mantemOsCamposNulosNaAlteracaoParcial() {
        civic.alterarParcialmente(new DadosVeiculo(null, null, null, "Sedan completo", null, null));

        assertEquals("Civic", civic.getVeiculo());
        assertEquals("Sedan completo", civic.getDescricao());
        assertEquals(new BigDecimal("10000"), civic.getValor());
        assertEquals("ABC1234", civic.getPlaca());
    }

    @Test
    void soConsideraTrocaDePlacaQuandoAPlacaNovaEDiferente() {
        assertTrue(civic.trocaDePlacaPara("DEF5678"));
        assertFalse(civic.trocaDePlacaPara("ABC1234"));
        assertFalse(civic.trocaDePlacaPara(null));
    }
}
