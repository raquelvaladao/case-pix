package com.example.demo.core.services;


import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.models.Holder;
import com.example.demo.core.models.HolderId;
import com.example.demo.core.repositories.HolderRepository;
import com.example.demo.core.repositories.views.HolderKeyCountView;
import com.example.demo.dtos.AccountHolderPostDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class HolderService {

    private static final String PF = "PF";
    private static final String PJ = "PJ";
    private static final int MAX_KEYS_PF = 5;
    private static final int MAX_KEYS_PJ = 20;

    private final HolderRepository holderRepository;
    private final ModelMapper mapper;

    public void createHolder(AccountHolderPostDTO holderRequest) {
        Optional<Holder> holder = holderRepository.findById(new HolderId(holderRequest.getAgencyNumber(), holderRequest.getAccountNumber()));
        if (holder.isPresent())
            throw new BusinessException(ErrorMessage.DUPLICATE, "Holder with this account already exists");

        log.info("Saving account holder data with holder name {} {}", holderRequest.getHolderName(), holderRequest.getHolderSurname());

        Holder entity = toEntity(holderRequest);

        holderRepository.save(entity);

    }

    public void checkIfHolderReachedKeysLimit(String agencyNumber, String accountNumber) {
        HolderKeyCountView holderKeysCount = holderRepository.getKeysCountAndPersonType(agencyNumber, accountNumber);

        if(holderKeysCount == null)
            throw new BusinessException(ErrorMessage.NOT_FOUND, "Account holder not found");

        if (reachedLimit(holderKeysCount, PF, MAX_KEYS_PF) || reachedLimit(holderKeysCount, PJ, MAX_KEYS_PJ))
            throw new BusinessException(ErrorMessage.REACHED_LIMIT, "Holder reached limit for pix keys addition");
    }

    public Holder findHolderByIdOrElseThrow(String agencyNumber, String accountNumber) {
        return holderRepository.findById(new HolderId(agencyNumber, accountNumber))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND, "Holder not found"));
    }

    private boolean reachedLimit(HolderKeyCountView view, String keyType, int max){
        return view.getPersonType().equals(keyType) && view.getKeyCount() >= max;
    }

    private Holder toEntity(AccountHolderPostDTO request) {
        Holder entity = mapper.map(request, Holder.class);
        HolderId id = new HolderId();

        id.setAccountNumber(request.getAccountNumber());
        id.setAgencyNumber(request.getAgencyNumber());
        entity.setHolderId(id);
        return entity;
    }
}
