package progresa.subastas.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.subastas.dto.Mensaje;
import progresa.subastas.dto.UsuarioDTO;
import progresa.subastas.entity.Usuario;
import progresa.subastas.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/lista")
    public ResponseEntity<List<Usuario>> lista() {
        List<Usuario> list = usuarioService.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        if (!usuarioService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(usuarioService.getById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UsuarioDTO usuarioDTO) {
        if (StringUtils.isBlank(usuarioDTO.getNombre()))
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(usuarioDTO.getEmail()))
            return new ResponseEntity<>(new Mensaje("El email es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(usuarioDTO.getNif()))
            return new ResponseEntity<>(new Mensaje("El NIF es obligatorio"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existsByEmail(usuarioDTO.getEmail()))
            return new ResponseEntity<>(new Mensaje("El email ya esta registrado"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existsByNif(usuarioDTO.getNif()))
            return new ResponseEntity<>(new Mensaje("El NIF ya esta registrado"), HttpStatus.BAD_REQUEST);

        Usuario usuario = new Usuario(usuarioDTO.getNombre(), usuarioDTO.getEmail(), usuarioDTO.getNif());
        usuarioService.save(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario creado correctamente"), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        if (!usuarioService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(usuarioDTO.getNombre()))
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(usuarioDTO.getEmail()))
            return new ResponseEntity<>(new Mensaje("El email es obligatorio"), HttpStatus.BAD_REQUEST);

        Usuario usuario = usuarioService.getById(id).get();

        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && usuarioService.existsByEmail(usuarioDTO.getEmail()))
            return new ResponseEntity<>(new Mensaje("El email ya esta registrado por otro usuario"), HttpStatus.BAD_REQUEST);

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuarioService.save(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario actualizado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!usuarioService.existsById(id))
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        usuarioService.delete(id);
        return new ResponseEntity<>(new Mensaje("Usuario eliminado correctamente"), HttpStatus.OK);
    }
}
