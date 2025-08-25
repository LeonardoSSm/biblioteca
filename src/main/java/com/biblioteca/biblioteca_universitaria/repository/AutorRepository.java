package com.biblioteca.biblioteca_universitaria.repository;

import com.biblioteca.biblioteca_universitaria.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNome(String nome);

    List<Autor> findByNomeContainingIgnoreCase(String nome);

    List<Autor> findByNacionalidade(String nacionalidade);

    @Query("SELECT DISTINCT a FROM Autor a JOIN a.livros l WHERE l.anoPublicacao > :ano")
    List<Autor> findAutoresComLivrosAposAno(@Param("ano") Integer ano);

    boolean existsByNome(String nome);
}