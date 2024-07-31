package com.example.demo.core.services;


import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.core.models.Holder;
import com.example.demo.core.models.HolderId;
import com.example.demo.core.repositories.HolderRepository;
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

    public Holder findHolderByIdOrElseThrow(Integer agencyNumber, Integer accountNumber) {
        return holderRepository.findById(new HolderId(agencyNumber, accountNumber))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND, "Holder not found"));
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
