package com.biblioteca.biblioteca_universitaria.controller;

import com.biblioteca.biblioteca_universitaria.model.Livro;
import com.biblioteca.biblioteca_universitaria.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public List<Livro> listarLivros() {
        return livroService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivro(@PathVariable Long id) {
        Optional<Livro> livro = livroService.findById(id);
        return livro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Livro adicionarLivro(@RequestBody Livro livro) {
        return livroService.save(livro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @RequestBody Livro livro) {
        if (!livroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        livro.setId(id);
        return ResponseEntity.ok(livroService.save(livro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerLivro(@PathVariable Long id) {
        if (!livroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        livroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Livro> buscarPorTitulo(@RequestParam String titulo) {
        return livroService.findByTitulo(titulo);
    }

    @GetMapping("/autor/{nomeAutor}")
    public List<Livro> buscarPorAutor(@PathVariable String nomeAutor) {
        return livroService.findByAutor(nomeAutor);
    }

    @GetMapping("/ano/{ano}")
    public List<Livro> buscarPorAno(@PathVariable Integer ano) {
        return livroService.findByAnoPublicacao(ano);
    }
}
