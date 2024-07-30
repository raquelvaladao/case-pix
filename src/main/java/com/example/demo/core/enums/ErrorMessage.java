package com.example.demo.core.enums;

public enum ErrorMessage {

    INTERNAL_ERROR(500),
    INVALID_FIELD(422),
    REACHED_LIMIT(422),
    DUPLICATE(422),
    INACTIVE_KEY(422),
    INVALID_CRITERIA(422),
    NOT_FOUND(404);

    private final int status;

    ErrorMessage(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
