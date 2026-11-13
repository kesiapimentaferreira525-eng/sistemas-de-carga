package sistema_de_cargas.api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_fatura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroFatura; // Ex: PERIODO: 1

    @Column(nullable = false)
    private String cnpjCliente;

    @Column(nullable = false)
    private String nomeCliente;

    private String enderecoCliente;
    private String emailCliente;

    @Column(nullable = false)
    private LocalDate dataFatura;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalFaturado;

    @Column(columnDefinition = "TEXT")
    private String dadosBancarios; // Itaú, Agência, Conta, PIX

    @Column(length = 100)
    private String agencia;

    @Column(length = 100)
    private String conta;

    @Column(length = 255)
    private String pix;

    @Column(nullable = false)
    private String statusPagamento = "PENDENTE";

    @OneToMany(mappedBy = "fatura", cascade = CascadeType.ALL)
    private List<ServicoCarga> cargas = new ArrayList<>();
}
