package sistema_de_cargas.api.Controller;

import sistema_de_cargas.api.DTO.FaturaRequestDTO;
import sistema_de_cargas.api.DTO.FaturaResponseDTO;
import sistema_de_cargas.api.Service.FaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/faturas")
@CrossOrigin(origins = "*")
public class FaturaController {

    @Autowired
    private FaturaService faturaService;

    @GetMapping
    public ResponseEntity<List<FaturaResponseDTO>> listar() {
        return ResponseEntity.ok(faturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaturaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(faturaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FaturaResponseDTO> gerarFatura(@RequestBody FaturaRequestDTO dto) {
        return ResponseEntity.ok(FaturaResponseDTO.fromEntity(faturaService.gerarFatura(dto)));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<FaturaResponseDTO> pagarFatura(@PathVariable Long id, @RequestParam LocalDate dataPagamento) {
        return ResponseEntity.ok(FaturaResponseDTO.fromEntity(
                faturaService.registrarPagamento(id, dataPagamento)
        ));
    }
}