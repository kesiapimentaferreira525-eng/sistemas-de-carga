package sistema_de_cargas.api.DTO;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FaturaRequestDTO(
        String numeroFatura,
        String cnpjCliente,
        String nomeCliente,
        String enderecoCliente,
        String emailCliente,
        LocalDate dataFatura,
        LocalDate dataVencimento,
        BigDecimal desconto,
        String dadosBancarios,
        String agencia,
        String conta,
        String pix,
        List<Long> idsCargas // IDs das viagens que entram nesta fatura
) {}
