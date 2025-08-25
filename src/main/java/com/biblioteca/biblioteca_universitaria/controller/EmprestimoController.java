package com.biblioteca.biblioteca_universitaria.controller;

import com.biblioteca.biblioteca_universitaria.model.Emprestimo;
import com.biblioteca.biblioteca_universitaria.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    public List<Emprestimo> listarEmprestimos() {
        return emprestimoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarEmprestimo(@PathVariable Long id) {
        Optional<Emprestimo> emprestimo = emprestimoService.findById(id);
        return emprestimo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Emprestimo adicionarEmprestimo(@RequestBody Emprestimo emprestimo) {
        return emprestimoService.save(emprestimo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emprestimo> atualizarEmprestimo(@PathVariable Long id, @RequestBody Emprestimo emprestimo) {
        if (!emprestimoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        emprestimo.setId(id);
        return ResponseEntity.ok(emprestimoService.save(emprestimo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEmprestimo(@PathVariable Long id) {
        if (!emprestimoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        emprestimoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aluno/{alunoId}")
    public List<Emprestimo> buscarPorAluno(@PathVariable Long alunoId) {
        return emprestimoService.findByAlunoId(alunoId);
    }

    @GetMapping("/livro/{livroId}")
    public List<Emprestimo> buscarPorLivro(@PathVariable Long livroId) {
        return emprestimoService.findByLivroId(livroId);
    }

    @GetMapping("/status/{status}")
    public List<Emprestimo> buscarPorStatus(@PathVariable String status) {
        return emprestimoService.findByStatus(status);
    }

    @GetMapping("/atrasados")
    public List<Emprestimo> buscarAtrasados() {
        return emprestimoService.findEmprestimosAtrasados();
    }

    @PostMapping("/realizar")
    public ResponseEntity<Emprestimo> realizarEmprestimo(@RequestParam Long livroId, @RequestParam Long alunoId) {
        Optional<Emprestimo> emprestimo = emprestimoService.realizarEmprestimo(livroId, alunoId);
        return emprestimo.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/finalizar/{id}")
    public ResponseEntity<Emprestimo> finalizarEmprestimo(@PathVariable Long id) {
        Optional<Emprestimo> emprestimo = emprestimoService.finalizarEmprestimo(id);
        return emprestimo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
