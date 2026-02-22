package progresa.subastas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import progresa.subastas.entity.Puja;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface PujaRepository extends JpaRepository<Puja, Long> {
    List<Puja> findBySubastaId(Long subastaId);
    Optional<Puja> findTopBySubastaIdOrderByImporteDesc(Long subastaId);
}


