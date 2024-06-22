package org.hplr.core.model;

import org.hplr.exception.HPLRValidationException;

import java.util.Objects;

public class StringValidator {

    static void validateString(String string) throws HPLRValidationException {
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
