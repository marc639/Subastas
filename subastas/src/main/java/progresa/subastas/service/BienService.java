package progresa.subastas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.subastas.dao.BienRepository;
import progresa.subastas.entity.Bien;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BienService {

    @Autowired
    private BienRepository bienRepository;

    public List<Bien> list() {
        return bienRepository.findAll();
    }

    public Optional<Bien> getById(Long id) {
        return bienRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return bienRepository.existsById(id);
    }

    public boolean existsByDescripcion(String descripcion) {
        return bienRepository.existsByDescripcion(descripcion);
    }

    public void save(Bien bien) {
        bienRepository.save(bien);
    }

    public void delete(Long id) {
        bienRepository.deleteById(id);
    }

    public List<Bien> getByEstado(String estado) {
        return bienRepository.findByEstado(estado);
    }
}
