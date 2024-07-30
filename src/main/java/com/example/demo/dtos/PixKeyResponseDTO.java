package com.example.demo.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PixKeyResponseDTO {

    private String keyId;

    private String keyValue;

    private String keyType;

    private String accountType;

    private Integer agencyNumber;

    private Integer accountNumber;

    private String holderName;

    private String holderSurname;

    private String inclusionDate;

    private String deactivationDate;

}
