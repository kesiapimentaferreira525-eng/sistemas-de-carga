package sistema_de_cargas.api.DTO;

import sistema_de_cargas.api.Model.ServicoCarga;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CargaResponseDTO(
        Long id,
        String material,
        String placa,
        LocalDate dataServico,
        String ticket,
        String unidade,
        Double quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        String cnpjCliente,
        String nomeCliente,
        LocalDate dataPagamento,
        String status
) {
    public static CargaResponseDTO fromEntity(ServicoCarga entity) {
        return new CargaResponseDTO(
                entity.getId(),
                entity.getMaterial(),
                entity.getPlaca(),
                entity.getDataServico(),
                entity.getTicket(),
                entity.getUnidade(),
                entity.getQuantidade(),
                entity.getValorUnitario(),
                entity.getValorTotal(),
                entity.getCnpjCliente(),
                entity.getNomeCliente(),
                entity.getDataPagamento(),
                entity.getStatus()
        );
    }
}