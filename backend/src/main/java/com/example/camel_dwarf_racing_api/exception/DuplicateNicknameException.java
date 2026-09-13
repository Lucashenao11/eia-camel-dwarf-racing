package com.example.camel_dwarf_racing_api.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException(String nickname) {
        super("A competitor with nickname '" + nickname + "' already exists.");
    }
}