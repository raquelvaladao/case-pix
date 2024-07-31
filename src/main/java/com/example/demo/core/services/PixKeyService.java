package com.example.demo.core.services;

import com.example.demo.core.converters.PixKeyEntityConverter;
import com.example.demo.core.converters.PixKeyResponseConverter;
import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.models.Holder;
import com.example.demo.core.models.PixKey;
import com.example.demo.core.repositories.PixKeyRepository;
import com.example.demo.core.repositories.PixKeySpecification;
import com.example.demo.dtos.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Service
@AllArgsConstructor
public class PixKeyService {

    private static final String DD_MM_YYYY = "dd/MM/yyyy";
    private static final String DD_MM_YYYY_TIME = "dd/MM/yyyy hh:MM:ss";

    private ValidationFacadeService validationFacadeService;
    private HolderService holderService;
    private PixKeyRepository pixKeyRepository;

    public PixKeyIdDTO includeKey(PixKeyRequestDTO request) {
        this.checkIfKeyExistsThenThrow(request.getKeyValue());
        holderService.checkIfHolderReachedKeysLimit(request.getAgencyNumber(), request.getAccountNumber());

        validationFacadeService.validate(request.getKeyType(), request.getKeyValue());

        Holder holder = holderService.findHolderByIdOrElseThrow(request.getAgencyNumber(), request.getAccountNumber());
        PixKey entity = PixKeyEntityConverter.convertToEntity(request, holder);

        log.info("Saving key to holder {} {}", request.getHolderName(), request.getHolderSurname());
        pixKeyRepository.saveAndFlush(entity);

        log.info("Saved pix key with type {}", request.getKeyType());

        return PixKeyIdDTO.builder()
                .keyId(entity.getKeyId())
                .build();
    }

    public PixKeyResponseDTO deactivateKey(PixKeyIdDTO request) {
        PixKey entity = findKeyByIdOrElseThrow(request.getKeyId());

        if (entity.getInactive())
            throw new BusinessException(ErrorMessage.DUPLICATE, "Key is already deactivated");

        entity.setDeactivationDate(OffsetDateTime.now());
        entity.setInactive(true);

        pixKeyRepository.save(entity);
        log.info("Key deactivated successfully");

        return PixKeyResponseConverter.convertToResponse(entity, DD_MM_YYYY_TIME, Strings.EMPTY);
    }

    public PixKey getActiveKeyById(String keyId) {
        PixKey key = findKeyByIdOrElseThrow(keyId);

        if (key.getInactive())
            throw new BusinessException(ErrorMessage.INACTIVE_KEY, "Cannot query inactive key");

        return key;
    }

    public void checkIfKeyExistsThenThrow(String keyValue) {
        if (pixKeyRepository.countByValue(keyValue) > 0)
            throw new BusinessException(ErrorMessage.DUPLICATE, "Pix key with this value already exists");
    }

    private PixKey findKeyByIdOrElseThrow(String keyId) {
        return pixKeyRepository.findById(keyId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND, "Key not found"));
    }

    public List<PixKeyResponseDTO> filter(KeySearchCriteriaRequestDTO criteria) {
        if (isNotBlank(criteria.getKeyId()) && criteria.checkIsAnyNonIdFieldPresent())
            throw new BusinessException(ErrorMessage.INVALID_CRITERIA, "If key id is passed, you cannot use other filters");

        if (isNotBlank(criteria.getKeyId()))
            return Stream.of(this.getActiveKeyById(criteria.getKeyId()))
                    .map(e -> PixKeyResponseConverter.convertToResponse(e, DD_MM_YYYY, Strings.EMPTY))
                    .collect(Collectors.toList());

        Specification<PixKey> dynamicQuery = PixKeySpecification.buildDynamicQueryActiveKeys(criteria);

        List<PixKey> result = pixKeyRepository.findAll(dynamicQuery);
        if (result.isEmpty())
            throw new EntityNotFoundException();

        return result.stream()
                .map(e -> PixKeyResponseConverter.convertToResponse(e, DD_MM_YYYY, Strings.EMPTY))
                .collect(Collectors.toList());
    }

    public PixKeyResponseDTO editKey(EditPixKeyRequestDTO request) {
        PixKey activeKeyById = this.getActiveKeyById(request.getKeyId());
        Holder newHolder = holderService.findHolderByIdOrElseThrow(request.getAgencyNumber(), request.getAccountNumber());

        if(activeKeyById.getHolder().getHolderId() == newHolder.getHolderId())
            return PixKeyResponseConverter.convertToResponse(activeKeyById, DD_MM_YYYY_TIME, null);

        holderService.checkIfHolderReachedKeysLimit(request.getAgencyNumber(), request.getAccountNumber());

        log.info("Setting key to holder {} {} ({})", request.getHolderName(), request.getHolderSurname(), request.getAccountType());
        activeKeyById.setHolder(newHolder);
        activeKeyById.setInclusionDate(OffsetDateTime.now());
        pixKeyRepository.save(activeKeyById);

        return PixKeyResponseConverter.convertToResponse(activeKeyById, DD_MM_YYYY_TIME, null);
    }

}
