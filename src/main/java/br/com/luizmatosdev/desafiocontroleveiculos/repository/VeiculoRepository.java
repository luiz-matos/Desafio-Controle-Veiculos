package br.com.luizmatosdev.desafiocontroleveiculos.repository;

import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.QuantidadeVeiculoPorMarcaResponseDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.entity.Veiculo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends BaseRepository<Veiculo, UUID> {
    @Query("SELECT v FROM Veiculo v WHERE v.placa = :placa AND v.deletado = false")
    Optional<Veiculo> buscarPorPlaca(@Param("placa") String placa);

    @Query("""
        SELECT new br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.QuantidadeVeiculoPorMarcaResponseDTO(COUNT(v), v.marca)
        FROM Veiculo v
        WHERE v.deletado = false
        GROUP BY v.marca
    """)
    List<QuantidadeVeiculoPorMarcaResponseDTO> contadorQuantidadePorMarca();
}
