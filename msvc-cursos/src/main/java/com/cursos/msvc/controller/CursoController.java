package com.cursos.msvc.controller;

import com.cursos.msvc.models.UsuarioPojo;
import com.cursos.msvc.models.entity.Curso;
import com.cursos.msvc.services.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@Slf4j
public class CursoController {


    @Autowired
    CursoService cursoService;

    private static ResponseEntity<Map<String, String>> validar(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), "El campo " + e.getField() + " "
                + e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {

        return new ResponseEntity<>(cursoService.byId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no existe el curso"))
                , HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }
        Curso cursoDb = cursoService.guardar(curso);

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);


    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult bindingResult, @PathVariable Long id) {

        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }
        Curso cursoEditar = cursoService.byId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "curso no encontrado"));

        cursoEditar.setNombre(curso.getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoEditar));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        cursoService.byId(id)
                .ifPresentOrElse(
                        curso -> cursoService.eliminar(curso.getId()),
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "curso no encontrado")
                );

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody UsuarioPojo usuarioPojo, @PathVariable Long cursoId) {

        Optional<UsuarioPojo> usuarioPojoOptional;
        try {
            usuarioPojoOptional = cursoService.asignarUsuario(usuarioPojo, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje", "no existe el usuario por id " +
                                    "o error en la comunicacion " + e.getMessage()));
        }

        if (usuarioPojoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioPojoOptional.get());
        }

        return ResponseEntity.notFound().build();


    }


    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioPojo usuarioPojo, @PathVariable Long cursoId) {

        Optional<UsuarioPojo> usuarioPojoOptional;
        try {
            usuarioPojoOptional = cursoService.crearUsuario(usuarioPojo, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje", "No se pudo crear el usuario  " +
                                    "o error en la comunicacion " + e.getMessage()));
        }

        if (usuarioPojoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioPojoOptional.get());
        }

        return ResponseEntity.notFound().build();


    }


    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody UsuarioPojo usuarioPojo, @PathVariable Long cursoId) {

        Optional<UsuarioPojo> usuarioPojoOptional;
        try {
            usuarioPojoOptional = cursoService.eliminarUsuarioCurso(usuarioPojo, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje", "No se pudo crear el usuario  " +
                                    "o error en la comunicacion " + e.getMessage()));
        }

        if (usuarioPojoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioPojoOptional.get());
        }

        return ResponseEntity.notFound().build();


    }

    @GetMapping("/usuarios-curso/{id}")
    public ResponseEntity<?> detalleCursos(@PathVariable Long id) {

        return new ResponseEntity<>(cursoService.porIdConUsuarios(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no existe el curso"))
                , HttpStatus.OK);
    }


    @DeleteMapping("/eliminar-usuario-curso/{id}")
    public ResponseEntity<?>eliminarCursoUsuarioPorId(@PathVariable Long id){
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }
}
