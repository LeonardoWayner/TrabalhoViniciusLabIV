package br.com.flastore.repository;

import br.com.flastore.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByExcluidoFalse(Sort sort);
    Optional<Produto> findByIdAndExcluidoFalse(Long id);
    boolean existsByCodigo(String codigo);
    boolean existsByCodigoAndIdNot(String codigo, Long id);

    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.id = :id")
    Optional<Produto> buscarParaAtualizar(@Param("id") Long id);
}

