package sistema_de_cargas.api.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CargaRequestDTO(
        String material,
        String placa,
        LocalDate dataServico,
        String ticket,
        String unidade,
        Double quantidade,
        BigDecimal valorUnitario,
        String cnpjCliente,
        String nomeCliente
) {}