package br.com.luizmatosdev.desafioveiculos.dto.veiculo;

import br.com.luizmatosdev.desafioveiculos.entity.Veiculo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlterarParcialmenteVeiculoRequestDTO(
        String veiculo,
        String marca,
        Integer ano,
        String descricao,
        BigDecimal valor,
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
