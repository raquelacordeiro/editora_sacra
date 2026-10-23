package br.com.curia.controller;

import br.com.curia.model.Categoria;
import br.com.curia.repository.CategoriaRepository;
import java.util.List;
import br.com.curia.model.Livro;
import br.com.curia.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroService service;
    private final CategoriaRepository categoriaRepository;

    public LivroController(
            LivroService service,
            CategoriaRepository categoriaRepository) {
        this.service = service;
        this.categoriaRepository = categoriaRepository; }

    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        model.addAttribute("livros", service.buscar(busca));
        model.addAttribute("busca", busca == null ? "" : busca);
        return "livros";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Livro livro = new Livro();
        livro.setEstoque(0);
        livro.setPreco(java.math.BigDecimal.ZERO);

        model.addAttribute("livro", livro);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("tituloPagina", "Cadastrar livro");

        return "formulario-livro";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("livro", service.buscarPorId(id));
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("tituloPagina", "Editar livro");

        return "formulario-livro";
    }


    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("livro") Livro livro,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute(
                    "tituloPagina",
                    livro.getIdLivro() == null ? "Cadastrar livro" : "Editar livro"
            );
            return "formulario-livro";
        }

        try {
            service.salvar(livro);
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Livro salvo com sucesso!"
            );
            return "redirect:/livros";
        } catch (IllegalArgumentException e) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("tituloPagina",
                    livro.getIdLivro() == null ? "Cadastrar livro" : "Editar livro");
            model.addAttribute("erro", e.getMessage());
            return "formulario-livro";
        }
    }


    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Livro excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/livros";
    }

    @PostMapping("/estoque/{id}")
    public String atualizarEstoque(@PathVariable Integer id, @RequestParam Integer quantidade, RedirectAttributes redirectAttributes) {
        try {
            service.atualizarEstoque(id, quantidade);
            redirectAttributes.addFlashAttribute("sucesso", "Estoque atualizado!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/livros";
    }
}
