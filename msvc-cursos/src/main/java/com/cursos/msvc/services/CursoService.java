package com.cursos.msvc.services;

import com.cursos.msvc.models.UsuarioPojo;
import com.cursos.msvc.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();
    Optional<Curso> byId(Long id);

    Curso guardar(Curso curso);

    void eliminar(Long id);

    Optional<UsuarioPojo> asignarUsuario(UsuarioPojo usuarioPojo,Long curso_id);

    Optional<UsuarioPojo> crearUsuario(UsuarioPojo usuarioPojo,Long curso_id);

    Optional<UsuarioPojo> eliminarUsuarioCurso(UsuarioPojo usuarioPojo,Long curso_id);

    Optional<Curso> porIdConUsuarios(Long id);

    void eliminarCursoUsuarioPorId(Long  id);



}
