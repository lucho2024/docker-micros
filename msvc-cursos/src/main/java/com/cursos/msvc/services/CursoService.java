package com.cursos.msvc.services;

import com.cursos.msvc.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();
    Optional<Curso> byId(Long id);

    Curso guardar(Curso curso);

    void eliminar(Long id);

}
