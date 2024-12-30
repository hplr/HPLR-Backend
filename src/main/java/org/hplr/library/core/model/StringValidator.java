package org.hplr.library.core.model;

import org.hplr.library.exception.HPLRValidationException;

import java.util.Objects;

public class StringValidator {

    public static void validateString(String string) throws HPLRValidationException {
        if (
                Objects.isNull(string) ||
                        string.isBlank() ||
                        string.isEmpty()
        ) {
            throw new HPLRValidationException("String cannot be empty!");
        }
    }
    private StringValidator(){
        throw new IllegalStateException("Utility class");
    }
}
