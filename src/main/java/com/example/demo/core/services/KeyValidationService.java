package com.example.demo.core.services;

import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.repositories.HolderRepository;
import com.example.demo.core.repositories.PixKeyRepository;
import com.example.demo.core.repositories.views.HolderKeyCountView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class KeyValidationService {

    private static final String PF = "PF";
    private static final String PJ = "PJ";
    private static final int MAX_KEYS_PF = 5;
    private static final int MAX_KEYS_PJ = 20;

    private final PixKeyRepository pixKeyRepository;
    private final HolderRepository holderRepository;

    public void checkIfKeyExists(String keyValue) {
        if (pixKeyRepository.countByValue(keyValue) > 0)
            throw new BusinessException(ErrorMessage.DUPLICATE, "Pix key with this value already exists");
    }

    public void checkIfHolderReachedKeysLimit(Integer agencyNumber, Integer accountNumber) {
        HolderKeyCountView holderKeysCount = holderRepository.getKeysCountAndPersonType(agencyNumber, accountNumber);

        if(holderKeysCount == null)
            throw new BusinessException(ErrorMessage.NOT_FOUND, "Account holder not found");

        if (reachedLimit(holderKeysCount, PF, MAX_KEYS_PF) || reachedLimit(holderKeysCount, PJ, MAX_KEYS_PJ))
            throw new BusinessException(ErrorMessage.REACHED_LIMIT, "Holder reached limit for pix keys addition");
    }

    private boolean reachedLimit(HolderKeyCountView view, String keyType, int max){
        return view.getPersonType().equals(keyType) && view.getKeyCount() >= max;
    }
}
