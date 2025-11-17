package app.controller;

import app.dto.MovimentacaoEstoqueRequestDTO;
import app.dto.MovimentacaoEstoqueResponseDTO;
import app.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoEstoqueController {

    @Autowired
    private MovimentacaoEstoqueService movimentacaoService;

    // ======== CONSULTAS ========

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> listarTodas() {
        return ResponseEntity.ok(movimentacaoService.listarTodas());
    }

    @GetMapping("/produto/{id}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> listarPorProduto(@PathVariable Long id) {
        return ResponseEntity.ok(movimentacaoService.listarPorProduto(id));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(movimentacaoService.listarPorTipo(tipo));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> listarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(movimentacaoService.listarPorPeriodo(inicio, fim));
    }

    // ======== REGISTRO ========

    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueResponseDTO> registrar(
            @RequestBody @Valid MovimentacaoEstoqueRequestDTO dto) {
        MovimentacaoEstoqueResponseDTO salvo = movimentacaoService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
