package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeySearchCriteriaRequestDTO {

    private String keyId;
    private String keyType;
    private String holderName;
    private Integer agencyNumber;
    private Integer accountNumber;
    private String inclusionDate;

    public boolean isAnyNonIdFieldPresent(){
        List<Object> nonIdFields = Arrays.asList(
                this.getAgencyNumber(),
                this.getAccountNumber(),
                this.getHolderName(),
                this.getKeyType(),
                this.getInclusionDate()
        );

        return nonIdFields.stream().anyMatch(Objects::nonNull);
    }
}
