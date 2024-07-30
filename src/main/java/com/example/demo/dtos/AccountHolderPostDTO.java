package com.example.demo.dtos;


import com.example.demo.core.validators.annotations.NumberDigits;
import com.example.demo.core.validators.annotations.ValidSurname;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountHolderPostDTO {

    public static final String MUST_MATCH_REGEXP = "must match any value in {regexp}";

    @NotEmpty
    @Size(max = 10)
    @Pattern(regexp = "corrente|poupanca", flags = Pattern.Flag.CASE_INSENSITIVE, message = MUST_MATCH_REGEXP)
    private String accountType;

    @NumberDigits(max = 4)
    private Integer agencyNumber;

    @NumberDigits(max = 8)
    private Integer accountNumber;

    @NotEmpty
    @Size(max = 30)
    private String holderName;

    @NotEmpty
    @Pattern(regexp = "PF|PJ", flags = Pattern.Flag.CASE_INSENSITIVE, message = MUST_MATCH_REGEXP)
    private String personType;

    @ValidSurname
    private String holderSurname;
}
