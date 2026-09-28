package br.com.luizmatosdev.desafiocontroleveiculos.specification;

import br.com.luizmatosdev.desafiocontroleveiculos.dto.veiculo.ListarVeiculosDTO;
import br.com.luizmatosdev.desafiocontroleveiculos.entity.Veiculo;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class VeiculoSpecification {

    private VeiculoSpecification() {
        throw new UnsupportedOperationException("Classe não instanciada");
    }

    public static Specification<Veiculo> filtrar(ListarVeiculosDTO listarVeiculosDTO) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isFalse(root.get("deletado")));

            if (Objects.nonNull(listarVeiculosDTO.marca())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("marca")),
                        "%" + listarVeiculosDTO.marca().toLowerCase(Locale.ROOT) + "%"));
            }

            if (Objects.nonNull(listarVeiculosDTO.ano())) {
                predicates.add(criteriaBuilder.equal(root.get("ano"), listarVeiculosDTO.ano()));
            }

            if (Objects.nonNull(listarVeiculosDTO.minPreco())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("valor"), listarVeiculosDTO.minPreco()));
            }

            if (Objects.nonNull(listarVeiculosDTO.maxPreco())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("valor"), listarVeiculosDTO.maxPreco()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
