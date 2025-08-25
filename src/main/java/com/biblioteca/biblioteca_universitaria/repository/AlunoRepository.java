package com.biblioteca.biblioteca_universitaria.repository;

import com.biblioteca.biblioteca_universitaria.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByMatricula(String matricula);

    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    List<Aluno> findByCurso(String curso);

    @Query("SELECT a FROM Aluno a WHERE SIZE(a.emprestimos) > 0")
    List<Aluno> findAlunosComEmprestimos();

    @Query("SELECT a FROM Aluno a WHERE a.matricula LIKE %:termo% OR a.nome LIKE %:termo% OR a.curso LIKE %:termo%")
    List<Aluno> searchAlunos(@Param("termo") String termo);
}