package progresa.subastas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import progresa.subastas.entity.Bien;

import java.util.List;

@RepositoryRestResource
public interface BienRepository extends JpaRepository<Bien, Long> {
    boolean existsByDescripcion(String descripcion);
    List<Bien> findByEstado(String estado);
}
