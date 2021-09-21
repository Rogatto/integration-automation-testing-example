package com.staxrt.tutorial.exception;

public enum ErrorInfo {

    USER_NOT_FOUND("User not found on :: ");

    private final String description;


    ErrorInfo(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
