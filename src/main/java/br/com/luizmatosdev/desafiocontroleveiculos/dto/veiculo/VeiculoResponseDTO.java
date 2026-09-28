package br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo;

import java.math.BigDecimal;
import java.util.UUID;

public record VeiculoResponseDTO(
        UUID id, String veiculo, String marca, Integer ano, String descricao, BigDecimal valor, String placa) {}
