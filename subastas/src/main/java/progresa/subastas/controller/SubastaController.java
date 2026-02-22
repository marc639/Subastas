package progresa.subastas.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.subastas.dto.Mensaje;
import progresa.subastas.dto.PujaDTO;
import progresa.subastas.dto.SubastaDTO;
import progresa.subastas.entity.*;
import progresa.subastas.service.AdjudicacionService;
import progresa.subastas.service.BienService;
import progresa.subastas.service.SubastaService;
import progresa.subastas.service.UsuarioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subasta")
@CrossOrigin(origins = "*")
public class SubastaController {

    @Autowired
    SubastaService subastaService;

    @Autowired
    BienService bienService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AdjudicacionService adjudicacionService;


    @GetMapping("/lista")
    public ResponseEntity<List<Subasta>> lista() {
        return new ResponseEntity<>(subastaService.list(), HttpStatus.OK);
    }


    @GetMapping("/activas")
    public ResponseEntity<List<Subasta>> activas() {
        return new ResponseEntity<>(subastaService.getByEstado("ABIERTA"), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(subastaService.getById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SubastaDTO subastaDTO) {
        if (subastaDTO.getBienId() == null)
            return new ResponseEntity<>(new Mensaje("El id del bien es obligatorio"), HttpStatus.BAD_REQUEST);
        if (!bienService.existsById(subastaDTO.getBienId()))
            return new ResponseEntity<>(new Mensaje("El bien no existe"), HttpStatus.NOT_FOUND);
        if (subastaService.existeSubastaPorBien(subastaDTO.getBienId()))
            return new ResponseEntity<>(new Mensaje("El bien ya tiene una subasta asignada"), HttpStatus.BAD_REQUEST);
        if (subastaDTO.getPrecioBase() == null || subastaDTO.getPrecioBase() <= 0)
            return new ResponseEntity<>(new Mensaje("El precio base debe ser mayor que 0"), HttpStatus.BAD_REQUEST);
        if (subastaDTO.getFechaInicio() == null || subastaDTO.getFechaFin() == null)
            return new ResponseEntity<>(new Mensaje("Las fechas de inicio y fin son obligatorias"), HttpStatus.BAD_REQUEST);
        if (subastaDTO.getFechaInicio().isAfter(subastaDTO.getFechaFin()))
            return new ResponseEntity<>(new Mensaje("La fecha de inicio no puede ser posterior a la fecha de fin"), HttpStatus.BAD_REQUEST);

        Bien bien = bienService.getById(subastaDTO.getBienId()).get();
        bien.setEstado("EN_SUBASTA");
        bienService.save(bien);

        Subasta subasta = new Subasta(
                subastaDTO.getFechaInicio(),
                subastaDTO.getFechaFin(),
                subastaDTO.getPrecioBase(),
                bien
        );
        subastaService.save(subasta);
        return new ResponseEntity<>(new Mensaje("Subasta creada correctamente"), HttpStatus.OK);
    }

    @PutMapping("/cerrar/{id}")
    public ResponseEntity<?> cerrar(@PathVariable Long id) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"),
                    HttpStatus.NOT_FOUND);

        Subasta subasta = subastaService.getById(id).get();
        if (!subasta.getEstado().equals("ABIERTA"))
            return new ResponseEntity<>(new Mensaje("Solo se pueden cerrar subastas en estado ABIERTA"), HttpStatus.BAD_REQUEST);

        subasta.setEstado("CERRADA");
        subastaService.save(subasta);
        return new ResponseEntity<>(new Mensaje("Subasta cerrada. Puja más alta actual: " + subasta.obtenerPujaMasAlta()), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"), HttpStatus.NOT_FOUND);

        Subasta subasta = subastaService.getById(id).get();
        if (!subasta.getEstado().equals("ABIERTA"))
            return new ResponseEntity<>(new Mensaje("No se puede eliminar una subasta que no esta ABIERTA"), HttpStatus.BAD_REQUEST);
        if (!subasta.getPujas().isEmpty())
            return new ResponseEntity<>(new Mensaje("No se puede eliminar una subasta que ya tiene pujas"), HttpStatus.BAD_REQUEST);

        Bien bien = subasta.getBien();
        bien.setEstado("DISPONIBLE");
        bienService.save(bien);

        subastaService.delete(id);
        return new ResponseEntity<>(new Mensaje("Subasta eliminada correctamente"), HttpStatus.OK);
    }

    @PostMapping("/{id}/puja")
    public ResponseEntity<?> puja(@PathVariable Long id, @RequestBody PujaDTO pujaDTO) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"), HttpStatus.NOT_FOUND);

        Subasta subasta = subastaService.getById(id).get();
        if (!subasta.getEstado().equals("ABIERTA"))
            return new ResponseEntity<>(new Mensaje("La subasta no esta ABIERTA, no se puede pujar"), HttpStatus.BAD_REQUEST);

        if (pujaDTO.getUsuarioId() == null)
            return new ResponseEntity<>(new Mensaje("El id de usuario es obligatorio"), HttpStatus.BAD_REQUEST);
        if (!usuarioService.existsById(pujaDTO.getUsuarioId()))
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        if (pujaDTO.getImporte() == null || pujaDTO.getImporte() <= 0)
            return new ResponseEntity<>(new Mensaje("El importe de la puja debe ser mayor que 0"), HttpStatus.BAD_REQUEST);

        double minimoRequerido = subasta.obtenerPujaMasAlta();

        if (pujaDTO.getImporte() <= minimoRequerido)
            return new ResponseEntity<>(
                    new Mensaje("La puja debe superar el importe actual: " + minimoRequerido + " EUR"),
                    HttpStatus.BAD_REQUEST);

        Usuario usuario = usuarioService.getById(pujaDTO.getUsuarioId()).get();
        Puja puja = new Puja(pujaDTO.getImporte(), subasta, usuario);
        subastaService.savePuja(puja);
        return new ResponseEntity<>(new Mensaje("Puja registrada correctamente"), HttpStatus.OK);
    }

