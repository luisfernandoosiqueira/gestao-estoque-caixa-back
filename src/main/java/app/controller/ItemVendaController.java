package app.controller;

import app.dto.ItemVendaRequestDTO;
import app.dto.ItemVendaResponseDTO;
import app.service.ItemVendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/itens-venda")
public class ItemVendaController {

    @Autowired
    private ItemVendaService itemVendaService;

    // ======== CONSULTAS ========

    @GetMapping("/venda/{vendaId}")
    public ResponseEntity<List<ItemVendaResponseDTO>> listarPorVenda(@PathVariable Long vendaId) {
        return ResponseEntity.ok(itemVendaService.listarPorVenda(vendaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemVendaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemVendaService.buscarPorId(id));
    }

    // ======== REGISTRO / REMOÇÃO ========

    @PostMapping("/venda/{vendaId}")
    public ResponseEntity<ItemVendaResponseDTO> salvar(
            @PathVariable Long vendaId,
            @RequestBody @Valid ItemVendaRequestDTO dto) {
        ItemVendaResponseDTO salvo = itemVendaService.salvar(vendaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemVendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
