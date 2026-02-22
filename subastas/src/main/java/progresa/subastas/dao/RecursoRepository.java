package progresa.subastas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import progresa.subastas.entity.Recurso;

import java.util.List;

@RepositoryRestResource
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByAdjudicacionId(Long adjudicacionId);
}
