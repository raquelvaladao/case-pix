package com.example.demo.core.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "TB_CORRENTISTA")
public class Holder {

    @EmbeddedId
    private HolderId holderId;

    @Column(name = "TIPO_CONTA", nullable = false, length = 10)
    private String accountType;

    @Column(name = "TIPO_PESSOA", nullable = false, length = 2)
    private String personType;

    @Column(name = "NOME_CORRENTISTA", nullable = false, length = 30)
    private String holderName;

    @Column(name = "SOBRENOME_CORRENTISTA", length = 45)
    private String holderSurname;

    @OneToMany(mappedBy = "holder", fetch = FetchType.LAZY)
    private Set<PixKey> pixKeys = new HashSet<>();

}
