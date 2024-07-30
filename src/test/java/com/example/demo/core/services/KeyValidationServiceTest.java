package com.example.demo.core.services;

import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.repositories.HolderRepository;
import com.example.demo.core.repositories.PixKeyRepository;
import com.example.demo.core.repositories.views.HolderKeyCountView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class KeyValidationServiceTest {

    @Mock
    private PixKeyRepository pixKeyRepository;

    @Mock
    private HolderRepository holderRepository;

    @InjectMocks
    private KeyValidationService keyValidationService;

    @Test
    void checkIfKeyExistsTrueSuccessfully() {
        when(pixKeyRepository.countByValue(any())).thenReturn(1);

        Assertions.assertThrows(BusinessException.class, () -> keyValidationService.checkIfKeyExists("abcdKey"));
    }

    @Test
    void checkIfKeyExistsFalseSuccessfully() {
        when(pixKeyRepository.countByValue(any())).thenReturn(0);

        Assertions.assertDoesNotThrow(() -> keyValidationService.checkIfKeyExists("abcdKey"));
    }

    @Test
    void shouldThrowExceptionWhenCheckLimitWithNullHolder() {
        when(holderRepository.getKeysCountAndPersonType(any(), any())).thenReturn(null);

        Assertions.assertThrows(BusinessException.class, () -> keyValidationService.checkIfHolderReachedKeysLimit(12,123123));
    }

    @Test
    void shouldThrowExceptionWhenReachedLimitPF() {
        HolderKeyCountView view = Mockito.mock(HolderKeyCountView.class);
        when(view.getKeyCount()).thenReturn(5L);
        when(view.getPersonType()).thenReturn("PF");

        when(holderRepository.getKeysCountAndPersonType(any(), any())).thenReturn(view);

        Assertions.assertThrows(BusinessException.class, () -> keyValidationService.checkIfHolderReachedKeysLimit(12,123123));
    }


    @Test
    void shouldNotThrowAnyExceptionPF() {
        HolderKeyCountView view = Mockito.mock(HolderKeyCountView.class);
        when(view.getKeyCount()).thenReturn(4L);
        when(view.getPersonType()).thenReturn("PF");

        when(holderRepository.getKeysCountAndPersonType(any(), any())).thenReturn(view);

        Assertions.assertDoesNotThrow(() -> keyValidationService.checkIfHolderReachedKeysLimit(12,123123));
    }
}