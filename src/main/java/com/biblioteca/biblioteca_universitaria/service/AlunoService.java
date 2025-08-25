package com.biblioteca.biblioteca_universitaria.service;

import com.biblioteca.biblioteca_universitaria.model.Aluno;
import com.biblioteca.biblioteca_universitaria.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> findById(Long id) {
        return alunoRepository.findById(id);
    }

    public Optional<Aluno> findByMatricula(String matricula) {
        return alunoRepository.findByMatricula(matricula);
    }

    public Aluno save(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public void deleteById(Long id) {
        alunoRepository.deleteById(id);
    }

    public List<Aluno> findByNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Aluno> findByCurso(String curso) {
        return alunoRepository.findByCurso(curso);
    }

    public List<Aluno> findAlunosComEmprestimos() {
        return alunoRepository.findAlunosComEmprestimos();
    }

    public List<Aluno> searchAlunos(String termo) {
        return alunoRepository.searchAlunos(termo);
    }

    public boolean existsByMatricula(String matricula) {
        return alunoRepository.findByMatricula(matricula).isPresent();
    }
}
