package com.example.demo.core.services;

import com.example.demo.Mocks;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.models.Holder;
import com.example.demo.core.models.PixKey;
import com.example.demo.core.repositories.PixKeyRepository;
import com.example.demo.dtos.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PixKeyServiceTest {

    @Mock
    private HolderService holderService;

    @Mock
    private ValidationFacadeService validationFacadeService;

    @Mock
    private PixKeyRepository pixKeyRepository;

    @InjectMocks
    private PixKeyService pixKeyService;

    private KeySearchCriteriaRequestDTO criteria;

    @BeforeEach
    void setUp() {
        criteria = new KeySearchCriteriaRequestDTO();
    }

    @Test
    void includeKeySuccessfully() {
        Holder holder = new Holder();
        PixKey pixKey = new PixKey();
        pixKey.setKeyId(UUID.randomUUID().toString());

        doNothing().when(validationFacadeService).validate(any(), any(), any(), any());
        when(holderService.findHolderById(any(), any())).thenReturn(holder);
        when(pixKeyRepository.saveAndFlush(any())).thenReturn(pixKey);

        PixKeyRequestDTO pixKeyRequestDTO = new PixKeyRequestDTO("abcdaleatorio","aleatorio","corrente",12,123123,"Joao","Silva");
        pixKeyService.includeKey(pixKeyRequestDTO);

        verify(pixKeyRepository).saveAndFlush(any());
    }

    @Test
    void shouldThrowExceptionWhenKeyDoesntExist() {
        when(pixKeyRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> pixKeyService.getActiveKeyById(UUID.randomUUID().toString()));
    }

    @Test
    void shouldThrowExceptionWhenKeyIsInactive() {
        PixKey pixKey = new PixKey();
        pixKey.setInactive(true);
        when(pixKeyRepository.findById(any())).thenReturn(Optional.of(pixKey));

        assertThrows(BusinessException.class, () -> pixKeyService.getActiveKeyById(UUID.randomUUID().toString()));
    }

    @Test
    void shouldFindActiveKeyByIdSuccessfully() {
        PixKey pixKey = Mocks.buildPixKeyEntity();
        when(pixKeyRepository.findById(any())).thenReturn(Optional.of(pixKey));

        PixKey activeKeyById = pixKeyService.getActiveKeyById(UUID.randomUUID().toString());

        assertEquals("abc-abc-abc", activeKeyById.getKeyId());
    }


    @Test
    void deactivateKeySuccessfully() {
        PixKey pixKeyActive = Mocks.buildPixKeyEntity();

        when(pixKeyRepository.findById(any())).thenReturn(Optional.of(pixKeyActive));
        when(pixKeyRepository.save(any())).thenReturn(pixKeyActive);

        assertNotNull(pixKeyService.deactivateKey(new PixKeyIdDTO()).getDeactivationDate());
    }

    @Test
    void shouldNotDeactivateKeyThatIsAlreadyDeactivated() {
        PixKey pixKeyActive = Mocks.buildPixKeyEntity();
        pixKeyActive.setInactive(true);

        when(pixKeyRepository.findById(any())).thenReturn(Optional.of(pixKeyActive));

        assertThrows(BusinessException.class, () -> pixKeyService.deactivateKey(new PixKeyIdDTO()));
    }

    @Test
    void testInvalidCriteria() {
        criteria.setKeyId("xxx-aaa-bbb");
        criteria.setKeyType("cpf");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pixKeyService.filter(criteria);
        });

        assertEquals("If key id is passed, you cannot use other filters", exception.getDetail());
    }

    @Test
    void testSearchByKeyId() {
        criteria.setKeyId("a-b-c-id");
        PixKey pixKey = Mocks.buildPixKeyEntity();

        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.of(pixKey));

        List<PixKeyResponseDTO> result = pixKeyService.filter(criteria);

        assertEquals(1, result.size());
        verify(pixKeyRepository, times(1)).findById("a-b-c-id");
    }

    @Test
    void testDynamicSearchCriteriaNotFound() {
        when(pixKeyRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            pixKeyService.filter(criteria);
        });

        assertNotNull(exception);
    }

    @Test
    void testDynamicSearchSuccessfully() {
        criteria.setHolderName("Name");
        when(pixKeyRepository.findAll(any(Specification.class))).thenReturn(List.of(Mocks.buildPixKeyEntity()));

        List<PixKeyResponseDTO> filter = pixKeyService.filter(criteria);
        assertNotNull(filter);
    }

    @Test
    void shouldEditKeySuccessfully() {
        PixKey entity = Mocks.buildPixKeyEntity();

        when(pixKeyRepository.findById(any())).thenReturn(Optional.of(entity));
        when(holderService.findHolderById(any(), any())).thenReturn(Mocks.buildHolderEntity());
        when(pixKeyRepository.save(any())).thenReturn(entity);

        PixKeyResponseDTO editedKey = pixKeyService.editKey(new EditPixKeyRequestDTO());
        assertNotNull(editedKey);
    }
}