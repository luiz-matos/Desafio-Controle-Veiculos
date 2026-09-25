package br.com.luizmatosdev.desafioveiculos.repository;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, E> extends JpaRepository<T, E>, JpaSpecificationExecutor<T> {

    @Override
    @Nonnull
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.deletado = false")
    Optional<T> findById(@Nonnull @Param("id") E id);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.deletado = true WHERE e = :entity")
    void delete(@Nonnull @Param("entity") T entity);
}
