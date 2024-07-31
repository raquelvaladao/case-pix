package com.example.demo.core.repositories;

import com.example.demo.core.models.Holder;
import com.example.demo.core.models.HolderId;
import com.example.demo.core.repositories.views.HolderKeyCountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HolderRepository extends JpaRepository<Holder, HolderId> {

    @Query(value = "SELECT h.TIPO_PESSOA AS personType, COUNT(p.ID_CHAVE) AS keyCount " +
            "FROM TB_CORRENTISTA h " +
            "LEFT JOIN TB_CHAVE_PIX p " +
            "ON h.NUMERO_AGENCIA = p.NUMERO_AGENCIA AND h.NUMERO_CONTA = p.NUMERO_CONTA " +
            "WHERE h.NUMERO_AGENCIA = :agencyNumber AND h.NUMERO_CONTA = :accountNumber " +
            "GROUP BY h.TIPO_PESSOA", nativeQuery = true)
    HolderKeyCountView getKeysCountAndPersonType(@Param("agencyNumber") String agencyNumber, @Param("accountNumber") String accountNumber);

}
