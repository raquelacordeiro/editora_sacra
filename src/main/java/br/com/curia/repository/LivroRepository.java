package br.com.curia.repository;

import br.com.curia.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Integer> {
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
}
