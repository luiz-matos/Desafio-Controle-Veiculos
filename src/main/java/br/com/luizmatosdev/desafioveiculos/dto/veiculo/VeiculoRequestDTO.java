package br.com.luizmatosdev.desafioveiculos.dto.veiculo;

import br.com.luizmatosdev.desafioveiculos.entity.DadosVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record VeiculoRequestDTO(
        @NotBlank @Size(max = 100) String veiculo,

        @NotBlank @Size(max = 100) String marca,

        @NotNull Integer ano,

        String descricao,

        @NotNull @PositiveOrZero BigDecimal valor,

        @NotBlank @Size(max = 8) String placa) {
    public DadosVeiculo toDados() {
        return new DadosVeiculo(veiculo, marca, ano, descricao, valor, placa);
    }
}
