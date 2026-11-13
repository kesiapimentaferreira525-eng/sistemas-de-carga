package sistema_de_cargas.api.Repository;


import sistema_de_cargas.api.Model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    Optional<Fatura> findByNumeroFatura(String numeroFatura);
}
