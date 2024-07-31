package com.example.demo.dtos;


import com.example.demo.core.validators.annotations.NumberDigits;
import com.example.demo.core.validators.annotations.ValidSurname;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EditPixKeyRequestDTO {

    public static final String MUST_MATCH_REGEXP = "must match any value in {regexp}";

    @NotEmpty
    private String keyId;

    @NotEmpty
    @Size(max = 10)
    @Pattern(regexp = "corrente|poupança", flags = Pattern.Flag.CASE_INSENSITIVE, message = MUST_MATCH_REGEXP)
    private String accountType;

    @NumberDigits(max = 4)
    private String agencyNumber;

    @NumberDigits(max = 8)
    private String accountNumber;

    @NotEmpty
    @Size(min = 1, max = 30)
    private String holderName;

    @ValidSurname
    private String holderSurname;
}
