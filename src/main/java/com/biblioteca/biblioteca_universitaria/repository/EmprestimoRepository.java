package com.biblioteca.biblioteca_universitaria.repository;

import com.biblioteca.biblioteca_universitaria.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByAlunoId(Long alunoId);

    List<Emprestimo> findByLivroId(Long livroId);

    List<Emprestimo> findByStatus(String status);

    @Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucao < :dataAtual AND e.status = 'ATIVO'")
    List<Emprestimo> findEmprestimosAtrasados(@Param("dataAtual") LocalDate dataAtual);
}
