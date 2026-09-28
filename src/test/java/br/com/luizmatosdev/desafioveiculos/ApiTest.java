package br.com.luizmatosdev.desafioveiculos;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizmatosdev.desafioveiculos.service.ValorDolarService;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Sobe a aplicação inteira com H2 e a cotação do dólar fixa em 5,00, sem Redis nem API externa.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class ApiTest {

    protected static final BigDecimal COTACAO_DOLAR = new BigDecimal("5.00");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @MockitoBean
    protected ValorDolarService valorDolarService;

    @BeforeEach
    void prepararCenario() {
        jdbcTemplate.update("DELETE FROM veiculos");
        when(valorDolarService.buscarValorAtual()).thenReturn(COTACAO_DOLAR);
    }

    protected String tokenAdmin() throws Exception {
        return login("admin", "admin123");
    }

    protected String tokenUser() throws Exception {
        return login("user", "user123");
    }

    private String login(String username, String password) throws Exception {
        String resposta = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"%s\",\"password\":\"%s\"}".formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return JsonPath.read(resposta, "$.data.token");
    }

    protected MockHttpServletRequestBuilder comToken(MockHttpServletRequestBuilder request, String token) {
        return request.header("Authorization", "Bearer " + token);
    }

    protected String criarVeiculo(String json) throws Exception {
        String resposta = mockMvc.perform(comToken(post("/admin/veiculos"), tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return JsonPath.read(resposta, "$.data.id");
    }

    protected String veiculo(String modelo, String marca, int ano, String valor, String placa) {
        return """
                {"veiculo":"%s","marca":"%s","ano":%d,"valor":%s,"placa":"%s"}
                """.formatted(modelo, marca, ano, valor, placa);
    }
}
