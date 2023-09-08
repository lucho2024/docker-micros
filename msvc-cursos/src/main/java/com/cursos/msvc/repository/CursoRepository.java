package com.cursos.msvc.repository;

import com.cursos.msvc.models.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso,Long> {
}
