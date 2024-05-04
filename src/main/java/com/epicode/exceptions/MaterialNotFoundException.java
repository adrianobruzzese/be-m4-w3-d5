package com.epicode.exceptions;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(String identifier) {
        super("Materiale con identificativo " + identifier + " non trovato.");
    }
}
