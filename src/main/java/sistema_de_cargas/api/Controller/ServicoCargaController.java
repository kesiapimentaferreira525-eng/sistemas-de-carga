package sistema_de_cargas.api.Controller;

import sistema_de_cargas.api.DTO.CargaRequestDTO;
import sistema_de_cargas.api.DTO.CargaResponseDTO;
import sistema_de_cargas.api.Service.ServicoCargaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargas")
@CrossOrigin(origins = "*")
public class ServicoCargaController {

    @Autowired
    private ServicoCargaService service;

    @PostMapping
    public ResponseEntity<CargaResponseDTO> criar(@RequestBody CargaRequestDTO dto) {
        CargaResponseDTO novaCarga = service.salvarCarga(dto);
        return ResponseEntity.ok(novaCarga);
    }

    @GetMapping
    public ResponseEntity<List<CargaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<CargaResponseDTO>> listarSemFatura() {
        return ResponseEntity.ok(service.listarSemFatura());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarCarga(id);
        return ResponseEntity.noContent().build();
    }
}