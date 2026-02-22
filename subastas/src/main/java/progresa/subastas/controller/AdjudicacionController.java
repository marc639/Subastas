package progresa.subastas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.subastas.dto.Mensaje;
import progresa.subastas.entity.Adjudicacion;
import progresa.subastas.service.AdjudicacionService;

import java.util.List;

@RestController
@RequestMapping("/api/adjudicacion")
@CrossOrigin(origins = "*")
public class AdjudicacionController {

    @Autowired
    AdjudicacionService adjudicacionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Adjudicacion>> lista() {
        return new ResponseEntity<>(adjudicacionService.list(), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        if (!adjudicacionService.existsById(id))
            return new ResponseEntity<>(new Mensaje("La adjudicacion no existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(adjudicacionService.getById(id).get(), HttpStatus.OK);
    }
}
