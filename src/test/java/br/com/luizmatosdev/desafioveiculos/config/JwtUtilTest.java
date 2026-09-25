package br.com.luizmatosdev.desafioveiculos.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtUtilTest {

    private final UserDetails admin =
            User.withUsername("admin").password("x").roles("ADMIN").build();

    @Test
    void leOUsuarioDeUmTokenAssinadoComOMesmoSegredo() {
        JwtUtil jwtUtil = new JwtUtil("segredo-a-com-pelo-menos-32-bytes-de-tamanho");

        assertEquals("admin", jwtUtil.extractUsername(jwtUtil.generateToken(admin)));
    }

    @Test
    void recusaTokenAssinadoComOutroSegredo() {
        String token = new JwtUtil("segredo-a-com-pelo-menos-32-bytes-de-tamanho").generateToken(admin);
        JwtUtil outroSegredo = new JwtUtil("segredo-b-com-pelo-menos-32-bytes-de-tamanho");

        assertThrows(JwtException.class, () -> outroSegredo.extractUsername(token));
    }
}
