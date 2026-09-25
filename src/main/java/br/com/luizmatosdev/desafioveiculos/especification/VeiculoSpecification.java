package br.com.luizmatosdev.desafioveiculos.especification;

import br.com.luizmatosdev.desafioveiculos.dto.veiculo.ListarVeiculosDTO;
import br.com.luizmatosdev.desafioveiculos.entity.Veiculo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VeiculoSpecification {

    private VeiculoSpecification() {
        throw new UnsupportedOperationException("Classe não instanciada");
    }

    public static Specification<Veiculo> filtrar(ListarVeiculosDTO listarVeiculosDTO) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(listarVeiculosDTO.marca())) {
                predicates.add(criteriaBuilder.like(root.get("marca"), "%" + listarVeiculosDTO.marca() + "%"));
            }

            if (Objects.nonNull(listarVeiculosDTO.ano())) {
                predicates.add(criteriaBuilder.equal(root.get("ano"), listarVeiculosDTO.ano()));
            }

            if (Objects.nonNull(listarVeiculosDTO.cor())) {
                predicates.add(criteriaBuilder.like(root.get("cor"), "%" + listarVeiculosDTO.cor() + "%"));
            }

            if (Objects.nonNull(listarVeiculosDTO.minPreco())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("preco"), listarVeiculosDTO.minPreco()));
            }

            if (Objects.nonNull(listarVeiculosDTO.maxPreco())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("preco"), listarVeiculosDTO.maxPreco()));
            }

            if (Objects.nonNull(listarVeiculosDTO.sort())) {
                var orders = listarVeiculosDTO.sort().stream()
                    .map(order -> order.isAscending() 
                        ? criteriaBuilder.asc(root.get(order.getProperty()))
                        : criteriaBuilder.desc(root.get(order.getProperty())))
                    .toList();
                criteriaQuery.orderBy(orders);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
