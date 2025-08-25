package com.biblioteca.biblioteca_universitaria.controller;

import com.biblioteca.biblioteca_universitaria.model.Autor;
import com.biblioteca.biblioteca_universitaria.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    public List<Autor> listarAutores() {
        return autorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Autor> buscarAutor(@PathVariable Long id) {
        return autorService.buscarPorId(id);
    }

    @PostMapping
    public Autor adicionarAutor(@RequestBody Autor autor) {
        return autorService.salvar(autor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id) {
        boolean deletado = autorService.deletarPorId(id);

        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

