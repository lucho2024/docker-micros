package com.cursos.msvc.services;

import com.cursos.msvc.clients.UsuarioClient;
import com.cursos.msvc.models.UsuarioPojo;
import com.cursos.msvc.models.entity.Curso;
import com.cursos.msvc.models.entity.CursoUsuario;
import com.cursos.msvc.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioClient usuarioClient;


    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> byId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UsuarioPojo> asignarUsuario(UsuarioPojo usuarioPojo, Long cursoId) {

        Optional<Curso> cursoOptional = cursoRepository.findById(cursoId);

        if (cursoOptional.isPresent()) {
            UsuarioPojo usuarioMsvc = usuarioClient.detalle(usuarioPojo.getId());


            Curso curso = cursoOptional.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UsuarioPojo> crearUsuario(UsuarioPojo usuarioPojo, Long cursoId) {
        Optional<Curso> cursoOptional = cursoRepository.findById(cursoId);

        if (cursoOptional.isPresent()) {
            UsuarioPojo usuarioMsvc = usuarioClient.crear(usuarioPojo);


            Curso curso = cursoOptional.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UsuarioPojo> eliminarUsuarioCurso(UsuarioPojo usuarioPojo, Long cursoId) {
        Optional<Curso> cursoOptional = cursoRepository.findById(cursoId);

        if (cursoOptional.isPresent()) {
            UsuarioPojo usuarioMsvc = usuarioClient.detalle(usuarioPojo.getId());


            Curso curso = cursoOptional.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.deleteCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Curso> porIdConUsuarios(Long id) {

        Optional<Curso> optionalCurso = cursoRepository.findById(id);

        if(optionalCurso.isPresent()){
            Curso curso = optionalCurso.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId).toList();

                List<UsuarioPojo> usuarioPojos = usuarioClient.obtenerAlumnosPorCurso(ids);
                curso.setUsuarioPojos(usuarioPojos);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }
}
