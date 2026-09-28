package br.com.luizmatosdev.desafioveiculos.service;

import br.com.luizmatosdev.desafioveiculos.dto.veiculo.*;
import br.com.luizmatosdev.desafioveiculos.entity.Veiculo;
import br.com.luizmatosdev.desafioveiculos.exception.VeiculoJaExistenteException;
import br.com.luizmatosdev.desafioveiculos.exception.VeiculoNaoExistenteException;
import br.com.luizmatosdev.desafioveiculos.mapper.VeiculoMapper;
import br.com.luizmatosdev.desafioveiculos.repository.VeiculoRepository;
import br.com.luizmatosdev.desafioveiculos.specification.VeiculoSpecification;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private static final String INDICE_PLACA_UNICA = "uk_veiculos_placa_ativa";

    private final VeiculoRepository repository;
    private final ValorDolarService valorDolarService;

    /**
     * Busca um veículo pelo seu ID.
     *
     * @param id ID único do veículo
     * @return Dados do veículo com valor convertido para dólar
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     */
    public VeiculoResponseDTO buscar(UUID id) {
        return paraResposta(buscarVeiculo(id));
    }

    /**
     * Lista veículos com paginação e filtros opcionais.
     *
     * @param listarVeiculosDTO Parâmetros de paginação e filtros
     * @return Página com lista de veículos e valores convertidos para dólar
     */
    public Page<VeiculoResponseDTO> listar(ListarVeiculosDTO listarVeiculosDTO) {
        var pageable = PageRequest.of(listarVeiculosDTO.page(), listarVeiculosDTO.size(), listarVeiculosDTO.sort());
        var specification = VeiculoSpecification.filtrar(listarVeiculosDTO);
        var todos = repository.findAll(specification, pageable);
        var valorDolar = valorDolarService.buscarValorAtual();
        return todos.map(veiculo -> VeiculoMapper.toResponseDTO(veiculo, valorDolar));
    }

    /**
     * Cria um novo veículo.
     *
     * @param veiculoDto Dados do veículo a ser criado
     * @return Dados do veículo criado com valor convertido para dólar
     * @throws VeiculoJaExistenteException se já existir um veículo com a mesma placa
     */
    public VeiculoResponseDTO criar(VeiculoRequestDTO veiculoDto) {
        Veiculo veiculo = veiculoDto.toVeiculo();
        validacaoVeiculoPlacaJaExistente(veiculo.getPlaca());

        return paraResposta(salvar(veiculo));
    }

    /**
     * Altera completamente um veículo existente.
     *
     * @param id      ID do veículo a ser alterado
     * @param request Novos dados do veículo
     * @return Dados do veículo alterado com valor convertido para dólar
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     * @throws VeiculoJaExistenteException  se a nova placa já estiver em uso
     */
    public VeiculoResponseDTO alterar(UUID id, VeiculoRequestDTO request) {
        Veiculo veiculo = buscarVeiculo(id);
        validarTrocaDePlaca(veiculo, request.placa());

        veiculo.setVeiculo(request.veiculo());
        veiculo.setMarca(request.marca());
        veiculo.setAno(request.ano());
        veiculo.setDescricao(request.descricao());
        veiculo.setValor(request.valor());
        veiculo.setPlaca(request.placa());
        return paraResposta(salvar(veiculo));
    }

    /**
     * Altera parcialmente um veículo existente, atualizando apenas os campos informados.
     *
     * @param id      ID do veículo a ser alterado
     * @param request Dados parciais do veículo (campos nulos são ignorados)
     * @return Dados do veículo alterado com valor convertido para dólar
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     * @throws VeiculoJaExistenteException  se a nova placa já estiver em uso
     */
    public VeiculoResponseDTO alterarParcialmente(UUID id, AlterarParcialmenteVeiculoRequestDTO request) {
        Veiculo veiculo = buscarVeiculo(id);
        validarTrocaDePlaca(veiculo, request.placa());

        if (request.veiculo() != null) veiculo.setVeiculo(request.veiculo());
        if (request.marca() != null) veiculo.setMarca(request.marca());
        if (request.ano() != null) veiculo.setAno(request.ano());
        if (request.descricao() != null) veiculo.setDescricao(request.descricao());
        if (request.valor() != null) veiculo.setValor(request.valor());
        if (request.placa() != null) veiculo.setPlaca(request.placa());
        return paraResposta(salvar(veiculo));
    }

    /**
     * Remove um veículo do sistema.
     *
     * @param id ID do veículo a ser removido
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     */
    public void deletar(UUID id) {
        repository.delete(buscarVeiculo(id));
    }

    /**
     * Busca a quantidade de veículos agrupados por marca.
     *
     * @return Lista com quantidade de veículos por marca
     */
    public List<QuantidadeVeiculoPorMarcaResponseDTO> buscarQuantidadePorMarca() {
        return repository.contadorQuantidadePorMarca();
    }

    /**
     * A checagem da placa antes de salvar não pega dois cadastros simultâneos com a mesma placa. Nesse
     * caso quem barra é o índice único do banco, e a resposta é a mesma da placa repetida.
     */
    private Veiculo salvar(Veiculo veiculo) {
        try {
            return repository.saveAndFlush(veiculo);
        } catch (DataIntegrityViolationException e) {
            if (violouPlacaUnica(e)) {
                throw new VeiculoJaExistenteException();
            }
            throw e;
        }
    }

    private static boolean violouPlacaUnica(DataIntegrityViolationException e) {
        String mensagem = e.getMostSpecificCause().getMessage();
        return mensagem != null && mensagem.toLowerCase(Locale.ROOT).contains(INDICE_PLACA_UNICA);
    }

    private Veiculo buscarVeiculo(UUID id) {
        return repository.findById(id).orElseThrow(VeiculoNaoExistenteException::new);
    }

    private VeiculoResponseDTO paraResposta(Veiculo veiculo) {
        return VeiculoMapper.toResponseDTO(veiculo, valorDolarService.buscarValorAtual());
    }

    /** Placa ausente (null) ou igual à atual não é troca e não precisa ser conferida. */
    private void validarTrocaDePlaca(Veiculo veiculo, String novaPlaca) {
        if (novaPlaca != null && !veiculo.getPlaca().equals(novaPlaca)) {
            validacaoVeiculoPlacaJaExistente(novaPlaca);
        }
    }

    private void validacaoVeiculoPlacaJaExistente(String placa) {
        if (repository.buscarPorPlaca(placa).isPresent()) {
            throw new VeiculoJaExistenteException();
        }
    }
}
