package com.biblioteca.biblioteca_universitaria.service;

import com.biblioteca.biblioteca_universitaria.model.Livro;
import com.biblioteca.biblioteca_universitaria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    public Optional<Livro> findById(Long id) {
        return livroRepository.findById(id);
    }

    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    }

    public void deleteById(Long id) {
        livroRepository.deleteById(id);
    }

    public List<Livro> findByTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> findByAutor(String nomeAutor) {
        return livroRepository.findByAutorNome(nomeAutor);
    }

    public List<Livro> findByAnoPublicacao(Integer ano) {
        return livroRepository.findByAnoPublicacao(ano);
    }
}
