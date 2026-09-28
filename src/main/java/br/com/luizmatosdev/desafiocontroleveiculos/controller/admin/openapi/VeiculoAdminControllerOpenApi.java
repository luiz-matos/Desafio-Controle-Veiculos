package br.com.luizmatosdev.desafiocontroleveiculos.controller.admin.openapi;

import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.AlterarParcialmenteVeiculoRequestDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.VeiculoRequestDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.handler.CampoInvalidoResponse;
import br.com.luizmatosdev.desafiocontroleveiculos.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Administração de veículos", description = "Cadastro, alteração e exclusão, só para ADMIN")
public interface VeiculoAdminControllerOpenApi {

    @PostMapping
    @Operation(summary = "Cadastra um veículo")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Cadastrado; o cabeçalho Location traz o endereço do veículo"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Campo inválido ou obrigatório",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CampoInvalidoResponse.class))
                        }),
                @ApiResponse(
                        responseCode = "422",
                        description = "Placa já cadastrada em outro veículo",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                "{\"message\": {\"codigo\": -1,\"descricao\":\"Placa do veículo já existe\"}}")))
            })
    ResponseEntity<ResponseService<VeiculoResponseDTO>> criar(@Valid @RequestBody VeiculoRequestDTO veiculo);

    @PutMapping("{id}")
    @Operation(summary = "Altera os dados de um veículo")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Campo inválido ou obrigatório",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CampoInvalidoResponse.class))
                        }),
                @ApiResponse(
                        responseCode = "422",
                        description = "Placa já cadastrada em outro veículo",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                "{\"message\": {\"codigo\": -1,\"descricao\":\"Placa do veículo já existe\"}}"))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Veículo não encontrado",
                        content =
                                @Content(
                                        examples = {
                                            @ExampleObject(
                                                    value =
                                                            "{\"message\": {\"codigo\": -2,\"descricao\":\"Veículo não encontrado\"}}")
                                        }))
            })
    ResponseEntity<ResponseService<VeiculoResponseDTO>> alterar(
            @PathVariable UUID id, @Valid @RequestBody VeiculoRequestDTO request);

    @PatchMapping("{id}")
    @Operation(summary = "Atualiza parcialmente um veículo")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Campo inválido ou obrigatório",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CampoInvalidoResponse.class))
                        }),
                @ApiResponse(
                        responseCode = "422",
                        description = "Placa já cadastrada em outro veículo",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value =
                                                                "{\"message\": {\"codigo\": -1,\"descricao\":\"Placa do veículo já existe\"}}"))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Veículo não encontrado",
                        content =
                                @Content(
                                        examples = {
                                            @ExampleObject(
                                                    value =
                                                            "{\"message\": {\"codigo\": -2,\"descricao\":\"Veículo não encontrado\"}}")
                                        }))
            })
    ResponseEntity<ResponseService<VeiculoResponseDTO>> atualizarParcialmente(
            @PathVariable UUID id, @Valid @RequestBody AlterarParcialmenteVeiculoRequestDTO request);

    @DeleteMapping("{id}")
    @Operation(summary = "Deleta um veículo")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Excluído"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Veículo não encontrado",
                        content =
                                @Content(
                                        examples = {
                                            @ExampleObject(
                                                    value =
                                                            "{\"message\": {\"codigo\": -2,\"descricao\":\"Veículo não encontrado\"}}")
                                        }))
            })
    ResponseEntity<Void> deletar(@PathVariable UUID id);
}
