package com.example.demo.core.repositories;

import com.example.demo.core.models.PixKey;
import com.example.demo.dtos.KeySearchCriteriaRequestDTO;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PixKeySpecificationTest {

    @Test
    public void testBuildDynamicQuery() {
        KeySearchCriteriaRequestDTO criteria = new KeySearchCriteriaRequestDTO();
        criteria.setKeyId("key123");
        criteria.setKeyType("aleatorio");
        criteria.setHolderName("Joao");
        criteria.setAccountNumber(123123);
        criteria.setAgencyNumber(12);
        criteria.setInclusionDate(LocalDate.now().toString());

        Specification<PixKey> specification = PixKeySpecification.buildDynamicQueryActiveKeys(criteria);

        assertNotNull(specification);
    }

    @Test
    public void testKeyIdIsLikeNotBlank() {
        String keyId = "123";
        Root<PixKey> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Path<String> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(root.get("keyId")).thenReturn((Path) path);
        Mockito.when(cb.equal(path, keyId)).thenReturn(predicate);

        Specification<PixKey> specification = PixKeySpecification.keyIdIsLike(keyId);
        Predicate resultPredicate = specification.toPredicate(root, query, cb);

        assertNotNull(resultPredicate);
    }

    @Test
    public void testKeyIdIsLikeBlank() {
        String keyId = "";
        Root<PixKey> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(cb.conjunction()).thenReturn(predicate);

        Specification<PixKey> specification = PixKeySpecification.keyIdIsLike(keyId);
        Predicate resultPredicate = specification.toPredicate(root, query, cb);

        assertNotNull(resultPredicate);
        assertEquals(resultPredicate, cb.conjunction());
    }

    @Test
    public void testKeyTypeIsLikeBlank() {
        String type = "";
        Root<PixKey> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(cb.conjunction()).thenReturn(predicate);

        Specification<PixKey> specification = PixKeySpecification.keyTypeIsLike(type);
        Predicate resultPredicate = specification.toPredicate(root, query, cb);

        assertNotNull(resultPredicate);
        assertEquals(resultPredicate, cb.conjunction());
    }

    @Test
    public void testKeyTypeIsLikeNotBlank() {
        String type = "123";
        Root<PixKey> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Path<String> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(root.get("keyType")).thenReturn((Path) path);
        Mockito.when(cb.equal(path, type)).thenReturn(predicate);

        Specification<PixKey> specification = PixKeySpecification.keyTypeIsLike(type);
        Predicate resultPredicate = specification.toPredicate(root, query, cb);

        assertNotNull(resultPredicate);
    }

    @Test
    public void testHolderNameIsLike_NotBlank() {
        // Arrange
        String holderName = "testHolder";
        Root<PixKey> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Path<String> holderNamePath = Mockito.mock(Path.class);
        Path<?> holderPath = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(root.get("holder")).thenReturn((Path) holderPath);
        Mockito.when(holderPath.get("holderName")).thenReturn((Path) holderNamePath);
        Mockito.when(cb.equal(holderNamePath, holderName)).thenReturn(predicate);

        Specification<PixKey> specification = PixKeySpecification.holderNameIsLike(holderName);
        Predicate resultPredicate = specification.toPredicate(root, query, cb);

        assertNotNull(resultPredicate);
    }

    @Test
    public void testAccountNumberIsLikeNotBlank() {
        Integer accountNumber = 123;
        Root<PixKey> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Path<Integer> accountNumberPath = Mockito.mock(Path.class);
        Path<?> holderPath = Mockito.mock(Path.class);
        Path<?> holderIdPath = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(root.get("holder")).thenReturn((Path)holderPath);
        Mockito.when(holderPath.get("holderId")).thenReturn((Path)holderIdPath);
        Mockito.when(holderIdPath.get("accountNumber")).thenReturn((Path)accountNumberPath);
        Mockito.when(cb.equal(accountNumberPath, accountNumber)).thenReturn(predicate);

        Specification<PixKey> specification = PixKeySpecification.accountNumberIsLike(accountNumber);
        Predicate resultPredicate = specification.toPredicate(root, query, cb);

        assertNotNull(resultPredicate);
    }

    @Test
    public void testAgencyNumberIsLikeNotBlank() {
        Integer agencyNumber = 123;
        Root<PixKey> root = Mockito.mock(Root.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        Path<Integer> accountNumberPath = Mockito.mock(Path.class);
        Path<?> holderPath = Mockito.mock(Path.class);
        Path<?> holderIdPath = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(root.get("holder")).thenReturn((Path)holderPath);
        Mockito.when(holderPath.get("holderId")).thenReturn((Path)holderIdPath);
        Mockito.when(holderIdPath.get("agencyNumber")).thenReturn((Path)accountNumberPath);
        Mockito.when(cb.equal(accountNumberPath, agencyNumber)).thenReturn(predicate);

        Specification<PixKey> specification = PixKeySpecification.agencyNumberIsLike(agencyNumber);
        Predicate resultPredicate = specification.toPredicate(root, query, cb);

        assertNotNull(resultPredicate);
    }
}