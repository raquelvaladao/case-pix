package com.example.demo.core.services;

import ch.qos.logback.core.util.StringUtil;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
        validationFacadeService.validate(request.getKeyType(), request.getKeyValue(), request.getAccountNumber(), request.getAgencyNumber());

        Holder holder = holderService.findHolderByIdOrElseThrow(request.getAgencyNumber(), request.getAccountNumber());
        PixKey entity = buildPixKeyEntity(request, holder);

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

        return buildPixKeyResponse(entity, DD_MM_YYYY_TIME, Strings.EMPTY);
    }

    public PixKey getActiveKeyById(String keyId) {
        PixKey key = findKeyByIdOrElseThrow(keyId);

        if (key.getInactive())
            throw new BusinessException(ErrorMessage.INACTIVE_KEY, "Cannot query inactive key");

        return key;
    }

    private PixKey findKeyByIdOrElseThrow(String keyId) {
        return pixKeyRepository.findById(keyId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND, "Key not found"));
    }

    private PixKey buildPixKeyEntity(PixKeyRequestDTO request, Holder holder) {
        PixKey entity = new PixKey();
        entity.setKeyType(request.getKeyType());
        entity.setKeyValue(request.getKeyValue());

        entity.setHolder(holder);
        return entity;
    }

    public List<PixKeyResponseDTO> filter(KeySearchCriteriaRequestDTO criteria) {
        if (isNotBlank(criteria.getKeyId()) && criteria.isAnyNonIdFieldPresent())
            throw new BusinessException(ErrorMessage.INVALID_CRITERIA, "If key id is passed, you cannot use other filters");

        if (isNotBlank(criteria.getKeyId()))
            return Stream.of(this.getActiveKeyById(criteria.getKeyId()))
                    .map(e -> this.buildPixKeyResponse(e, DD_MM_YYYY, Strings.EMPTY))
                    .collect(Collectors.toList());

        Specification<PixKey> dynamicQuery = PixKeySpecification.buildDynamicQueryActiveKeys(criteria);

        List<PixKey> result = pixKeyRepository.findAll(dynamicQuery);
        if (result.isEmpty())
            throw new EntityNotFoundException();

        return result.stream()
                .map(e -> this.buildPixKeyResponse(e, DD_MM_YYYY, Strings.EMPTY))
                .collect(Collectors.toList());
    }

    public PixKeyResponseDTO editKey(EditPixKeyRequestDTO request) {
        PixKey activeKeyById = this.getActiveKeyById(request.getKeyId());
        Holder newHolder = holderService.findHolderByIdOrElseThrow(request.getAgencyNumber(), request.getAccountNumber());

        log.info("Setting key to holder {} {} ({})", request.getHolderName(), request.getHolderSurname(), request.getAccountType());
        activeKeyById.setHolder(newHolder);
        activeKeyById.setInclusionDate(OffsetDateTime.now());
        pixKeyRepository.save(activeKeyById);

        return buildPixKeyResponse(activeKeyById, DD_MM_YYYY_TIME, null);
    }


    private PixKeyResponseDTO buildPixKeyResponse(PixKey entity, String dateFormat, String defaultValue) {
        DateTimeFormatter df = new DateTimeFormatterBuilder().appendPattern(dateFormat).toFormatter();

        return PixKeyResponseDTO.builder()
                .keyId(entity.getKeyId())
                .keyValue(entity.getKeyValue())
                .keyType(entity.getKeyType())
                .holderName(entity.getHolder().getHolderName())
                .holderSurname(StringUtil.nullStringToEmpty(entity.getHolder().getHolderSurname()))
                .agencyNumber(entity.getHolder().getHolderId().getAgencyNumber())
                .accountNumber(entity.getHolder().getHolderId().getAccountNumber())
                .deactivationDate(entity.getDeactivationDate() == null ? defaultValue : df.format(entity.getDeactivationDate()))
                .inclusionDate(df.format(entity.getInclusionDate()))
                .build();
    }
}