    @GetMapping("/{id}/pujas")
    public ResponseEntity<?> pujas(@PathVariable Long id) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(subastaService.getPujas(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/mejor-puja")
    public ResponseEntity<?> mejorPuja(@PathVariable Long id) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"), HttpStatus.NOT_FOUND);

        Optional<Puja> mejorPuja = subastaService.getMejorPuja(id);
        if (mejorPuja.isEmpty())
            return new ResponseEntity<>(new Mensaje("No hay pujas en esta subasta"), HttpStatus.OK);

        return new ResponseEntity<>(mejorPuja.get(), HttpStatus.OK);
    }

    @PostMapping("/{id}/adjudicar")
    public ResponseEntity<?> adjudicar(@PathVariable Long id) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"), HttpStatus.NOT_FOUND);

        Subasta subasta = subastaService.getById(id).get();
        if (!subasta.getEstado().equals("CERRADA"))
            return new ResponseEntity<>(new Mensaje("La subasta debe estar CERRADA para poder adjudicarse"), HttpStatus.BAD_REQUEST);
        if (adjudicacionService.existeAdjudicacionPorSubasta(id))
            return new ResponseEntity<>(new Mensaje("Esta subasta ya ha sido adjudicada"), HttpStatus.BAD_REQUEST);

        Optional<Puja> mejorPuja = subastaService.getMejorPuja(id);
        if (mejorPuja.isEmpty())
            return new ResponseEntity<>(new Mensaje("No hay pujas, use el endpoint /desierta"), HttpStatus.BAD_REQUEST);

        Puja ganadora = mejorPuja.get();
        Adjudicacion adjudicacion = new Adjudicacion(ganadora.getImporte(), subasta, ganadora.getUsuario());
        adjudicacionService.save(adjudicacion);

        subasta.setEstado("ADJUDICADA");
        subastaService.save(subasta);

        Bien bien = subasta.getBien();
        bien.setEstado("ADJUDICADO");
        bienService.save(bien);

        return new ResponseEntity<>(
                new Mensaje("Subasta adjudicada a " + ganadora.getUsuario().getNombre() + " por " + ganadora.getImporte() + " EUR"),
                HttpStatus.OK);
    }

    @PostMapping("/{id}/desierta")
    public ResponseEntity<?> desierta(@PathVariable Long id) {
        if (!subastaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La subasta no existe"), HttpStatus.NOT_FOUND);

        Subasta subasta = subastaService.getById(id).get();
        if (!subasta.getEstado().equals("CERRADA"))
            return new ResponseEntity<>(new Mensaje("La subasta debe estar CERRADA para declararla desierta"), HttpStatus.BAD_REQUEST);

        subasta.setEstado("DESIERTA");
        subastaService.save(subasta);

        Bien bien = subasta.getBien();
        bien.setEstado("DISPONIBLE");
        bienService.save(bien);

        return new ResponseEntity<>(new Mensaje("Subasta declarada desierta. El bien vuelve a estar DISPONIBLE"), HttpStatus.OK);
    }
}
