package br.com.luizmatosdev.desafioveiculos.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import br.com.luizmatosdev.desafioveiculos.ApiTest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class PlacaUnicaTest extends ApiTest {

    private static final int CADASTROS = 10;

    @Test
    void aceitaUmSoCadastroQuandoAMesmaPlacaChegaAoMesmoTempo() throws Exception {
        String token = tokenAdmin();
        CountDownLatch largada = new CountDownLatch(1);
        List<Future<Integer>> respostas = new ArrayList<>();

        ExecutorService threads = Executors.newFixedThreadPool(CADASTROS);
        try {
            for (int i = 0; i < CADASTROS; i++) {
                respostas.add(threads.submit(() -> {
                    largada.await();
                    return mockMvc.perform(comToken(post("/admin/veiculos"), token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(veiculo("Civic", "Honda", 2020, "10000", "ABC1234")))
                            .andReturn()
                            .getResponse()
                            .getStatus();
                }));
            }
            largada.countDown();

            List<Integer> status = new ArrayList<>();
            for (Future<Integer> resposta : respostas) {
                status.add(resposta.get());
            }
            assertEquals(1, status.stream().filter(s -> s == 200).count(), status.toString());
            assertEquals(CADASTROS - 1, status.stream().filter(s -> s == 422).count(), status.toString());
        } finally {
            threads.shutdown();
        }

        Integer gravados = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM veiculos WHERE placa = 'ABC1234' AND deletado = false", Integer.class);
        assertEquals(1, gravados);
    }
}
