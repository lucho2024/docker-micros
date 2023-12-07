package com.cursos.msvc.repository;

import com.cursos.msvc.models.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CursoRepository extends JpaRepository<Curso,Long> {
    @Modifying//para que la anotacion query pueda hacer modificacione
    @Query("delete  from CursoUsuario cu where cu.usuarioId=?1")
    void eliminarCursoUsuarioPorId(Long Id);

}
