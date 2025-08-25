package com.biblioteca.biblioteca_universitaria.service;

import com.biblioteca.biblioteca_universitaria.model.Autor;
import com.biblioteca.biblioteca_universitaria.repository.AutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Optional<Autor> buscarPorId(Long id) {
        return autorRepository.findById(id);
    }

    public Autor salvar(Autor autor) {
        return autorRepository.save(autor);
    }

    @Transactional
    public boolean deletarPorId(Long id) {
        Optional<Autor> autorOptional = autorRepository.findById(id);

        if (autorOptional.isPresent()) {
            autorRepository.deleteById(id);
            return true;
        }

        return false;
    }
}

