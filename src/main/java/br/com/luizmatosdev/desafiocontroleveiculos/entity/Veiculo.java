package br.com.luizmatosdev.desafiocontroleveiculos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "veiculos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String veiculo;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false)
    private Integer ano;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Boolean deletado = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updated;

    @Column(precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 8)
    private String placa;

    public static Veiculo cadastrar(DadosVeiculo dados) {
        Veiculo novo = new Veiculo();
        novo.alterar(dados);
        return novo;
    }

    /** Troca todos os campos, como no PUT. */
    public void alterar(DadosVeiculo dados) {
        this.veiculo = dados.veiculo();
        this.marca = dados.marca();
        this.ano = dados.ano();
        this.descricao = dados.descricao();
        this.valor = dados.valor();
        this.placa = dados.placa();
    }

    /** Troca só os campos informados, como no PATCH: campo nulo mantém o valor atual. */
    public void alterarParcialmente(DadosVeiculo dados) {
        if (dados.veiculo() != null) this.veiculo = dados.veiculo();
        if (dados.marca() != null) this.marca = dados.marca();
        if (dados.ano() != null) this.ano = dados.ano();
        if (dados.descricao() != null) this.descricao = dados.descricao();
        if (dados.valor() != null) this.valor = dados.valor();
        if (dados.placa() != null) this.placa = dados.placa();
    }

    /** Placa ausente (null) ou igual à atual não é troca. */
    public boolean trocaDePlacaPara(String novaPlaca) {
        return novaPlaca != null && !placa.equals(novaPlaca);
    }
}
