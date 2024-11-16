package io.github.venkat1701.yugaantar.commons.exceptions;

public enum ExceptionEnum {
    UsernameNotFoundException("It may so happen that the User with the corresponding username is not found");

    private String description;
    ExceptionEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
