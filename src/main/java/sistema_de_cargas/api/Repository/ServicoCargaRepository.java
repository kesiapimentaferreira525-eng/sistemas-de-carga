package sistema_de_cargas.api.Repository;

import sistema_de_cargas.api.Model.ServicoCarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServicoCargaRepository extends JpaRepository<ServicoCarga, Long> {

    // Buscar cargas por CNPJ do cliente
    List<ServicoCarga> findByCnpjCliente(String cnpjCliente);

    // Buscar cargas por intervalo de datas (para faturamento)
    List<ServicoCarga> findByDataServicoBetween(LocalDate inicio, LocalDate fim);

    // Buscar cargas que ainda não foram associadas a nenhuma fatura
    List<ServicoCarga> findByFaturaIsNull();
}