package com.vacancy.exceptions;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidationException extends Exception {

    @RequiredArgsConstructor
    public enum Type {
        KJHKJ("kldf"),
        KJHK234J("kldf"),
        KJHKJ234("kldf");
        private final String description;
    }

    private final Type type;
}
