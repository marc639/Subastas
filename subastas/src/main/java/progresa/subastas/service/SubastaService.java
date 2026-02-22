package progresa.subastas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.subastas.dao.PujaRepository;
import progresa.subastas.dao.SubastaRepository;
import progresa.subastas.entity.Puja;
import progresa.subastas.entity.Subasta;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubastaService {

    @Autowired
    private SubastaRepository subastaRepository;

    @Autowired
    private PujaRepository pujaRepository;

    public List<Subasta> list() {
        return subastaRepository.findAll();
    }

    public Optional<Subasta> getById(Long id) {
        return subastaRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return subastaRepository.existsById(id);
    }

    public boolean existeSubastaPorBien(Long bienId) {
        return subastaRepository.existsByBienId(bienId);
    }

    public void save(Subasta subasta) {
        subastaRepository.save(subasta);
    }

    public void delete(Long id) {
        subastaRepository.deleteById(id);
    }

    public List<Subasta> getByEstado(String estado) {
        return subastaRepository.findByEstado(estado);
    }

    public List<Puja> getPujas(Long subastaId) {
        return pujaRepository.findBySubastaId(subastaId);
    }

    public Optional<Puja> getMejorPuja(Long subastaId) {
        return pujaRepository.findTopBySubastaIdOrderByImporteDesc(subastaId);
    }

    public void savePuja(Puja puja) {
        pujaRepository.save(puja);
    }

    public void realizarPuja(Long idSubasta, Puja nuevaPuja) throws Exception {
        Subasta subasta = subastaRepository.findById(idSubasta)
                .orElseThrow(() -> new Exception("La subasta no existe"));


        if (!subasta.getEstado().equals("ABIERTA")) {
            throw new Exception("No se admiten pujas: la subasta está "
                    + subasta.getEstado());
        }

        if (nuevaPuja.getImporte() <= subasta.obtenerPujaMasAlta()) {
            throw new Exception("La puja debe ser mayor a la actual: "
                    + subasta.obtenerPujaMasAlta());
        }

        nuevaPuja.setSubasta(subasta);
        pujaRepository.save(nuevaPuja);
    }

    public void cerrarSubasta(Long subastaId) throws Exception {
        Subasta subasta = subastaRepository.findById(subastaId)
                .orElseThrow(() -> new Exception("Subasta no encontrada"));

        if (subasta.getEstado().equals("CERRADA")) {
            throw new Exception("La subasta ya está cerrada");
        }


        Double pujaGanadora = subasta.obtenerPujaMasAlta();
        subasta.setEstado("CERRADA");

        subastaRepository.save(subasta);
    }
}
