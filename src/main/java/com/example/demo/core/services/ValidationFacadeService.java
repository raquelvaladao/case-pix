package com.example.demo.core.services;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.enums.KeyType;
import com.example.demo.core.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class ValidationFacadeService {

    public static final int MAX_RANDOM_KEY_SIZE = 36;

    public void validate(String keyType, String keyValue) {
        KeyType type = KeyType.fromDescription(keyType);

        switch (type) {
            case EMAIL -> validateEmail(keyValue);
            case CPF -> validateCPF(keyValue);
            case CNPJ -> validateCNPJ(keyValue);
            case RANDOM -> validateRandomKey(keyValue);
        }
    }

    private void validateEmail(String email) {
        if(!email.contains("@"))
            throw new BusinessException(ErrorMessage.INVALID_FIELD, "Email must contain '@' symbol");

        String alphaNumericAtPattern = "^[A-Za-z0-9]+@[A-Za-z0-9.]+$";
        if (!email.matches(alphaNumericAtPattern)) {
            throw new BusinessException(ErrorMessage.INVALID_FIELD, "Email must contain only alphanumeric characters and '@' in between");
        }
    }

    private void validateCPF(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        checkNumericDigits(cpf);

        try {
            cpfValidator.assertValid(cpf);
        } catch (InvalidStateException e){
            String errorMesssage = cpfValidator.invalidMessagesFor(cpf).get(0).getMessage();
            throw new BusinessException(ErrorMessage.INVALID_FIELD, "Invalid CPF for pix key: ".concat(errorMesssage));
        }
    }

    private void validateCNPJ(String cnpj) {
        CNPJValidator cnpjValidator = new CNPJValidator();
        checkNumericDigits(cnpj);

        try {
            cnpjValidator.assertValid(cnpj);
        } catch (InvalidStateException e){
            String errorMesssage = cnpjValidator.invalidMessagesFor(cnpj).get(0).getMessage();
            throw new BusinessException(ErrorMessage.INVALID_FIELD, "Invalid pix key CNPJ: ".concat(errorMesssage));
        }
    }

    private void validateRandomKey(String randomKey) {
        if(randomKey.length() > MAX_RANDOM_KEY_SIZE){
            throw new BusinessException(ErrorMessage.INVALID_FIELD, "Max random pix key size is ".concat(String.valueOf(MAX_RANDOM_KEY_SIZE)));
        }
        if(!randomKey.matches("[A-Za-z0-9]+")){
            throw new BusinessException(ErrorMessage.INVALID_FIELD, "Strictly alphanumeric characters allowed");
        }
    }

    private void checkNumericDigits(String numericStr) {
        if(!numericStr.matches("[0-9]+"))
            throw new BusinessException(ErrorMessage.INVALID_FIELD, "Strictly numeric characters allowed");
    }
}

