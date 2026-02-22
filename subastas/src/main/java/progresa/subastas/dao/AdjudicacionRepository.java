package progresa.subastas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import progresa.subastas.entity.Adjudicacion;

@RepositoryRestResource
public interface AdjudicacionRepository extends JpaRepository<Adjudicacion, Long> {
    boolean existsBySubastaId(Long subastaId);
}
