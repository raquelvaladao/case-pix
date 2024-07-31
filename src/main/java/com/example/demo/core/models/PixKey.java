package com.example.demo.core.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@Table(name = "TB_CHAVE_PIX")
public class PixKey {

    @Id
    @UuidGenerator
    @Column(name = "ID_CHAVE", updatable = false, nullable = false)
    private String keyId;

    @Column(name = "TIPO_CHAVE", nullable = false, length = 9)
    private String keyType;

    @Column(name = "VALOR_CHAVE", nullable = false, length = 77)
    private String keyValue;

    @Column(name = "INATIVO", nullable = false)
    private Boolean inactive = false;

    @Column(name = "HORA_INCLUSAO", nullable = false)
    private OffsetDateTime inclusionDate = OffsetDateTime.now();

    @Column(name = "HORA_INATIVACAO")
    private OffsetDateTime deactivationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "NUMERO_AGENCIA", referencedColumnName = "NUMERO_AGENCIA"),
            @JoinColumn(name = "NUMERO_CONTA", referencedColumnName = "NUMERO_CONTA")
    })
    private Holder holder;
}
