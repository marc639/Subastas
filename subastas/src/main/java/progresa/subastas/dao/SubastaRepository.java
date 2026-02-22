package progresa.subastas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import progresa.subastas.entity.Subasta;

import java.util.List;

@RepositoryRestResource
public interface SubastaRepository extends JpaRepository<Subasta, Long> {
    List<Subasta> findByEstado(String estado);
    boolean existsByBienId(Long bienId);
}
