package com.example.camel_dwarf_racing_api.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("Username '" + username + "' is already taken.");
    }
}