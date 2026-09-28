package br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo;

import java.math.BigDecimal;
import org.springframework.data.domain.Sort;

public record ListarVeiculosDTO(
        Integer page, Integer size, Sort sort, String marca, Integer ano, BigDecimal minPreco, BigDecimal maxPreco) {}
