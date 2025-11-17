package app.controller;

import app.dto.VendaRequestDTO;
import app.dto.VendaResponseDTO;
import app.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    // ======== CONSULTAS ========

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<VendaResponseDTO>> listarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.listarPorUsuario(id));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<VendaResponseDTO>> listarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(vendaService.listarPorPeriodo(inicio, fim));
    }

    // ======== REGISTRO ========

    @PostMapping
    public ResponseEntity<VendaResponseDTO> registrar(@RequestBody @Valid VendaRequestDTO dto) {
        VendaResponseDTO salvo = vendaService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
