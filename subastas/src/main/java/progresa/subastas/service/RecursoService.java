package progresa.subastas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.subastas.dao.RecursoRepository;
import progresa.subastas.entity.Recurso;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    public List<Recurso> list() {
        return recursoRepository.findAll();
    }

    public Optional<Recurso> getById(Long id) {
        return recursoRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return recursoRepository.existsById(id);
    }

    public void save(Recurso recurso) {
        recursoRepository.save(recurso);
    }

    public List<Recurso> getByAdjudicacion(Long adjudicacionId) {
        return recursoRepository.findByAdjudicacionId(adjudicacionId);
    }

    public void resolverRecurso(Long id, String nuevoEstado){
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        if (recurso.getEstado().equals("RESUELTO")){
            throw new RuntimeException("No se puede modificar un recurso ya finalizado");
        }

        recurso.setEstado(nuevoEstado);
        recursoRepository.save(recurso);
    }
}
