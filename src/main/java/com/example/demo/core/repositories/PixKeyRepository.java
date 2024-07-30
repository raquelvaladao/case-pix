package com.example.demo.core.repositories;

import com.example.demo.core.models.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PixKeyRepository extends JpaRepository<PixKey, String>, JpaSpecificationExecutor<PixKey> {

    @Query("SELECT COUNT(p) FROM PixKey p WHERE p.keyValue = :value")
    int countByValue(@Param("value") String value);
}
