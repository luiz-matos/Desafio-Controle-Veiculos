package br.com.luizmatosdev.desafiocontroleveiculos.dto.token;

public record TokenResponse(String token, String type) {

    public TokenResponse(String token) {
        this(token, "Bearer");
    }
}
