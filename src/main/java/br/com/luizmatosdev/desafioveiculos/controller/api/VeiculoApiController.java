package br.com.luizmatosdev.desafioveiculos.controller.api;

import br.com.luizmatosdev.desafioveiculos.controller.api.openapi.VeiculoApiControllerOpenApi;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.ListarVeiculosDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.QuantidadeVeiculoPorMarcaResponseDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import br.com.luizmatosdev.desafioveiculos.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VeiculoApiController implements VeiculoApiControllerOpenApi {

    private final VeiculoService veiculoService;

    @Override
    public ResponseEntity<Page<VeiculoResponseDTO>> listar(
        Pageable pageable,
        String marca,
        Integer ano,
        String cor,
        BigDecimal minPreco,
        BigDecimal maxPreco
    ) {
        ListarVeiculosDTO listarVeiculosDTO = new ListarVeiculosDTO(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort(),
            marca,
            ano,
            cor,
            minPreco,
            maxPreco
        );
        return ResponseEntity.ok(veiculoService.listar(listarVeiculosDTO));
    }

    @Override
    public ResponseEntity<ResponseService<VeiculoResponseDTO>> buscar(UUID id) {
        return ResponseEntity.ok(ResponseService.build(veiculoService.buscar(id)));
    }

    @Override
    public ResponseEntity<ResponseService<List<QuantidadeVeiculoPorMarcaResponseDTO>>> relatorioVeiculosPorMarca() {
        return ResponseEntity.ok(ResponseService.build(veiculoService.buscarQuantidadePorMarca()));
    }
}
