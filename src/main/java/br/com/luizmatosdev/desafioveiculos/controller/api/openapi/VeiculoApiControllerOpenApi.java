package br.com.luizmatosdev.desafioveiculos.controller.api.openapi;

import br.com.luizmatosdev.desafioveiculos.dto.veiculo.QuantidadeVeiculoPorMarcaResponseDTO;
import br.com.luizmatosdev.desafioveiculos.dto.veiculo.VeiculoResponseDTO;
import br.com.luizmatosdev.desafioveiculos.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Validated
@Tag(name = "Clientes", description = "Gerencia os clientes")
@RequestMapping("/api/veiculos")
public interface VeiculoApiControllerOpenApi {

    @GetMapping
    @Parameter(
            in = ParameterIn.QUERY,
            name = "page",
            description = "Número da página",
            schema = @Schema(type = "integer", defaultValue = "0")
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "size",
            description = "Quantidade de elementos por página",
            schema = @Schema(type = "integer", defaultValue = "10")
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "sort",
            description = "Ordenação dos resultados. Exemplo: sort=marca,asc",
            schema = @Schema(type = "string")
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "marca",
            description = "Filtra pela marca do veículo",
            schema = @Schema(type = "string")
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "ano",
            description = "Filtra pelo ano do veículo",
            schema = @Schema(type = "number")
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "minPreco",
            description = "Valor mínimo em reais, como foi cadastrado",
            schema = @Schema(type = "number")
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "maxPreco",
            description = "Valor máximo em reais, como foi cadastrado",
            schema = @Schema(type = "number")
    )
    @Operation(summary = "Lista os veículos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    ResponseEntity<Page<VeiculoResponseDTO>> listar(
            @Parameter(hidden = true) @PageableDefault Pageable pageable,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) BigDecimal minPreco,
            @RequestParam(required = false) BigDecimal maxPreco
    );

    @GetMapping("{id}")
    @Operation(summary = "Consulta um veículo", responses = {
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
    ResponseEntity<ResponseService<VeiculoResponseDTO>> buscar(@PathVariable UUID id);

    @GetMapping("/relatorios/por-marca")
    @Operation(summary = "Relatório de quantidade de veículos agrupados por marca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    ResponseEntity<ResponseService<List<QuantidadeVeiculoPorMarcaResponseDTO>>> relatorioVeiculosPorMarca();
}
