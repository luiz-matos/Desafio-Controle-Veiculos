package br.com.luizmatosdev.desafioveiculos.controller;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DocumentacaoTest extends ApiTest {

    @Test
    void publicaAEspecificacaoOpenApiSemToken() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths", hasKey("/api/veiculos")))
                .andExpect(jsonPath("$.paths", hasKey("/admin/veiculos")))
                .andExpect(jsonPath("$.components.securitySchemes", hasKey("bearerAuth")));
    }

    @Test
    void abreOSwaggerUiSemToken() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}
