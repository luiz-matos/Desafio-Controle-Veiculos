package br.com.luizmatosdev.desafioveiculos.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import br.com.luizmatosdev.desafioveiculos.exception.ErroWsException;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class VeiculoApiTest extends ApiTest {

    @Test
    void cadastraConsultaEExcluiUmVeiculo() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message.codigo").value(0))
                .andExpect(jsonPath("$.data.veiculo").value("Civic"))
                .andExpect(jsonPath("$.data.placa").value("ABC1234"));

        mockMvc.perform(comToken(delete("/admin/veiculos/" + id), tokenAdmin())).andExpect(status().isNoContent());

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
        mockMvc.perform(comToken(delete("/admin/veiculos/" + id), tokenAdmin())).andExpect(status().isNoContent());

        criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
    }

    @Test
    void mostraOValorCadastradoEmReaisConvertidoParaDolar() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenUser()))
                .andExpect(jsonPath("$.data.valor").value(2000.00));
    }

    @Test
    void listaNormalmenteUmVeiculoGravadoSemValor() throws Exception {
        criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
        // O valor passou a ser obrigatório, mas o banco ainda pode ter veículos gravados antes sem ele
        String semValor = "6f1c7e1a-0000-4000-8000-000000000001";
        jdbcTemplate.update(
                "INSERT INTO veiculos (id, veiculo, marca, ano, placa, deletado, created, updated)"
                        + " VALUES (CAST(? AS UUID), 'Uno', 'Fiat', 2010, 'SEM0001', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                semValor);

        mockMvc.perform(comToken(get("/api/veiculos/" + semValor), tokenUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valor").value(nullValue()));

        mockMvc.perform(comToken(get("/api/veiculos"), tokenUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }

    @Test
    void responde503QuandoNenhumaApiDeCotacaoResponde() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));
        when(valorDolarService.buscarValorAtual()).thenThrow(new ErroWsException());

        mockMvc.perform(comToken(get("/api/veiculos/" + id), tokenUser()))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message.codigo").value(-80));
    }

    @Test
    void respondeOCadastroCom201EOEnderecoDoVeiculo() throws Exception {
        String resposta = mockMvc.perform(comToken(post("/admin/veiculos"), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculo("Civic", "Honda", 2020, "10000", "ABC1234")))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(comToken(get(URI.create(resposta).getPath()), tokenUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.placa").value("ABC1234"));
    }

    @Test
    void respondeAExclusaoCom204SemCorpo() throws Exception {
        String id = criarVeiculo(veiculo("Civic", "Honda", 2020, "10000", "ABC1234"));

        mockMvc.perform(comToken(delete("/admin/veiculos/" + id), tokenAdmin()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
