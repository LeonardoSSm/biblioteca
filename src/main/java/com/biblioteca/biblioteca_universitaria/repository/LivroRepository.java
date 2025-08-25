package com.biblioteca.biblioteca_universitaria.repository;

import com.biblioteca.biblioteca_universitaria.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT l FROM Livro l WHERE l.autor.nome LIKE %:nomeAutor%")
    List<Livro> findByAutorNome(@Param("nomeAutor") String nomeAutor);

    List<Livro> findByAnoPublicacao(Integer anoPublicacao);
}
