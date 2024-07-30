package com.example.demo.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HolderId implements Serializable {

    @Column(name = "NUMERO_AGENCIA")
    private Integer agencyNumber;

    @Column(name = "NUMERO_CONTA")
    private Integer accountNumber;
}
