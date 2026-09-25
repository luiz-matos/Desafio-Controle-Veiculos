package br.com.luizmatosdev.desafioveiculos.dto.veiculo;

import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

public record ListarVeiculosDTO(
    Integer page,
    Integer size,
    Sort sort,
    String marca,
    Integer ano,
    BigDecimal minPreco,
    BigDecimal maxPreco
) {
}
