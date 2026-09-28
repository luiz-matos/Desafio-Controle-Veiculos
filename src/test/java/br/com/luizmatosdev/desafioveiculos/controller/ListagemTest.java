package br.com.luizmatosdev.desafioveiculos.controller;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListagemTest extends ApiTest {

    @BeforeEach
    void cadastrarVeiculos() throws Exception {
        criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
        criarVeiculo(veiculo("Corolla", "Toyota", 2021, "20000", "DEF5678"));
        criarVeiculo(veiculo("Fit", "Honda", 2019, "8000", "GHI9012"));
    }

    @Test
    void filtraPelaMarca() throws Exception {
        listar("?marca=Toyota&sort=veiculo").andExpect(jsonPath("$.data.content[*].veiculo", contains("Corolla")));
    }

    @Test
    void filtraPelaMarcaSemDiferenciarMaiuscula() throws Exception {
        listar("?marca=hONDA&sort=veiculo").andExpect(jsonPath("$.data.content[*].veiculo", contains("Civic", "Fit")));
    }

    @Test
    void filtraPeloAno() throws Exception {
        listar("?ano=2020").andExpect(jsonPath("$.data.content[*].veiculo", contains("Civic")));
    }

    @Test
    void filtraPelaFaixaDeValorEmReais() throws Exception {
        listar("?minPreco=9000&maxPreco=20000&sort=veiculo")
                .andExpect(jsonPath("$.data.content[*].veiculo", contains("Civic", "Corolla")));
    }

    @Test
    void ordenaPeloCampoInformado() throws Exception {
        listar("?sort=veiculo,desc")
                .andExpect(jsonPath("$.data.content[*].veiculo", contains("Fit", "Corolla", "Civic")));
    }

    @Test
    void paginaOResultadoOrdenado() throws Exception {
        listar("?sort=veiculo&size=2&page=1")
                .andExpect(jsonPath("$.message.codigo").value(0))
                .andExpect(jsonPath("$.data.content[*].veiculo", contains("Fit")))
                .andExpect(jsonPath("$.data.page.size").value(2))
                .andExpect(jsonPath("$.data.page.number").value(1))
                .andExpect(jsonPath("$.data.page.totalElements").value(3))
                .andExpect(jsonPath("$.data.page.totalPages").value(2));
    }

    @Test
    void naoListaVeiculoExcluido() throws Exception {
        String id = criarVeiculo(veiculo("Ka", "Ford", 2015, "5000", "JKL3456"));
        mockMvc.perform(comToken(delete("/admin/veiculos/" + id), tokenAdmin())).andExpect(status().isNoContent());

        listar("?marca=Ford").andExpect(jsonPath("$.data.page.totalElements").value(0));
    }

    @Test
    void recusaOrdenacaoPorCampoQueNaoExiste() throws Exception {
        mockMvc.perform(comToken(get("/api/veiculos?sort=cor"), tokenUser())).andExpect(status().isBadRequest());
    }

    private org.springframework.test.web.servlet.ResultActions listar(String filtros) throws Exception {
        return mockMvc.perform(comToken(get("/api/veiculos" + filtros), tokenUser()))
                .andExpect(status().isOk());
    }
}
