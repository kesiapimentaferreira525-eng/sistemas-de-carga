package sistema_de_cargas.api.Service;

import sistema_de_cargas.api.DTO.CargaRequestDTO;
import sistema_de_cargas.api.DTO.CargaResponseDTO;
import sistema_de_cargas.api.Model.ServicoCarga;
import sistema_de_cargas.api.Repository.ServicoCargaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoCargaService {

    @Autowired
    private ServicoCargaRepository repository;

    public CargaResponseDTO salvarCarga(CargaRequestDTO dto) {
        ServicoCarga carga = new ServicoCarga();
        carga.setMaterial(dto.material());
        carga.setPlaca(dto.placa());
        carga.setDataServico(dto.dataServico());
        carga.setTicket(dto.ticket());
        carga.setUnidade(dto.unidade());
        carga.setQuantidade(dto.quantidade());
        carga.setValorUnitario(dto.valorUnitario());

        // Cálculo automático: Quantidade x Valor Unitário
        if (dto.quantidade() != null && dto.valorUnitario() != null) {
            BigDecimal total = dto.valorUnitario().multiply(BigDecimal.valueOf(dto.quantidade()));
            carga.setValorTotal(total);
        } else {
            carga.setValorTotal(BigDecimal.ZERO);
        }

        carga.setCnpjCliente(dto.cnpjCliente());
        carga.setNomeCliente(dto.nomeCliente());
        carga.setStatus("PENDENTE");

        ServicoCarga salva = repository.save(carga);
        return CargaResponseDTO.fromEntity(salva);
    }

    public List<CargaResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(CargaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CargaResponseDTO> listarSemFatura() {
        return repository.findByFaturaIsNull().stream()
                .map(CargaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void deletarCarga(Long id) {
        repository.deleteById(id);
    }
}