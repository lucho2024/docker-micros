package com.cursos.ms.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-cursos",url = "host.docker.intertal:8002")
public interface CursoClient {


    @DeleteMapping("/eliminar-usuario-curso/{id}")
    void eliminarCursoUsuarioPorId(@PathVariable Long id);
}