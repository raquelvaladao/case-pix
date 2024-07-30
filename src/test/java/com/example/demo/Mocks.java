package com.example.demo;

import com.example.demo.core.models.Holder;
import com.example.demo.core.models.HolderId;
import com.example.demo.core.models.PixKey;

public class Mocks {

    public static Holder buildHolderEntity() {
        HolderId id = new HolderId(12,123123);
        Holder entity = new Holder();
        entity.setHolderSurname("Joao");
        entity.setHolderId(id);
        entity.setAccountType("corrente");
        entity.setPersonType("PF");
        return entity;
    }

    public static PixKey buildPixKeyEntity() {
        PixKey pixKey = new PixKey();
        pixKey.setKeyId("abc-abc-abc");
        pixKey.setInactive(false);
        pixKey.setHolder(Mocks.buildHolderEntity());
        return pixKey;
    }
}
