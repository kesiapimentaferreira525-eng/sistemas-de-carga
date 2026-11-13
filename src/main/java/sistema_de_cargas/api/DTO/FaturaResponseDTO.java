package sistema_de_cargas.api.DTO;

import sistema_de_cargas.api.Model.Fatura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FaturaResponseDTO(
        Long id,
        String numeroFatura,
        String cnpjCliente,
        String nomeCliente,
        String enderecoCliente,
        String emailCliente,
        LocalDate dataFatura,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        BigDecimal subtotal,
        BigDecimal desconto,
        BigDecimal totalFaturado,
        String dadosBancarios,
        String agencia,
        String conta,
        String pix,
        String statusPagamento,
        List<CargaResponseDTO> cargas
) {
    public static FaturaResponseDTO fromEntity(Fatura fatura) {
        return new FaturaResponseDTO(
                fatura.getId(),
                fatura.getNumeroFatura(),
                fatura.getCnpjCliente(),
                fatura.getNomeCliente(),
                fatura.getEnderecoCliente(),
                fatura.getEmailCliente(),
                fatura.getDataFatura(),
                fatura.getDataVencimento(),
                fatura.getDataPagamento(),
                fatura.getSubtotal(),
                fatura.getDesconto(),
                fatura.getTotalFaturado(),
                fatura.getDadosBancarios(),
                fatura.getAgencia(),
                fatura.getConta(),
                fatura.getPix(),
                fatura.getStatusPagamento(),
                fatura.getCargas().stream()
                        .map(CargaResponseDTO::fromEntity)
                        .toList()
        );
    }
}
