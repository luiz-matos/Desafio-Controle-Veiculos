package br.com.luizmatosdev.desafioveiculos.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ValidacaoTest extends ApiTest {

    @Test
    void recusaCadastroSemOsCamposObrigatorios() throws Exception {
        mockMvc.perform(comToken(post("/admin/veiculos"), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.codigo").value(-90))
                .andExpect(jsonPath(
                        "$.data.fields[*].field", containsInAnyOrder("veiculo", "marca", "ano", "valor", "placa")));
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

    @Test
    void recusaAlteracaoParcialComCamposInvalidos() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(patch("/admin/veiculos/" + id), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"veiculo\":\" \",\"valor\":-1,\"placa\":\"PLACAGRANDE123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.fields[*].field", containsInAnyOrder("veiculo", "valor", "placa")));

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenAdmin()))
                .andExpect(jsonPath("$.data.veiculo").value("Civic"));
    }

    @Test
    void alteraSoOCampoInformadoNaAlteracaoParcial() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(patch("/admin/veiculos/" + id), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"Sedan\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.descricao").value("Sedan"))
                .andExpect(jsonPath("$.data.veiculo").value("Civic"))
                .andExpect(jsonPath("$.data.placa").value("ABC1234"));
    }
}
