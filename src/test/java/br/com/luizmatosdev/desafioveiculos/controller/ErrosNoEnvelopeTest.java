package br.com.luizmatosdev.desafioveiculos.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/** Toda resposta de erro vem no envelope, com o código de retorno. */
class ErrosNoEnvelopeTest extends ApiTest {

    @Test
    void semToken() throws Exception {
        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message.codigo").value(-70));
    }

    @Test
    void senhaErrada() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"errada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message.codigo").value(-70));
    }

    @Test
    void perfilSemAcesso() throws Exception {
        mockMvc.perform(comToken(post("/admin/veiculos"), tokenUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculo("Civic", "Honda", 2020, "10000", "ABC1234")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message.codigo").value(-71));
    }

    @Test
    void jsonMalformado() throws Exception {
        mockMvc.perform(comToken(post("/admin/veiculos"), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{json quebrado"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.codigo").value(-91));
    }

    @Test
    void idQueNaoEUuid() throws Exception {
        mockMvc.perform(comToken(get("/api/veiculos/nao-e-uuid"), tokenUser()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.codigo").value(-91));
    }

    @Test
    void filtroComTipoErrado() throws Exception {
        mockMvc.perform(comToken(get("/api/veiculos?ano=abc"), tokenUser()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.codigo").value(-91));
    }

    @Test
    void metodoQueARotaNaoAceita() throws Exception {
        mockMvc.perform(comToken(put("/api/veiculos"), tokenAdmin()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message.codigo").value(-91));
    }

    @Test
    void rotaQueNaoExiste() throws Exception {
        mockMvc.perform(comToken(get("/api/rota-que-nao-existe"), tokenUser()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message.codigo").value(-92));
    }

    @Test
    void erroInesperado() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
        when(valorDolarService.buscarValorAtual()).thenThrow(new IllegalStateException("falha qualquer"));

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenUser()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message.codigo").value(-99));
    }

    @Test
    void erroQueChegaPeloErrorDoServidor() throws Exception {
        mockMvc.perform(get("/error").requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message.codigo").value(-99));
    }
}
