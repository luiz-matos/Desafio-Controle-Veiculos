package br.com.luizmatosdev.desafioveiculos.entity;

import java.math.BigDecimal;

/** Os dados que o cadastro e as alterações passam para o veículo, sem depender dos DTOs da API. */
public record DadosVeiculo(
        String veiculo, String marca, Integer ano, String descricao, BigDecimal valor, String placa) {}
