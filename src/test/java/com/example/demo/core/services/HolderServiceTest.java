package com.example.demo.core.services;

import com.example.demo.Mocks;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.models.Holder;
import com.example.demo.core.repositories.HolderRepository;
import com.example.demo.dtos.AccountHolderPostDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HolderServiceTest {

    @Mock
    private HolderRepository holderRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private HolderService holderService;

    @Test
    void createHolderSuccessfully() {
        Holder entity = Mocks.buildHolderEntity();

        when(mapper.map(any(), any())).thenReturn(entity);
        when(holderRepository.save(any())).thenReturn(entity);

        holderService.createHolder(new AccountHolderPostDTO());

        verify(holderRepository).save(entity);
    }

    @Test
    void findHolderSuccessfully(){
        when(holderRepository.findById(any())).thenReturn(Optional.of(new Holder()));

        Holder holderById = holderService.findHolderByIdOrElseThrow("12", "123123");

        assertNotNull(holderById);
    }

    @Test
    void shouldNotFindHolder(){
        when(holderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> holderService.findHolderByIdOrElseThrow("12", "123123"));
    }


    @Test
    void shouldFindDuplicateHolderWhenCreating(){
        when(holderRepository.findById(any())).thenReturn(Optional.of(Mocks.buildHolderEntity()));

        assertThrows(BusinessException.class, () -> holderService.createHolder(new AccountHolderPostDTO()));
    }

    @Test
    void shouldThrowExceptionWhenCreateHolder() {
        Holder entity = Mocks.buildHolderEntity();

        when(mapper.map(any(), any())).thenReturn(entity);
        doThrow(RuntimeException.class).when(holderRepository).save(any());

        assertThrows(RuntimeException.class, () -> holderService.createHolder(new AccountHolderPostDTO()));
    }

}