
package br.com.curia.service;

import br.com.curia.model.Livro;
import br.com.curia.repository.CategoriaRepository;
import br.com.curia.repository.LivroRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class LivroService {

    private final LivroRepository repository;
    private final CategoriaRepository categoriaRepository;

    public LivroService(
            LivroRepository repository,
            CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Livro> listarTodos() {
        return repository.findAll();
    }

    public Livro buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Livro não encontrado."));
    }

    public List<Livro> buscar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }
        return repository.findByTituloContainingIgnoreCase(termo.trim());
    }

    public Livro salvar(Livro livro) {
        validarLivro(livro);

        if (livro.getIdLivro() != null) {
            buscarPorId(livro.getIdLivro());
        }

        if (livro.getCategoria() == null
                || livro.getCategoria().getIdCategorias() == null
                || !categoriaRepository.existsById(
                    livro.getCategoria().getIdCategorias())) {
            throw new IllegalArgumentException(
                    "Selecione uma categoria existente.");
        }

        return repository.save(livro);
    }

    public void excluir(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    public Livro atualizarEstoque(Integer id, Integer quantidade) {
        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException(
                    "O estoque não pode ser negativo.");
        }

        Livro livro = buscarPorId(id);
        livro.setEstoque(quantidade);
        return repository.save(livro);
    }

    public List<Livro> listarEstoqueBaixo() {
        return listarTodos().stream()
                .filter(livro -> livro.getEstoque() != null
                        && livro.getEstoque() <= 5)
                .toList();
    }

    private void validarLivro(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título é obrigatório.");
        }

        if (livro.getPreco() == null
                || livro.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Informe um preço válido, maior ou igual a zero.");
        }

        if (livro.getEstoque() == null || livro.getEstoque() < 0) {
            throw new IllegalArgumentException(
                    "O estoque não pode ser negativo.");
        }
    }
}
