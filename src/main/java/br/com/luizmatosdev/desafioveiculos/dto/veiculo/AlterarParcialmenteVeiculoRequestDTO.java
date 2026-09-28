package br.com.luizmatosdev.desafioveiculos.dto.veiculo;

import br.com.luizmatosdev.desafioveiculos.entity.DadosVeiculo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlterarParcialmenteVeiculoRequestDTO(
        @Pattern(regexp = PREENCHIDO, message = NAO_PODE_FICAR_EM_BRANCO) @Size(max = 100)
        String veiculo,

        @Pattern(regexp = PREENCHIDO, message = NAO_PODE_FICAR_EM_BRANCO) @Size(max = 100)
        String marca,

        Integer ano,

        String descricao,

        @PositiveOrZero BigDecimal valor,

        @Pattern(regexp = PREENCHIDO, message = NAO_PODE_FICAR_EM_BRANCO) @Size(max = 8)
        String placa) {
    // Campo ausente (null) mantém o valor atual; campo enviado não pode ser só espaços
    private static final String PREENCHIDO = "(?s).*\\S.*";
    private static final String NAO_PODE_FICAR_EM_BRANCO = "não deve estar em branco";

    public DadosVeiculo toDados() {
        return new DadosVeiculo(veiculo, marca, ano, descricao, valor, placa);
    }
}
