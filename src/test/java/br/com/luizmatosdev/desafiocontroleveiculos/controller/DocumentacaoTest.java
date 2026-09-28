package br.com.luizmatosdev.desafiocontroleveiculos.controller;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizmatosdev.desafiocontroleveiculos.ApiTest;
import org.junit.jupiter.api.Test;

class DocumentacaoTest extends ApiTest {

    @Test
    void publicaAEspecificacaoOpenApiSemToken() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths", hasKey("/api/veiculos")))
                .andExpect(jsonPath("$.paths", hasKey("/admin/veiculos")))
                .andExpect(jsonPath("$.components.securitySchemes", hasKey("bearerAuth")))
                .andExpect(jsonPath("$.paths", not(hasKey("/error"))));
    }

    @Test
    void abreOSwaggerUiSemToken() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }
}
