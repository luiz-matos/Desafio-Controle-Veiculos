package br.com.luizmatosdev.desafioveiculos.dto.veiculo;

import br.com.luizmatosdev.desafioveiculos.entity.Veiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VeiculoRequestDTO(
    @NotBlank
    @Size(max = 100)
    String veiculo,

    @NotBlank
    @Size(max = 100)
    String marca,

    @NotNull
    Integer ano,

    String descricao,

    @PositiveOrZero
    BigDecimal valor,

    @NotBlank
    @Size(max = 8)
    String placa
) {
    public Veiculo toVeiculo() {
        Veiculo entity = new Veiculo();
        entity.setVeiculo(this.veiculo);
        entity.setMarca(this.marca);
        entity.setAno(this.ano);
        entity.setDescricao(this.descricao);
        entity.setValor(this.valor);
        entity.setPlaca(this.placa);
        return entity;
    }
}
