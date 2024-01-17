package com.cursos.msvc.clients;

import com.cursos.msvc.models.UsuarioPojo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url}")
public interface UsuarioClient {


    @GetMapping("/v1/{id}")
    UsuarioPojo detalle(@PathVariable Long id);

    @PostMapping("/v1/")
    UsuarioPojo crear(@RequestBody UsuarioPojo usuarioPojo);

    @GetMapping("/v1/usuarios-por-curso")
    List<UsuarioPojo> obtenerAlumnosPorCurso(@RequestParam Iterable<Long>ids);




}
