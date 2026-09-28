package br.com.luizmatosdev.desafioveiculos.controller.admin;

import br.com.luizmatosdev.desafioveiculos.controller.admin.openapi.VeiculoAdminControllerOpenApi;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.AlterarParcialmenteVeiculoRequestDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoRequestDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import br.com.luizmatosdev.desafioveiculos.service.VeiculoService;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/admin/veiculos")
@RequiredArgsConstructor
public class VeiculoAdminController implements VeiculoAdminControllerOpenApi {

    private final VeiculoService service;

    @Override
    public ResponseEntity<ResponseService<VeiculoResponseDTO>> criar(VeiculoRequestDTO veiculo) {
        VeiculoResponseDTO criado = service.criar(veiculo);
        URI endereco = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/veiculos/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(endereco).body(ResponseService.build(criado));
    }

    @Override
    public ResponseEntity<ResponseService<VeiculoResponseDTO>> alterar(UUID id, VeiculoRequestDTO request) {
        return ResponseEntity.ok(ResponseService.build(service.alterar(id, request)));
    }

    @Override
    public ResponseEntity<ResponseService<VeiculoResponseDTO>> atualizarParcialmente(
            UUID id, AlterarParcialmenteVeiculoRequestDTO request) {
        return ResponseEntity.ok(ResponseService.build(service.alterarParcialmente(id, request)));
    }

    @Override
    public ResponseEntity<Void> deletar(UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
