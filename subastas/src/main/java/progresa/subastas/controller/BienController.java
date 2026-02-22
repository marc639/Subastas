package progresa.subastas.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.subastas.dto.BienDTO;
import progresa.subastas.dto.Mensaje;
import progresa.subastas.entity.Bien;
import progresa.subastas.service.BienService;

import java.util.List;

@RestController
@RequestMapping("/api/bien")
@CrossOrigin(origins = "*")
public class BienController {

    @Autowired
    BienService bienService;

    @GetMapping("/lista")
    public ResponseEntity<List<Bien>> lista() {
        List<Bien> list = bienService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        if (!bienService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El bien no existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(bienService.getById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody BienDTO bienDTO) {
        if (StringUtils.isBlank(bienDTO.getDescripcion()))
            return new ResponseEntity<>(new Mensaje("La descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (bienDTO.getValorInicial() == null || bienDTO.getValorInicial() <= 0)
            return new ResponseEntity<>(new Mensaje("El valor inicial debe ser mayor que 0"),
                    HttpStatus.BAD_REQUEST);
        if (bienService.existsByDescripcion(bienDTO.getDescripcion()))
            return new ResponseEntity<>(new Mensaje("Ya existe un bien con esa descripcion"),
                    HttpStatus.BAD_REQUEST);

        Bien bien = new Bien(bienDTO.getDescripcion(), bienDTO.getValorInicial());
        bienService.save(bien);
        return new ResponseEntity<>(new Mensaje("Bien registrado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BienDTO bienDTO) {
        if (!bienService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El bien no existe"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(bienDTO.getDescripcion()))
            return new ResponseEntity<>(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);
        if (bienDTO.getValorInicial() == null || bienDTO.getValorInicial() <= 0)
            return new ResponseEntity<>(new Mensaje("El valor inicial debe ser mayor que 0"), HttpStatus.BAD_REQUEST);

        Bien bien = bienService.getById(id).get();

        if (!bien.getEstado().equals("DISPONIBLE"))
            return new ResponseEntity<>(new Mensaje("No se puede modificar un bien que no esta DISPONIBLE"), HttpStatus.BAD_REQUEST);

        bien.setDescripcion(bienDTO.getDescripcion());
        bien.setValorInicial(bienDTO.getValorInicial());
        bienService.save(bien);
        return new ResponseEntity<>(new Mensaje("Bien actualizado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!bienService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El bien no existe"), HttpStatus.NOT_FOUND);

        Bien bien = bienService.getById(id).get();
        if (!bien.getEstado().equals("DISPONIBLE"))
            return new ResponseEntity<>(new Mensaje("No se puede eliminar un bien que no esta DISPONIBLE"), HttpStatus.BAD_REQUEST);

        bienService.delete(id);
        return new ResponseEntity<>(new Mensaje("Bien eliminado correctamente"), HttpStatus.OK);
    }
}
