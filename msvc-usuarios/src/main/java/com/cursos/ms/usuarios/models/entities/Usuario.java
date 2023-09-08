package com.cursos.ms.usuarios.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty(message = "El nombre no puede ser null")
    private String nombre;

    @NotEmpty
    @Email
    @Column(unique = true)//solo aplica si se crea la tabla de forma automatica, si la creamos nosotros debemos crear
    //un indice con un constraint unique
    private String email;

    @NotBlank
    private String password;





}
