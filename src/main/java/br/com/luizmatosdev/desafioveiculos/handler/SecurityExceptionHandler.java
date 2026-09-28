package br.com.luizmatosdev.desafioveiculos.handler;

import br.com.luizmatosdev.desafioveiculos.enums.Retorno;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

/** Respostas 401 e 403 da segurança, que acontecem antes do controller, no mesmo envelope da API. */
@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final JsonMapper jsonMapper;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        escrever(response, HttpStatus.UNAUTHORIZED, Retorno.NAO_AUTENTICADO);
    }

    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        escrever(response, HttpStatus.FORBIDDEN, Retorno.ACESSO_NEGADO);
    }

    private void escrever(HttpServletResponse response, HttpStatus status, Retorno retorno) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        jsonMapper.writeValue(
                response.getOutputStream(), ResponseService.build(retorno.getCodigo(), retorno.getDescricao()));
    }
}
