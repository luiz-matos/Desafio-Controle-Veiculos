package br.com.luizmatosdev.desafioveiculos.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.luizmatosdev.desafioveiculos.config.EconomiaAwesomeApiWsConfig;
import br.com.luizmatosdev.desafioveiculos.config.FrankfurterApiConfig;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Os dois clientes contra um servidor HTTP local, sem chamar as APIs de verdade. */
class ClientesCotacaoTest {

    private HttpServer servidor;
    private int status;
    private String corpo;
    private long demoraMs;

    @BeforeEach
    void subirServidor() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        servidor.createContext("/", troca -> {
            dormir(demoraMs);
            byte[] bytes = corpo.getBytes(StandardCharsets.UTF_8);
            troca.getResponseHeaders().add("Content-Type", "application/json");
            troca.sendResponseHeaders(status, bytes.length);
            troca.getResponseBody().write(bytes);
            troca.close();
        });
        servidor.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
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

    @Test
    void desisteDaApiQueDemoraMaisQueOTimeout() {
        responder(200, "{\"USDBRL\":{\"ask\":\"5.19\"}}");
        demoraMs = 3000;
        var config = new EconomiaAwesomeApiWsConfig();
        config.setUrl(url());
        config.setTimeout(Duration.ofSeconds(1));

        long inicio = System.nanoTime();
        assertNull(new EconomiaAwesomeApiWsClient(config).buscarValorDolarAgora());
        long decorridoMs = Duration.ofNanos(System.nanoTime() - inicio).toMillis();

        assertTrue(decorridoMs < 2500, "esperou " + decorridoMs + " ms");
    }

    @Test
    void usaTimeoutDe30SegundosPorPadrao() {
        assertEquals(Duration.ofSeconds(30), new EconomiaAwesomeApiWsConfig().getTimeout());
        assertEquals(Duration.ofSeconds(30), new FrankfurterApiConfig().getTimeout());
    }

    private static void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
