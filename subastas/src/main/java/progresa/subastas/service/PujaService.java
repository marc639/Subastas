package progresa.subastas.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.subastas.dao.PujaRepository;
import progresa.subastas.dao.SubastaRepository;
import progresa.subastas.dao.UsuarioRepository;
import progresa.subastas.dto.PujaDTO;
import progresa.subastas.entity.Puja;
import progresa.subastas.entity.Subasta;
import progresa.subastas.entity.Usuario;

import java.time.LocalDateTime;

@Service
@Transactional
public class PujaService {
    @Autowired
    PujaRepository pujaRepository;

    @Autowired
    SubastaRepository subastaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public void registrarPuja(PujaDTO pujaDTO) throws Exception {
        Subasta subasta = subastaRepository.findById(pujaDTO.getSubastaId())
                .orElseThrow(() -> new Exception("Subasta no encontrada"));


        Usuario usuario = usuarioRepository.findById(pujaDTO.getUsuarioId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        Double precioASuperar = subasta.obtenerPujaMasAlta();

        if (pujaDTO.getImporte() <= precioASuperar) {
            throw new Exception("El importe debe ser superior a la puja actual de: " + precioASuperar);
        }


        Puja puja = new Puja();
        puja.setImporte(pujaDTO.getImporte());
        puja.setFechaPuja(java.time.LocalDateTime.now());
        puja.setUsuario(usuario);
        puja.setSubasta(subasta);

        pujaRepository.save(puja);
    }
}
