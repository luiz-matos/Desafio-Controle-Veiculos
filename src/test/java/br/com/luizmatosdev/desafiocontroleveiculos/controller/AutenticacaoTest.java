package br.com.luizmatosdev.desafiocontroleveiculos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizmatosdev.desafiocontroleveiculos.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AutenticacaoTest extends ApiTest {

    @Test
    void recusaSenhaErradaCom401() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void recusaRequisicaoSemTokenCom401() throws Exception {
        mockMvc.perform(get("/api/veiculos")).andExpect(status().isUnauthorized());
    }

    @Test
    void recusaTokenInvalidoCom401() throws Exception {
        mockMvc.perform(comToken(get("/api/veiculos"), "abc.def.ghi")).andExpect(status().isUnauthorized());
    }

    @Test
    void recusaTokenComAssinaturaAdulteradaCom401() throws Exception {
        String token = tokenUser();
        String adulterado = token.substring(0, token.length() - 4) + "AAAA";

        mockMvc.perform(comToken(get("/api/veiculos"), adulterado)).andExpect(status().isUnauthorized());
    }

    @Test
    void recusaUsuarioSemPerfilAdminCom403() throws Exception {
        mockMvc.perform(comToken(post("/admin/veiculos"), tokenUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculo("Civic", "Honda", 2020, "10000", "ABC1234")))
                .andExpect(status().isForbidden());
    }
}
