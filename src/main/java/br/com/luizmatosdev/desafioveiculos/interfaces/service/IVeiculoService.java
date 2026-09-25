package br.com.luizmatosdev.desafioveiculos.interfaces.service;

import br.com.luizmatosdev.desafioveiculos.dto.veiculo.*;
import br.com.luizmatosdev.desafioveiculos.exception.VeiculoJaExistenteException;
import br.com.luizmatosdev.desafioveiculos.exception.VeiculoNaoExistenteException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface IVeiculoService {

    /**
     * Busca um veículo pelo seu ID.
     *
     * @param id ID único do veículo
     * @return Dados do veículo com valor convertido para dólar
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     */
    VeiculoResponseDTO buscar(UUID id);

    /**
     * Lista veículos com paginação e filtros opcionais.
     *
     * @param listarVeiculosDTO Parâmetros de paginação e filtros
     * @return Página com lista de veículos e valores convertidos para dólar
     */
    Page<VeiculoResponseDTO> listar(ListarVeiculosDTO listarVeiculosDTO);

    /**
     * Cria um novo veículo.
     *
     * @param veiculoDto Dados do veículo a ser criado
     * @return Dados do veículo criado com valor convertido para dólar
     * @throws VeiculoJaExistenteException se já existir um veículo com a mesma placa
     */
    VeiculoResponseDTO criar(VeiculoRequestDTO veiculoDto);

    /**
     * Altera completamente um veículo existente.
     *
     * @param id      ID do veículo a ser alterado
     * @param request Novos dados do veículo
     * @return Dados do veículo alterado com valor convertido para dólar
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     * @throws VeiculoJaExistenteException  se a nova placa já estiver em uso
     */
    VeiculoResponseDTO alterar(UUID id, VeiculoRequestDTO request);

    /**
     * Altera parcialmente um veículo existente, atualizando apenas os campos informados.
     *
     * @param id      ID do veículo a ser alterado
     * @param request Dados parciais do veículo (campos nulos são ignorados)
     * @return Dados do veículo alterado com valor convertido para dólar
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     * @throws VeiculoJaExistenteException  se a nova placa já estiver em uso
     */
    VeiculoResponseDTO alterarParcialmente(UUID id, AlterarParcialmenteVeiculoRequestDTO request);

    /**
     * Remove um veículo do sistema.
     *
     * @param id ID do veículo a ser removido
     * @throws VeiculoNaoExistenteException se o veículo não for encontrado
     */
    void deletar(UUID id);

    /**
     * Busca a quantidade de veículos agrupados por marca.
     *
     * @return Lista com quantidade de veículos por marca
     */
    List<QuantidadeVeiculoPorMarcaResponseDTO> buscarQuantidadePorMarca();
}
