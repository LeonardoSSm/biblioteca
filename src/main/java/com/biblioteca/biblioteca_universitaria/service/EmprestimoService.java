package com.biblioteca.biblioteca_universitaria.service;

import com.biblioteca.biblioteca_universitaria.model.Emprestimo;
import com.biblioteca.biblioteca_universitaria.model.Livro;
import com.biblioteca.biblioteca_universitaria.model.Aluno;
import com.biblioteca.biblioteca_universitaria.repository.EmprestimoRepository;
import com.biblioteca.biblioteca_universitaria.repository.LivroRepository;
import com.biblioteca.biblioteca_universitaria.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public List<Emprestimo> findAll() {
        return emprestimoRepository.findAll();
    }

    public Optional<Emprestimo> findById(Long id) {
        return emprestimoRepository.findById(id);
    }

    public Emprestimo save(Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    public void deleteById(Long id) {
        emprestimoRepository.deleteById(id);
    }

    public List<Emprestimo> findByAlunoId(Long alunoId) {
        return emprestimoRepository.findByAlunoId(alunoId);
    }

    public List<Emprestimo> findByLivroId(Long livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }

    public List<Emprestimo> findByStatus(String status) {
        return emprestimoRepository.findByStatus(status);
    }

    public List<Emprestimo> findEmprestimosAtrasados() {
        return emprestimoRepository.findEmprestimosAtrasados(LocalDate.now());
    }

    public Optional<Emprestimo> realizarEmprestimo(Long livroId, Long alunoId) {
        Optional<Livro> livroOpt = livroRepository.findById(livroId);
        Optional<Aluno> alunoOpt = alunoRepository.findById(alunoId);

        if (livroOpt.isPresent() && alunoOpt.isPresent()) {
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setLivro(livroOpt.get());
            emprestimo.setAluno(alunoOpt.get());
            emprestimo.setDataEmprestimo(LocalDate.now());
            emprestimo.setDataDevolucao(LocalDate.now().plusDays(15)); // 15 dias para devolução
            emprestimo.setStatus("ATIVO");

            return Optional.of(emprestimoRepository.save(emprestimo));
        }

        return Optional.empty();
    }

    public Optional<Emprestimo> finalizarEmprestimo(Long emprestimoId) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findById(emprestimoId);

        if (emprestimoOpt.isPresent()) {
            Emprestimo emprestimo = emprestimoOpt.get();
            emprestimo.setDataDevolucao(LocalDate.now());
            emprestimo.setStatus("FINALIZADO");

            return Optional.of(emprestimoRepository.save(emprestimo));
        }

        return Optional.empty();
    }
}
