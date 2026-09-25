package br.com.luizmatosdev.desafioveiculos.controller;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListagemTest extends ApiTest {

    @BeforeEach
    void cadastrarVeiculos() throws Exception {
        criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
        criarVeiculo(veiculo("Corolla", "Toyota", 2021, "20000", "DEF5678"));
        criarVeiculo(veiculo("Fit", "Honda", 2019, "8000", "GHI9012"));
    }

    @Test
    void filtraPelaMarca() throws Exception {
        listar("?marca=Toyota&sort=veiculo")
                .andExpect(jsonPath("$.content[*].veiculo", contains("Corolla")));
    }

    @Test
    void filtraPeloAno() throws Exception {
        listar("?ano=2020")
                .andExpect(jsonPath("$.content[*].veiculo", contains("Civic")));
    }

    @Test
    void filtraPelaFaixaDeValorEmReais() throws Exception {
        listar("?minPreco=9000&maxPreco=20000&sort=veiculo")
                .andExpect(jsonPath("$.content[*].veiculo", contains("Civic", "Corolla")));
    }

    @Test
    void ordenaPeloCampoInformado() throws Exception {
        listar("?sort=veiculo,desc")
                .andExpect(jsonPath("$.content[*].veiculo", contains("Fit", "Corolla", "Civic")));
    }

    @Test
    void paginaOResultadoOrdenado() throws Exception {
        listar("?sort=veiculo&size=2&page=1")
                .andExpect(jsonPath("$.content[*].veiculo", contains("Fit")))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void naoListaVeiculoExcluido() throws Exception {
        String id = criarVeiculo(veiculo("Ka", "Ford", 2015, "5000", "JKL3456"));
        mockMvc.perform(comToken(delete("/admin/veiculos/" + id), tokenAdmin()))
                .andExpect(status().isOk());

        listar("?marca=Ford")
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void recusaOrdenacaoPorCampoQueNaoExiste() throws Exception {
        mockMvc.perform(comToken(get("/api/veiculos?sort=cor"), tokenUser()))
                .andExpect(status().isBadRequest());
    }

    private org.springframework.test.web.servlet.ResultActions listar(String filtros) throws Exception {
        return mockMvc.perform(comToken(get("/api/veiculos" + filtros), tokenUser()))
                .andExpect(status().isOk());
    }
}
