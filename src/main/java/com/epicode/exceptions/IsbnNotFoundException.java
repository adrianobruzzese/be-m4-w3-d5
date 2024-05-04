package com.epicode.exceptions;

public class IsbnNotFoundException extends RuntimeException {
    public IsbnNotFoundException(String isbn) {
        super("Materiale con ISBN " + isbn + " non trovato.");
    }
}
