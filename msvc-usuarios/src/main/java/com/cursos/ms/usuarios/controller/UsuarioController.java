package com.cursos.ms.usuarios.controller;

import com.cursos.ms.usuarios.models.entities.Usuario;
import com.cursos.ms.usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/v1")
public class UsuarioController {


    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Environment environment;

    @GetMapping("/crash")
    public void crash(){
        ((ConfigurableApplicationContext)context).close();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(e -> {
            errors.put(e.getField(), "El Campo " + e.getField() + e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/")
    public Map<String,Object> listar() {
        Map<String,Object> body = new HashMap<>();
        body.put("usuarios",usuarioService.listar());
        body.put("podinfo",environment.getProperty("MY_POD_NAME")+": "+environment.getProperty("MY_POD_IP"));
        body.put("texto",environment.getProperty("config.texto"));


        return  body;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.porId(id);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con id " + id + "no fue encontrado");
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }

        if (!usuario.getEmail().isEmpty() && usuarioService.existePorEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("Mensaje", "Ya existe este email en la base de datos"));
        }

        return ResponseEntity.ok().body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult bindingResult, @PathVariable Long id) {


        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }

        Optional<Usuario> usuarioOptional = usuarioService.porId(id);

        if (!usuario.getEmail().isEmpty() && usuarioOptional.isPresent()) {
            Usuario usuariDb = usuarioOptional.get();
            if (!usuario.getEmail().equalsIgnoreCase(usuariDb.getEmail()) && usuarioService.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("Mensaje", "Ya existe este email en la base de datos"));
            }
            usuariDb.setNombre(usuario.getNombre());
            usuariDb.setEmail(usuario.getEmail());
            usuariDb.setPassword(usuario.getPassword());
            return new ResponseEntity<>(usuarioService.guardar(usuariDb), HttpStatus.CREATED);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con id " + id + "no fue encontrado");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        Optional<Usuario> usuarioOptional = usuarioService.porId(id);

        if (usuarioOptional.isPresent()) {
            usuarioService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con id " + id + "no fue encontrado");
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids) {

        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }


}
