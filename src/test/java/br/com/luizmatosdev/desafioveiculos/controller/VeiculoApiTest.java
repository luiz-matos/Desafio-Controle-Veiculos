package br.com.luizmatosdev.desafioveiculos.controller;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VeiculoApiTest extends ApiTest {

    @Test
    void cadastraConsultaEExcluiUmVeiculo() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message.codigo").value(0))
                .andExpect(jsonPath("$.data.veiculo").value("Civic"))
                .andExpect(jsonPath("$.data.placa").value("ABC1234"));

        mockMvc.perform(comToken(delete("/admin/veiculos/" + id), tokenAdmin()))
                .andExpect(status().isOk());

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenUser()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message.codigo").value(-2));
    }

    @Test
    void contaOsVeiculosPorMarca() throws Exception {
        criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
        criarVeiculo(veiculo("Fit", "Honda", 2019, "8000", "DEF5678"));
        criarVeiculo(veiculo("Corolla", "Toyota", 2021, "20000", "GHI9012"));

        mockMvc.perform(comToken(get("/api/veiculos/relatorios/por-marca"), tokenUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.marca == 'Honda')].quantidade").value(2))
                .andExpect(jsonPath("$.data[?(@.marca == 'Toyota')].quantidade").value(1));
    }

    @Test
    void permiteCadastrarDeNovoAPlacaDeUmVeiculoExcluido() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
        mockMvc.perform(comToken(delete("/admin/veiculos/" + id), tokenAdmin()))
                .andExpect(status().isOk());

        criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
    }

    @Test
    void mostraOValorCadastradoEmReaisConvertidoParaDolar() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenUser()))
                .andExpect(jsonPath("$.data.valor").value(2000.00));
    }
}
