package com.example.demo.core.repositories;


import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.models.PixKey;
import com.example.demo.dtos.KeySearchCriteriaRequestDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PixKeySpecification {

    public static Specification<PixKey> keyIdIsLike(String keyId) {
        return (Root<PixKey> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (Strings.isNotBlank(keyId)) {
                return cb.equal(root.get("keyId"), keyId);
            }
            return cb.conjunction();
        };
    }

    public static Specification<PixKey> keyTypeIsLike(String keyType) {
        return (Root<PixKey> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (Strings.isNotBlank(keyType)) {
                return cb.equal(root.get("keyType"), keyType);
            }
            return cb.conjunction();
        };
    }

    public static Specification<PixKey> holderNameIsLike(String holderName) {
        return (Root<PixKey> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (Strings.isNotBlank(holderName)) {
                return cb.equal(root.get("holder").get("holderName"), holderName);
            }
            return cb.conjunction();
        };
    }

    public static Specification<PixKey> accountNumberIsLike(String accountNumber) {
        return (Root<PixKey> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (Strings.isNotBlank(accountNumber)) {
                return cb.equal(root.get("holder").get("holderId").get("accountNumber"), accountNumber);
            }
            return cb.conjunction();
        };
    }

    public static Specification<PixKey> agencyNumberIsLike(String agencyNumber) {
        return (Root<PixKey> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (Strings.isNotBlank(agencyNumber)) {
                return cb.equal(root.get("holder").get("holderId").get("agencyNumber"), agencyNumber);
            }
            return cb.conjunction();
        };
    }

    public static Specification<PixKey> inclusionDateIsLike(String inclusionDate) {
        return (Root<PixKey> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (inclusionDate != null) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate localDate = LocalDate.parse(inclusionDate, formatter);
                    LocalDateTime startOfDay = localDate.atStartOfDay();
                    LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
                    return cb.between(root.get("inclusionDate"), startOfDay, endOfDay);
                } catch (RuntimeException e){
                    throw new BusinessException(ErrorMessage.INVALID_FIELD, "Date should be in the format dd/MM/yyyy");
                }
            }
            return cb.conjunction();
        };
    }

    public static Specification<PixKey> isActive() {
        return (Root<PixKey> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.equal(root.get("inactive"), false);
    }

    public static Specification<PixKey> buildDynamicQueryActiveKeys(KeySearchCriteriaRequestDTO criteria) {
        return Specification.where(keyIdIsLike(criteria.getKeyId()))
                .and(keyTypeIsLike(criteria.getKeyType()))
                .and(holderNameIsLike(criteria.getHolderName()))
                .and(accountNumberIsLike(criteria.getAccountNumber()))
                .and(agencyNumberIsLike(criteria.getAgencyNumber()))
                .and(inclusionDateIsLike(criteria.getInclusionDate()))
                .and(isActive());
    }
}
