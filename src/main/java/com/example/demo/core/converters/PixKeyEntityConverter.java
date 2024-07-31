package com.example.demo.core.converters;

import com.example.demo.core.models.Holder;
import com.example.demo.core.models.PixKey;
import com.example.demo.dtos.PixKeyRequestDTO;

public class PixKeyEntityConverter {

    public static PixKey convertToEntity(PixKeyRequestDTO request, Holder holder) {
        PixKey entity = new PixKey();
        entity.setKeyType(request.getKeyType());
        entity.setKeyValue(request.getKeyValue());
        entity.setHolder(holder);
        return entity;
    }
}
