package br.com.luizmatosdev.desafioveiculos.service;

import br.com.luizmatosdev.desafioveiculos.dto.veiculo.*;
import br.com.luizmatosdev.desafioveiculos.entity.Veiculo;
import br.com.luizmatosdev.desafioveiculos.exception.VeiculoJaExistenteException;
import br.com.luizmatosdev.desafioveiculos.exception.VeiculoNaoExistenteException;
import br.com.luizmatosdev.desafioveiculos.interfaces.service.IValorDolarService;
import br.com.luizmatosdev.desafioveiculos.interfaces.service.IVeiculoService;
import br.com.luizmatosdev.desafioveiculos.mapper.VeiculoMapper;
import br.com.luizmatosdev.desafioveiculos.repository.VeiculoRepository;
import br.com.luizmatosdev.desafioveiculos.specification.VeiculoSpecification;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeiculoService implements IVeiculoService {

    private final VeiculoRepository repository;
    private final IValorDolarService valorDolarService;

    @Override
    public VeiculoResponseDTO buscar(UUID id) {
        Veiculo veiculo = repository.findById(id).orElseThrow(VeiculoNaoExistenteException::new);
        var valorDolar = valorDolarService.buscarValorAtual();
        return VeiculoMapper.toResponseDTO(veiculo, valorDolar);
    }

    @Override
    public Page<VeiculoResponseDTO> listar(ListarVeiculosDTO listarVeiculosDTO) {
        var pageable = PageRequest.of(listarVeiculosDTO.page(), listarVeiculosDTO.size(), listarVeiculosDTO.sort());
        var specification = VeiculoSpecification.filtrar(listarVeiculosDTO);
        var todos = repository.findAll(specification, pageable);
        var valorDolar = valorDolarService.buscarValorAtual();
        return todos.map(veiculo -> VeiculoMapper.toResponseDTO(veiculo, valorDolar));
    }

    @Override
    public VeiculoResponseDTO criar(VeiculoRequestDTO veiculoDto) {
        Veiculo veiculo = veiculoDto.toVeiculo();
        validacaoVeiculoPlacaJaExistente(veiculo.getPlaca());

        Veiculo salvo = repository.save(veiculo);
        var valorDolar = valorDolarService.buscarValorAtual();
        return VeiculoMapper.toResponseDTO(salvo, valorDolar);
    }

    private void validacaoVeiculoPlacaJaExistente(String placa) {
        Veiculo veiculoExistente = repository.buscarPorPlaca(placa).orElse(null);

        if (veiculoExistente != null) {
            throw new VeiculoJaExistenteException();
        }
    }

    @Override
    public VeiculoResponseDTO alterar(UUID id, VeiculoRequestDTO request) {
        Veiculo veiculo = repository.findById(id).orElseThrow(VeiculoNaoExistenteException::new);
        if (!veiculo.getPlaca().equals(request.placa())) {
            validacaoVeiculoPlacaJaExistente(request.placa());
        }
        veiculo.setVeiculo(request.veiculo());
        veiculo.setMarca(request.marca());
        veiculo.setAno(request.ano());
        veiculo.setDescricao(request.descricao());
        veiculo.setValor(request.valor());
        veiculo.setPlaca(request.placa());
        Veiculo salvo = repository.save(veiculo);
        var valorDolar = valorDolarService.buscarValorAtual();
        return VeiculoMapper.toResponseDTO(salvo, valorDolar);
    }

    @Override
    public VeiculoResponseDTO alterarParcialmente(UUID id, AlterarParcialmenteVeiculoRequestDTO request) {
        Veiculo veiculo = repository.findById(id).orElseThrow(VeiculoNaoExistenteException::new);
        if (request.placa() != null && !veiculo.getPlaca().equals(request.placa())) {
            validacaoVeiculoPlacaJaExistente(request.placa());
        }

        if (request.veiculo() != null) veiculo.setVeiculo(request.veiculo());
        if (request.marca() != null) veiculo.setMarca(request.marca());
        if (request.ano() != null) veiculo.setAno(request.ano());
        if (request.descricao() != null) veiculo.setDescricao(request.descricao());
        if (request.valor() != null) veiculo.setValor(request.valor());
        if (request.placa() != null) veiculo.setPlaca(request.placa());
        Veiculo salvo = repository.save(veiculo);
        var valorDolar = valorDolarService.buscarValorAtual();
        return VeiculoMapper.toResponseDTO(salvo, valorDolar);
    }

    @Override
    public void deletar(UUID id) {
        Veiculo veiculo = repository.findById(id).orElseThrow(VeiculoNaoExistenteException::new);
        repository.delete(veiculo);
    }

    @Override
    public List<QuantidadeVeiculoPorMarcaResponseDTO> buscarQuantidadePorMarca() {
        return repository.contadorQuantidadePorMarca();
    }
}
