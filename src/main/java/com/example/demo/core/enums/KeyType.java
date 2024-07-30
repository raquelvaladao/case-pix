package com.example.demo.core.enums;

public enum KeyType {

    RANDOM("aleatorio"),
    EMAIL("email"),
    CNPJ("cnpj"),
    CPF("cpf");

    private final String description;

    KeyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static KeyType fromDescription(String description) {
        for (KeyType keyType : values()) {
            if (keyType.getDescription().equalsIgnoreCase(description)) {
                return keyType;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }

}
