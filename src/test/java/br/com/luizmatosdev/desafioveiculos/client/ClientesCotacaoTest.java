package br.com.luizmatosdev.desafioveiculos.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.luizmatosdev.desafioveiculos.config.EconomiaAwesomeApiWsConfig;
import br.com.luizmatosdev.desafioveiculos.config.FrankfurterApiConfig;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Os dois clientes contra um servidor HTTP local, sem chamar as APIs de verdade. */
class ClientesCotacaoTest {

    private HttpServer servidor;
    private int status;
    private String corpo;

    @BeforeEach
    void subirServidor() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        servidor.createContext("/", troca -> {
            byte[] bytes = corpo.getBytes(StandardCharsets.UTF_8);
            troca.getResponseHeaders().add("Content-Type", "application/json");
            troca.sendResponseHeaders(status, bytes.length);
            troca.getResponseBody().write(bytes);
            troca.close();
        });
        servidor.start();
    }

    @AfterEach
    void pararServidor() {
        servidor.stop(0);
    }

    @Test
    void leACotacaoDaAwesomeApi() {
        responder(200, "{\"USDBRL\":{\"code\":\"USD\",\"ask\":\"5.1904\"}}");

        assertEquals(new BigDecimal("5.1904"), awesome(url()).buscarValorDolarAgora());
    }

    @Test
    void leACotacaoDaFrankfurter() {
        responder(200, "{\"amount\":1.0,\"base\":\"USD\",\"date\":\"2026-09-25\",\"rates\":{\"BRL\":5.1821}}");

        assertEquals(new BigDecimal("5.1821"), frankfurter(url()).buscarValorDolarAgora());
    }

    @Test
    void devolveNullQuandoAApiRespondeErro() {
        responder(500, "{}");

        assertNull(awesome(url()).buscarValorDolarAgora());
        assertNull(frankfurter(url()).buscarValorDolarAgora());
    }

    @Test
    void devolveNullQuandoARespostaNaoEJson() {
        responder(200, "isto não é json");

        assertNull(awesome(url()).buscarValorDolarAgora());
        assertNull(frankfurter(url()).buscarValorDolarAgora());
    }

    @Test
    void devolveNullQuandoARespostaNaoTemACotacao() {
        responder(200, "{}");

        assertNull(awesome(url()).buscarValorDolarAgora());
        assertNull(frankfurter(url()).buscarValorDolarAgora());
    }

    @Test
    void devolveNullQuandoAApiNaoAtende() {
        String portaFechada = url();
        servidor.stop(0);

        assertNull(awesome(portaFechada).buscarValorDolarAgora());
        assertNull(frankfurter(portaFechada).buscarValorDolarAgora());
    }

    private void responder(int status, String corpo) {
        this.status = status;
        this.corpo = corpo;
    }

    private String url() {
        return "http://localhost:" + servidor.getAddress().getPort() + "/cotacao";
    }

    private EconomiaAwesomeApiWsClient awesome(String url) {
        var config = new EconomiaAwesomeApiWsConfig();
        config.setUrl(url);
        return new EconomiaAwesomeApiWsClient(config);
    }

    private FrankfurterApiClient frankfurter(String url) {
        var config = new FrankfurterApiConfig();
        config.setUrl(url);
        return new FrankfurterApiClient(config);
    }
}
