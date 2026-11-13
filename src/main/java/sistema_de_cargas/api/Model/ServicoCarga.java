package sistema_de_cargas.api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_servico_carga")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoCarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String material; // Rachão, Rachinha, Rachão de Face, Areia, Brita, Pó, Barro

    private String placa; // Ex: JJH7717

    @Column(nullable = false)
    private LocalDate dataServico;

    private String ticket; // Ex: 701

    @Column(nullable = false)
    private String unidade; // Carrada, M³, Tonelada

    @Column(nullable = false)
    private Double quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario; // PU

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal; // Total

    @Column(nullable = false)
    private String cnpjCliente; // Ex: 07.157.925/0001-90

    @Column(nullable = false)
    private String nomeCliente; // Ex: WB CONSTRUTORA

    private LocalDate dataPagamento;

    @Column(nullable = false)
    private String status = "PENDENTE"; // PENDENTE ou PAGO

    @ManyToOne
    @JoinColumn(name = "fatura_id")
    private Fatura fatura; // Relacionamento com a fatura de cobrança
}