package br.com.luizmatosdev.desafioveiculos.controller.admin;

import br.com.luizmatosdev.desafioveiculos.controller.admin.openapi.VeiculoAdminControllerApi;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.AlterarParcialmenteVeiculoRequestDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoRequestDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import br.com.luizmatosdev.desafioveiculos.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/veiculos")
@RequiredArgsConstructor
public class VeiculoAdminController implements VeiculoAdminControllerApi {

    private final VeiculoService service;

    @Override
    public ResponseEntity<ResponseService<VeiculoResponseDTO>> criar(VeiculoRequestDTO veiculo) {
        return ResponseEntity.ok(ResponseService.build(service.criar(veiculo)));
    }

    @Override
    public ResponseEntity<ResponseService<VeiculoResponseDTO>> alterar(UUID id, VeiculoRequestDTO request) {
        return ResponseEntity.ok(ResponseService.build(service.alterar(id, request)));
    }

    @Override
    public ResponseEntity<ResponseService<VeiculoResponseDTO>> atualizarParcialmente(UUID id, AlterarParcialmenteVeiculoRequestDTO request) {
        return ResponseEntity.ok(ResponseService.build(service.alterarParcialmente(id, request)));
    }

    @Override
    public ResponseEntity<ResponseService<Void>> deletar(UUID id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
