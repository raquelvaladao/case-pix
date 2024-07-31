package com.example.demo.core.converters;

import ch.qos.logback.core.util.StringUtil;
import com.example.demo.core.models.PixKey;
import com.example.demo.dtos.PixKeyResponseDTO;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class PixKeyResponseConverter {

    public static PixKeyResponseDTO convertToResponse(PixKey entity, String dateFormat, String defaultValue) {
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
