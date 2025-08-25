package com.biblioteca.biblioteca_universitaria.controller;

import com.biblioteca.biblioteca_universitaria.model.Aluno;
import com.biblioteca.biblioteca_universitaria.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public List<Aluno> listarAlunos() {
        return alunoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarAluno(@PathVariable Long id) {
        Optional<Aluno> aluno = alunoService.findById(id);
        return aluno.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<Aluno> buscarAlunoPorMatricula(@PathVariable String matricula) {
        Optional<Aluno> aluno = alunoService.findByMatricula(matricula);
        return aluno.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Aluno adicionarAluno(@RequestBody Aluno aluno) {
        return alunoService.save(aluno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @RequestBody Aluno aluno) {
        if (!alunoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        aluno.setId(id);
        return ResponseEntity.ok(alunoService.save(aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerAluno(@PathVariable Long id) {
        if (!alunoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        alunoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Aluno> buscarPorNome(@RequestParam String nome) {
        return alunoService.findByNome(nome);
    }

    @GetMapping("/curso/{curso}")
    public List<Aluno> buscarPorCurso(@PathVariable String curso) {
        return alunoService.findByCurso(curso);
    }

    @GetMapping("/com-emprestimos")
    public List<Aluno> buscarAlunosComEmprestimos() {
        return alunoService.findAlunosComEmprestimos();
    }

    @GetMapping("/pesquisar/{termo}")
    public List<Aluno> pesquisarAlunos(@PathVariable String termo) {
        return alunoService.searchAlunos(termo);
    }

    @GetMapping("/verificar-matricula/{matricula}")
    public boolean verificarMatricula(@PathVariable String matricula) {
        return alunoService.existsByMatricula(matricula);
    }
}
