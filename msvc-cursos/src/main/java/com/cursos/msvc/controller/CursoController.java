package com.cursos.msvc.controller;

import com.cursos.msvc.entity.Curso;
import com.cursos.msvc.services.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {


    @Autowired
    CursoService cursoService;


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

        if (bindingResult.hasErrors()){
            return validar(bindingResult);
        }
        Curso cursoDb = cursoService.guardar(curso);

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);


    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso,BindingResult bindingResult, @PathVariable Long id) {

        if (bindingResult.hasErrors()){
            return validar(bindingResult);
        }
        Curso cursoEditar = cursoService.byId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "curso no encontrado"));

        cursoEditar.setNombre(curso.getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoEditar));

    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult bindingResult) {
        Map<String,String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(e-> errors.put(e.getField(),"El campo "+e.getField()+" "
                +e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
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


}
