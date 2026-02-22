package progresa.subastas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.subastas.dao.AdjudicacionRepository;
import progresa.subastas.entity.Adjudicacion;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdjudicacionService {

    @Autowired
    private AdjudicacionRepository adjudicacionRepository;

    public List<Adjudicacion> list() {
        return adjudicacionRepository.findAll();
    }

    public Optional<Adjudicacion> getById(Long id) {
        return adjudicacionRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return adjudicacionRepository.existsById(id);
    }

    public boolean existeAdjudicacionPorSubasta(Long subastaId) {
        return adjudicacionRepository.existsBySubastaId(subastaId);
    }

    public void save(Adjudicacion adjudicacion) {
        adjudicacionRepository.save(adjudicacion);
    }
}
