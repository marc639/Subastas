package progresa.subastas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.subastas.dto.Mensaje;
import progresa.subastas.dto.PujaDTO;
import progresa.subastas.service.PujaService;

@RestController
@RequestMapping("/api/puja")
@CrossOrigin(origins = "*")

public class PujaController {
    @Autowired
    private PujaService pujaService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PujaDTO pujaDTO) {
        try {

            pujaService.registrarPuja(pujaDTO);
            return new ResponseEntity<>(new Mensaje("Puja registrada correctamente"), HttpStatus.CREATED);
        } catch (Exception e) {
            // Si la puja es menor a la actual, aquí se captura el error para mostrarlo en Postman
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
