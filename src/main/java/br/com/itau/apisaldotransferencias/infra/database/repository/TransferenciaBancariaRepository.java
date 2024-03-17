package br.com.itau.apisaldotransferencias.infra.database.repository;

import br.com.itau.apisaldotransferencias.infra.database.entity.TransferenciaBancariaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaBancariaRepository extends JpaRepository<TransferenciaBancariaEntity, String> {
    TransferenciaBancariaEntity findByCodTransferenciaBancaria(String codTransferenciaBancaria);
}
