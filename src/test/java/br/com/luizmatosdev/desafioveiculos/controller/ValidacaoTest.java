package br.com.luizmatosdev.desafioveiculos.controller;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ValidacaoTest extends ApiTest {

    @Test
    void recusaCadastroSemOsCamposObrigatorios() throws Exception {
        mockMvc.perform(comToken(post("/admin/veiculos"), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.codigo").value(-90))
                .andExpect(jsonPath("$.data.fields[*].field", containsInAnyOrder("veiculo", "marca", "ano", "placa")));
    }

    @Test
    void recusaCadastroComPlacaMaiorQueOitoCaracteres() throws Exception {
        mockMvc.perform(comToken(post("/admin/veiculos"), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculo("Civic", "Honda", 2020, "10000", "PLACAGRANDE123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.fields[*].field", containsInAnyOrder("placa")));
    }

    @Test
    void dizQualCampoEstaInvalidoNaAlteracao() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(put("/admin/veiculos/" + id), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculo("", "Honda", 2020, "-1", "ABC1234")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.fields[*].field", containsInAnyOrder("veiculo", "valor")));
    }
}
