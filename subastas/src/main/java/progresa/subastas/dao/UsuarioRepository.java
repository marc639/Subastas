package progresa.subastas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import progresa.subastas.entity.Usuario;

import java.util.Optional;

@RepositoryRestResource
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByNif(String nif);
    Optional<Usuario> findByEmail(String email);
}
