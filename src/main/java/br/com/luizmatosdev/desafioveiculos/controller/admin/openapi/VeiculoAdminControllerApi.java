package br.com.luizmatosdev.desafioveiculos.controller.admin.openapi;

import br.com.luizmatosdev.desafioveiculos.dto.veiculo.AlterarParcialmenteVeiculoRequestDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoRequestDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafioveiculos.handler.CampoInvalidoResponse;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface VeiculoAdminControllerApi {

    @PostMapping
    @Operation(summary = "Cadastra um veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "422",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CampoInvalidoResponse.class))}
            )
    })
    ResponseEntity<ResponseService<VeiculoResponseDTO>> criar(@Valid @RequestBody VeiculoRequestDTO veiculo);

    @PutMapping("{id}")
    @Operation(summary = "Altera os dados de um veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "422",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CampoInvalidoResponse.class))}
            ),
            @ApiResponse(responseCode = "404",
                    description = "Veículo não encontrado",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{\"message\": {\"codigo\": -2,\"descricao\":\"Veículo não encontrado\"}}"
                                    )
                            }
                    )
            )
    })
    ResponseEntity<ResponseService<VeiculoResponseDTO>> alterar(@PathVariable UUID id, @Valid @RequestBody VeiculoRequestDTO request);

    @PatchMapping("{id}")
    @Operation(summary = "Atualiza parcialmente um veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "422",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CampoInvalidoResponse.class))}
            ),
            @ApiResponse(responseCode = "404",
                    description = "Veículo não encontrado",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{\"message\": {\"codigo\": -2,\"descricao\":\"Veículo não encontrado\"}}"
                                    )
                            }
                    )
            )
    })
    ResponseEntity<ResponseService<VeiculoResponseDTO>> atualizarParcialmente(@PathVariable UUID id, @Valid @RequestBody AlterarParcialmenteVeiculoRequestDTO request);

    @DeleteMapping("{id}")
    @Operation(summary = "Deleta um veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "Veículo não encontrado",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{\"message\": {\"codigo\": -2,\"descricao\":\"Veículo não encontrado\"}}"
                                    )
                            }
                    )
            )
    })
    ResponseEntity<ResponseService<Void>> deletar(@PathVariable UUID id);
}
