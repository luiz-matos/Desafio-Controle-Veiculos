package br.com.luizmatosdev.desafiocontroleveiculos.dto;

import java.math.BigDecimal;

public record EconomiaAwesomeApiWsResponse(DolarApiWsResponse USDBRL) {
    public record DolarApiWsResponse(BigDecimal ask) {}
}
