package br.com.itau.apisaldotransferencias.infra.database.repository;

import br.com.itau.apisaldotransferencias.infra.database.entity.SaldoContaCorrenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaldoContaCorrenteRepository extends JpaRepository<SaldoContaCorrenteEntity, String> {
    SaldoContaCorrenteEntity findByNumContaCorrente(String numContaCorrente);
}
