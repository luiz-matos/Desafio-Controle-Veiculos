package br.com.luizmatosdev.desafiocontroleveiculos.mapper;

import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.entity.Veiculo;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class VeiculoMapper {
    private VeiculoMapper() {
        throw new UnsupportedOperationException("Classe não instanciada");
    }

    /**
     * @param valorDolar cotação do dólar em reais; o valor do veículo é cadastrado em reais e sai em dólar
     */
    public static VeiculoResponseDTO toResponseDTO(Veiculo veiculo, BigDecimal valorDolar) {
        return new VeiculoResponseDTO(
                veiculo.getId(),
                veiculo.getVeiculo(),
                veiculo.getMarca(),
                veiculo.getAno(),
                veiculo.getDescricao(),
                converterParaDolar(veiculo.getValor(), valorDolar),
                veiculo.getPlaca());
    }

    private static BigDecimal converterParaDolar(BigDecimal valorEmReais, BigDecimal valorDolar) {
        if (valorEmReais == null) {
            return null;
        }
        return valorEmReais.divide(valorDolar, 2, RoundingMode.HALF_UP);
    }
}
