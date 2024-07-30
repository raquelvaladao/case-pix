package com.example.demo.core.services;

import com.example.demo.core.exceptions.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;


@ExtendWith(MockitoExtension.class)
class ValidationFacadeServiceTest {

    @Mock
    private KeyValidationService keyValidationService;

    @InjectMocks
    private ValidationFacadeService validationFacadeService;

    @ParameterizedTest
    @MethodSource(value = "validPairs")
    void validatePairsSuccessfully(String keyType, String keyValue) {
        doNothing().when(keyValidationService).checkIfKeyExists(any());
        doNothing().when(keyValidationService).checkIfHolderReachedKeysLimit(any(), any());

        Assertions.assertDoesNotThrow(() -> validationFacadeService.validate(keyType, keyValue, 123123, 12));
    }

    @ParameterizedTest
    @MethodSource(value = "invalidPairs")
    void validateInvalidPairsSuccessfully(String keyType, String keyValue) {
        doNothing().when(keyValidationService).checkIfKeyExists(any());
        doNothing().when(keyValidationService).checkIfHolderReachedKeysLimit(any(), any());

        Assertions.assertThrows(BusinessException.class,
                () -> validationFacadeService.validate(keyType, keyValue, 123123, 12));
    }

    private static Stream<Arguments> validPairs() {
        return Stream.of(
                Arguments.of("email", "with@gmail.com"),
                Arguments.of("cpf", "92220802035"),
                Arguments.of("cnpj", "58749503000144"),
                Arguments.of("aleatorio", "134lakdfma2")
        );
    }

    private static Stream<Arguments> invalidPairs() {
        return Stream.of(
                Arguments.of("email", "withoutAt.com"),
                Arguments.of("email", "withAt@notAlph$a.com"),
                Arguments.of("cpf", "11111111111"),
                Arguments.of("cpf", "22222222222"),
                Arguments.of("cpf", "33333333333"),
                Arguments.of("cpf", "44444444444"),
                Arguments.of("cpf", "55555555555"),
                Arguments.of("cpf", "66666666666"),
                Arguments.of("cpf", "77777777777"),
                Arguments.of("cpf", "88888888888"),
                Arguments.of("cpf", "99999999999"),
                Arguments.of("cpf", "12345678912"),
                Arguments.of("cpf", "623.325.930-47"),
                Arguments.of("cnpj", "19.152.315/0001-82"),
                Arguments.of("cnpj", "123456789128989"),
                Arguments.of("aleatorio", "maisde36caracteresaaaaabbbbbbbbbccccccccddd"),
                Arguments.of("aleatorio", "Not%Alphnumeric$")
        );
    }

}