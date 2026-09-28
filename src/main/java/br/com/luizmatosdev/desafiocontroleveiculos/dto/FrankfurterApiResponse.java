package br.com.luizmatosdev.desafiocontroleveiculos.dto;

import java.math.BigDecimal;

public record FrankfurterApiResponse(String base, String date, Rates rates) {
    public record Rates(BigDecimal BRL) {}
}
