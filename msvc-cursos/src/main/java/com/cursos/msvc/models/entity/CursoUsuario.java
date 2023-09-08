package com.cursos.msvc.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "cursos_usuarios")
@Getter
@Setter
public class CursoUsuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id",unique = true)
    private Long usuarioId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CursoUsuario that = (CursoUsuario) o;
        return Objects.equals(id, that.id) && Objects.equals(usuarioId, that.usuarioId);
    }

}
