package br.com.luizmatosdev.desafioveiculos.mapper;

import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafioveiculos.entity.Veiculo;

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
            veiculo.getValor().divide(valorDolar, 2, RoundingMode.HALF_UP),
            veiculo.getPlaca()
        );
    }
}
