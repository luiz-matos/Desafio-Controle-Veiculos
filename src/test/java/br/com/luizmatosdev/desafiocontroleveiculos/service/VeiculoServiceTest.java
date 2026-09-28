package br.com.luizmatosdev.desafiocontroleveiculos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.AlterarParcialmenteVeiculoRequestDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.ListarVeiculosDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.QuantidadeVeiculoPorMarcaResponseDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.VeiculoRequestDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.entity.DadosVeiculo;
import br.com.luizmatosdev.desafiocontroleveiculos.entity.Veiculo;
import br.com.luizmatosdev.desafiocontroleveiculos.repository.VeiculoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository repository;

    @Mock
    private ValorDolarService valorDolarService;

    @InjectMocks
    private VeiculoService veiculoService;

    @Test
    void buscar_DeveRetornarVeiculoComValorEmDolar_QuandoVeiculoExistir() {
        // Arrange
        UUID id = UUID.randomUUID();
        Veiculo veiculo = veiculo(id, "Civic", "Honda", 2020, "Sedan", new BigDecimal("10000.00"), "ABC1234");

        BigDecimal valorDolar = new BigDecimal("5.50");

        when(repository.findById(id)).thenReturn(Optional.of(veiculo));
        when(valorDolarService.buscarValorAtual()).thenReturn(valorDolar);

        // Act
        VeiculoResponseDTO resultado = veiculoService.buscar(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Civic", resultado.veiculo());
        assertEquals("Honda", resultado.marca());
        assertEquals(2020, resultado.ano());
        assertEquals("Sedan", resultado.descricao());
        assertEquals(new BigDecimal("1818.18"), resultado.valor());
        assertEquals("ABC1234", resultado.placa());

        verify(repository).findById(id);
        verify(valorDolarService).buscarValorAtual();
    }

    @Test
    void listar_DeveRetornarPaginaDeVeiculos_QuandoChamado() {
        // Arrange
        ListarVeiculosDTO filtros = new ListarVeiculosDTO(0, 10, Sort.unsorted(), null, null, null, null);

        Veiculo veiculo1 =
                veiculo(UUID.randomUUID(), "Civic", "Honda", 2020, null, new BigDecimal("80000.00"), "ABC1234");

        Veiculo veiculo2 =
                veiculo(UUID.randomUUID(), "Corolla", "Toyota", 2021, null, new BigDecimal("90000.00"), "XYZ5678");

        List<Veiculo> veiculos = List.of(veiculo1, veiculo2);
        Page<Veiculo> pageVeiculos = new PageImpl<>(veiculos, PageRequest.of(0, 10), 2);
        BigDecimal valorDolar = new BigDecimal("5.50");

        when(repository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(pageVeiculos);
        when(valorDolarService.buscarValorAtual()).thenReturn(valorDolar);

        // Act
        Page<VeiculoResponseDTO> resultado = veiculoService.listar(filtros);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals("Civic", resultado.getContent().get(0).veiculo());
        assertEquals("Corolla", resultado.getContent().get(1).veiculo());

        verify(repository).findAll(any(Specification.class), any(PageRequest.class));
        verify(valorDolarService).buscarValorAtual();
    }

    @Test
    void criarDeveCriarVeiculoQuandoPlacaNaoExistir() {
        // Arrange
        VeiculoRequestDTO request =
                new VeiculoRequestDTO("Civic", "Honda", 2020, "Sedan", new BigDecimal("80000.00"), "ABC1234");

        Veiculo veiculoSalvo =
                veiculo(UUID.randomUUID(), "Civic", "Honda", 2020, "Sedan", new BigDecimal("10000.00"), "ABC1234");

        BigDecimal valorDolar = new BigDecimal("5.50");

        when(repository.buscarPorPlaca("ABC1234")).thenReturn(Optional.empty());
        when(repository.saveAndFlush(any(Veiculo.class))).thenReturn(veiculoSalvo);
        when(valorDolarService.buscarValorAtual()).thenReturn(valorDolar);

        // Act
        VeiculoResponseDTO resultado = veiculoService.criar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Civic", resultado.veiculo());
        assertEquals("Honda", resultado.marca());
        assertEquals(2020, resultado.ano());
        assertEquals("Sedan", resultado.descricao());
        assertEquals(new BigDecimal("1818.18"), resultado.valor());
        assertEquals("ABC1234", resultado.placa());

        verify(repository).buscarPorPlaca("ABC1234");
        verify(repository).saveAndFlush(any(Veiculo.class));
        verify(valorDolarService).buscarValorAtual();
    }

    @Test
    void alterarDeveAlterarVeiculoQuandoVeiculoExistirEPlacaNaoConflitar() {
        // Arrange
        UUID id = UUID.randomUUID();
        VeiculoRequestDTO request = new VeiculoRequestDTO(
                "Corolla", "Toyota", 2021, "Sedan híbrido", new BigDecimal("90000.00"), "XYZ5678");

        Veiculo veiculoExistente = veiculo(id, "Civic", "Honda", 2020, "Sedan", new BigDecimal("10000.00"), "ABC1234");

        Veiculo veiculoAlterado =
                veiculo(id, "Corolla", "Toyota", 2021, "Sedan híbrido", new BigDecimal("50000.00"), "XYZ5678");

        BigDecimal valorDolar = new BigDecimal("5.50");

        when(repository.findById(id)).thenReturn(Optional.of(veiculoExistente));
        when(repository.buscarPorPlaca("XYZ5678")).thenReturn(Optional.empty());
        when(repository.saveAndFlush(any(Veiculo.class))).thenReturn(veiculoAlterado);
        when(valorDolarService.buscarValorAtual()).thenReturn(valorDolar);

        // Act
        VeiculoResponseDTO resultado = veiculoService.alterar(id, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Corolla", resultado.veiculo());
        assertEquals("Toyota", resultado.marca());
        assertEquals(2021, resultado.ano());
        assertEquals("Sedan híbrido", resultado.descricao());
        assertEquals(new BigDecimal("9090.91"), resultado.valor());
        assertEquals("XYZ5678", resultado.placa());

        verify(repository).findById(id);
        verify(repository).buscarPorPlaca("XYZ5678");
        verify(repository).saveAndFlush(any(Veiculo.class));
        verify(valorDolarService).buscarValorAtual();
    }

    @Test
    void alterarParcialmenteDeveAlterarApenasValorEDescricaoQuandoCamposInformados() {
        // Arrange
        UUID id = UUID.randomUUID();
        AlterarParcialmenteVeiculoRequestDTO request = new AlterarParcialmenteVeiculoRequestDTO(
                null, // veiculo - não altera
                null, // marca - não altera
                null, // ano - não altera
                "Sedan com ar condicionado", // descrição - altera
                new BigDecimal("85000.00"), // valor - altera
                null // placa - não altera
                );

        Veiculo veiculoExistente = veiculo(id, "Civic", "Honda", 2020, "Sedan", new BigDecimal("80000.00"), "ABC1234");

        Veiculo veiculoAlterado =
                veiculo(id, "Civic", "Honda", 2020, "Sedan com ar condicionado", new BigDecimal("85000.00"), "ABC1234");

        BigDecimal valorDolar = new BigDecimal("5.50");

        when(repository.findById(id)).thenReturn(Optional.of(veiculoExistente));
        when(repository.saveAndFlush(any(Veiculo.class))).thenReturn(veiculoAlterado);
        when(valorDolarService.buscarValorAtual()).thenReturn(valorDolar);

        // Act
        VeiculoResponseDTO resultado = veiculoService.alterarParcialmente(id, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Civic", resultado.veiculo()); // não alterado
        assertEquals("Honda", resultado.marca()); // não alterado
        assertEquals(2020, resultado.ano()); // não alterado
        assertEquals("Sedan com ar condicionado", resultado.descricao()); // alterado
        assertEquals(new BigDecimal("15454.55"), resultado.valor()); // alterado
        assertEquals("ABC1234", resultado.placa()); // não alterado

        verify(repository).findById(id);
        verify(repository).saveAndFlush(any(Veiculo.class));
        verify(valorDolarService).buscarValorAtual();
        verify(repository, never()).buscarPorPlaca(anyString()); // não deve validar placa pois não foi alterada
    }

    @Test
    void deletarDeveDeletarVeiculoQuandoVeiculoExistir() {
        // Arrange
        UUID id = UUID.randomUUID();
        Veiculo veiculo = veiculo(id, "Civic", "Honda", 2020, "Sedan", new BigDecimal("80000.00"), "ABC1234");

        when(repository.findById(id)).thenReturn(Optional.of(veiculo));

        // Act
        veiculoService.deletar(id);

        // Assert
        verify(repository).findById(id);
        verify(repository).delete(veiculo);
    }

    @Test
    void buscarQuantidadePorMarcaDeveRetornarEstatisticasPorMarcaQuandoChamado() {
        // Arrange
        List<QuantidadeVeiculoPorMarcaResponseDTO> estatisticas = List.of(
                new QuantidadeVeiculoPorMarcaResponseDTO(5L, "Honda"),
                new QuantidadeVeiculoPorMarcaResponseDTO(3L, "Toyota"),
                new QuantidadeVeiculoPorMarcaResponseDTO(2L, "Ford"));

        when(repository.contadorQuantidadePorMarca()).thenReturn(estatisticas);

        // Act
        List<QuantidadeVeiculoPorMarcaResponseDTO> resultado = veiculoService.buscarQuantidadePorMarca();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(5L, resultado.get(0).quantidade());
        assertEquals("Honda", resultado.get(0).marca());
        assertEquals(3L, resultado.get(1).quantidade());
        assertEquals("Toyota", resultado.get(1).marca());
        assertEquals(2L, resultado.get(2).quantidade());
        assertEquals("Ford", resultado.get(2).marca());

        verify(repository).contadorQuantidadePorMarca();
    }

    private static Veiculo veiculo(
            UUID id, String modelo, String marca, Integer ano, String descricao, BigDecimal valor, String placa) {
        Veiculo veiculo = Veiculo.cadastrar(new DadosVeiculo(modelo, marca, ano, descricao, valor, placa));
        ReflectionTestUtils.setField(veiculo, "id", id);
        return veiculo;
    }
}
