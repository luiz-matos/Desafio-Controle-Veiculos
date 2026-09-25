package br.com.luizmatosdev.desafioveiculos.controller;

import br.com.luizmatosdev.desafioveiculos.interfaces.service.IValorDolarService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Com servidor de verdade, porque o MockMvc não encaminha os erros para o /error.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ErroHttpTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private IValorDolarService valorDolarService;

    private final HttpClient http = HttpClient.newHttpClient();

    @Test
    void respondeOErroDeVerdadeEmVezDeUmaRespostaVazia() throws Exception {
        HttpResponse<String> resposta = enviar(HttpRequest.newBuilder(uri("/api/veiculos/nao-e-uuid"))
                .header("Authorization", "Bearer " + tokenAdmin())
                .GET());

        assertEquals(400, resposta.statusCode());
        assertTrue(resposta.body().contains("/api/veiculos/nao-e-uuid"), resposta.body());
    }

    private String tokenAdmin() throws Exception {
        HttpResponse<String> resposta = enviar(HttpRequest.newBuilder(uri("/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"admin\",\"password\":\"admin123\"}")));
        return JsonPath.read(resposta.body(), "$.token");
    }

    private HttpResponse<String> enviar(HttpRequest.Builder request) throws Exception {
        return http.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String caminho) {
        return URI.create("http://localhost:" + port + caminho);
    }
}
