package progresa.subastas.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.subastas.dto.Mensaje;
import progresa.subastas.dto.RecursoDTO;
import progresa.subastas.entity.Adjudicacion;
import progresa.subastas.entity.Recurso;
import progresa.subastas.service.AdjudicacionService;
import progresa.subastas.service.RecursoService;

import java.util.List;

@RestController
@RequestMapping("/api/recurso")
@CrossOrigin(origins = "*")
public class RecursoController {

    @Autowired
    RecursoService recursoService;

    @Autowired
    AdjudicacionService adjudicacionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Recurso>> lista() {
        return new ResponseEntity<>(recursoService.list(), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        if (!recursoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El recurso no existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(recursoService.getById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RecursoDTO recursoDTO) {
        if (recursoDTO.getAdjudicacionId() == null)
            return new ResponseEntity<>(new Mensaje("El id de la adjudicacion es obligatorio"), HttpStatus.BAD_REQUEST);
        if (!adjudicacionService.existsById(recursoDTO.getAdjudicacionId()))
            return new ResponseEntity<>(new Mensaje("La adjudicacion no existe"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(recursoDTO.getMotivo()))
            return new ResponseEntity<>(new Mensaje("El motivo del recurso es obligatorio"), HttpStatus.BAD_REQUEST);

        Adjudicacion adjudicacion = adjudicacionService.getById(recursoDTO.getAdjudicacionId()).get();
        Recurso recurso = new Recurso(recursoDTO.getMotivo(), adjudicacion);
        recursoService.save(recurso);
        return new ResponseEntity<>(new Mensaje("Recurso registrado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/resolver/{id}")
    public ResponseEntity<?> resolver(@PathVariable Long id, @RequestParam String estado) {
        if (!recursoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El recurso no existe"), HttpStatus.NOT_FOUND);
        if (!estado.equals("RESUELTO") && !estado.equals("DESESTIMADO"))
            return new ResponseEntity<>(new Mensaje("El estado debe ser RESUELTO o DESESTIMADO"), HttpStatus.BAD_REQUEST);

        Recurso recurso = recursoService.getById(id).get();
        if (!recurso.getEstado().equals("PENDIENTE"))
            return new ResponseEntity<>(new Mensaje("Solo se pueden resolver recursos en estado PENDIENTE"), HttpStatus.BAD_REQUEST);

        recurso.setEstado(estado);
        recursoService.save(recurso);
        return new ResponseEntity<>(new Mensaje("Recurso actualizado a estado: " + estado), HttpStatus.OK);
    }
}
