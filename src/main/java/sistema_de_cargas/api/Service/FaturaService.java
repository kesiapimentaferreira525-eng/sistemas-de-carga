package sistema_de_cargas.api.Service;


import sistema_de_cargas.api.DTO.FaturaRequestDTO;
import sistema_de_cargas.api.DTO.FaturaResponseDTO;
import sistema_de_cargas.api.Model.Fatura;
import sistema_de_cargas.api.Model.ServicoCarga;
import sistema_de_cargas.api.Repository.FaturaRepository;
import sistema_de_cargas.api.Repository.ServicoCargaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaturaService {

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private ServicoCargaRepository cargaRepository;

    @Transactional(readOnly = true)
    public List<FaturaResponseDTO> listarTodas() {
        return faturaRepository.findAll().stream()
                .map(FaturaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FaturaResponseDTO buscarPorId(Long id) {
        Fatura fatura = faturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fatura não encontrada"));
        return FaturaResponseDTO.fromEntity(fatura);
    }

    @Transactional
    public Fatura gerarFatura(FaturaRequestDTO dto) {
        Fatura fatura = new Fatura();
        fatura.setNumeroFatura(dto.numeroFatura());
        fatura.setCnpjCliente(dto.cnpjCliente());
        fatura.setNomeCliente(dto.nomeCliente());
        fatura.setEnderecoCliente(dto.enderecoCliente());
        fatura.setEmailCliente(dto.emailCliente());
        fatura.setDataFatura(dto.dataFatura());
        fatura.setDataVencimento(dto.dataVencimento());
        fatura.setDadosBancarios(dto.dadosBancarios());
        fatura.setAgencia(dto.agencia());
        fatura.setConta(dto.conta());
        fatura.setPix(dto.pix());
        fatura.setDesconto(dto.desconto() != null ? dto.desconto() : BigDecimal.ZERO);

        // Busca as cargas vinculadas a esta fatura pelos IDs informados
        List<ServicoCarga> cargas = cargaRepository.findAllById(dto.idsCargas());

        BigDecimal subtotal = cargas.stream()
                .map(ServicoCarga::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        fatura.setSubtotal(subtotal);
        fatura.setTotalFaturado(subtotal.subtract(fatura.getDesconto()));
        fatura.setStatusPagamento("PENDENTE");

        // Associa as cargas à fatura
        for (ServicoCarga carga : cargas) {
            carga.setFatura(fatura);
        }
        fatura.setCargas(cargas);

        return faturaRepository.save(fatura);
    }

    @Transactional
    public Fatura registrarPagamento(Long faturaId, LocalDate dataPagamento) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        fatura.setStatusPagamento("PAGO");
        fatura.setDataPagamento(dataPagamento);

        for (ServicoCarga carga : fatura.getCargas()) {
            carga.setStatus("PAGO");
            carga.setDataPagamento(dataPagamento);
        }

        return faturaRepository.save(fatura);
    }
}
