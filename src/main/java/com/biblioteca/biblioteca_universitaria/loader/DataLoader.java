package com.biblioteca.biblioteca_universitaria.loader;

import com.biblioteca.biblioteca_universitaria.model.Autor;
import com.biblioteca.biblioteca_universitaria.model.Livro;
import com.biblioteca.biblioteca_universitaria.model.Aluno;
import com.biblioteca.biblioteca_universitaria.repository.AutorRepository;
import com.biblioteca.biblioteca_universitaria.repository.LivroRepository;
import com.biblioteca.biblioteca_universitaria.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void run(String... args) throws Exception {
        Map<Long, Autor> autoresMap = carregarAutores();
        carregarLivros(autoresMap);
        carregarAlunos();
    }

    private Map<Long, Autor> carregarAutores() {
        Map<Long, Autor> autoresMap = new HashMap<>();
        try {
            var resource = resourceLoader.getResource("classpath:autores.txt");
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] dados = linha.split(";");
                    if (dados.length >= 3) {
                        Autor autor = new Autor();
                        autor.setNome(dados[1].trim());
                        autor.setNacionalidade(dados[2].trim());
                        Autor autorSalvo = autorRepository.save(autor);

                        // Mapear ID do arquivo para o objeto salvo
                        Long idArquivo = Long.parseLong(dados[0].trim());
                        autoresMap.put(idArquivo, autorSalvo);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar autores: " + e.getMessage());
        }
        return autoresMap;
    }

    private void carregarLivros(Map<Long, Autor> autoresMap) {
        try {
            var resource = resourceLoader.getResource("classpath:livros.txt");
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] dados = linha.split(";");
                    if (dados.length >= 5) {
                        Livro livro = new Livro();
                        livro.setTitulo(dados[1].trim());
                        livro.setIsbn(dados[2].trim());
                        livro.setAnoPublicacao(Integer.parseInt(dados[3].trim()));

                        Long autorId = Long.parseLong(dados[4].trim());
                        Autor autor = autoresMap.get(autorId);
                        if (autor != null) {
                            livro.setAutor(autor);
                            livroRepository.save(livro);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar livros: " + e.getMessage());
        }
    }

    private void carregarAlunos() {
        try {
            var resource = resourceLoader.getResource("classpath:alunos.txt");
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] dados = linha.split(";");
                    if (dados.length >= 3) {
                        Aluno aluno = new Aluno();
                        aluno.setNome(dados[0].trim());
                        aluno.setMatricula(dados[1].trim());
                        aluno.setCurso(dados[2].trim());

                        if (dados.length >= 4) {
                            aluno.setEmail(dados[3].trim());
                        }
                        if (dados.length >= 5) {
                            aluno.setTelefone(dados[4].trim());
                        }

                        alunoRepository.save(aluno);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar alunos: " + e.getMessage());

            // Carregar alunos de exemplo em caso de erro
            Aluno aluno1 = new Aluno("João Silva", "2023001", "Engenharia de Software", "joao@email.com", "(11) 99999-9999");
            Aluno aluno2 = new Aluno("Maria Santos", "2023002", "Ciência da Computação", "maria@email.com", "(11) 98888-8888");

            alunoRepository.save(aluno1);
            alunoRepository.save(aluno2);
        }
    }
}
