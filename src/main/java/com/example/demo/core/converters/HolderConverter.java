package com.example.demo.core.converters;

import com.example.demo.core.models.Holder;
import com.example.demo.core.models.HolderId;
import com.example.demo.dtos.AccountHolderPostDTO;

public class HolderConverter {

    public static Holder toEntity(AccountHolderPostDTO request) {
        Holder entity = new Holder();
        HolderId id = new HolderId();

        id.setAccountNumber(request.getAccountNumber());
        id.setAgencyNumber(request.getAgencyNumber());
        entity.setHolderId(id);

        entity.setHolderName(request.getHolderName());
        entity.setHolderSurname(request.getHolderSurname());
        entity.setAccountType(request.getAccountType());
        entity.setPersonType(request.getPersonType());

        return entity;
    }
}
