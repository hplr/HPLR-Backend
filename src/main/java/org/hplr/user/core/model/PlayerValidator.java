package org.hplr.user.core.model;

import org.hplr.library.core.model.StringValidator;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.library.core.util.ConstValues;

import java.util.Objects;

public class PlayerValidator {

    public static void validatePlayer(Player player) throws HPLRValidationException {
        validateUserData(player);
        validatePlayerRanking(player);
        validatePlayerSecurity(player);
    }

    static void validateUserData(Player player) throws HPLRValidationException{
        validateName(player);
        validateNickname(player);
        validateEmail(player);
    }

    static void validatePlayerRanking(Player player) throws HPLRValidationException {
        validateScore(player);
    }

    static void validatePlayerSecurity(Player player) throws HPLRValidationException {
        validatePwHash(player);
        validateRegistrationTime(player);
    }

    static void validateName(Player player) throws HPLRValidationException {
        try {
            StringValidator.validateString(player.getUserData().name());
        }
         catch (HPLRValidationException ex){
            throw new HPLRValidationException("Name cannot be empty!");
        }

        if (
                player.getUserData().name().length() < ConstValues.MIN_NAME_LENGTH ||
                        player.getUserData().name().length() > ConstValues.MAX_NAME_LENGTH
        ) {
            throw new HPLRValidationException("Name must be between 2 and 12 characters");
        }
    }

    static void validateNickname(Player player) throws HPLRValidationException {
        try {
            StringValidator.validateString(player.getUserData().nickname());
        }
        catch (HPLRValidationException ex){
            throw new HPLRValidationException("Nickname cannot be empty!");
        }
        if (
                player.getUserData().nickname().length() < ConstValues.MIN_NAME_LENGTH ||
                        player.getUserData().nickname().length() > ConstValues.MAX_NAME_LENGTH
        ) {
            throw new HPLRValidationException("Nickname must be between 2 and 12 characters");
        }
    }

    static void validateEmail(Player player) throws HPLRValidationException {
        try {
            StringValidator.validateString(player.getUserData().email());
        }
        catch (HPLRValidationException ex){
            throw new HPLRValidationException("Email cannot be empty!");
        }
    }

    static void validateScore(Player player) throws HPLRValidationException {
        if(Objects.isNull(player.getRanking().score()) ||
                player.getRanking().score() <0){
            throw new HPLRValidationException("Score must be non-negative!");
        }
    }

    static void validatePwHash(Player player) throws HPLRValidationException {
        try {
            StringValidator.validateString(player.getSecurity().pwHash());
        }
        catch (HPLRValidationException ex){
            throw new HPLRValidationException("Password cannot be empty!");
        }
    }

    static void validateRegistrationTime(Player player) throws HPLRValidationException {
        if (
                Objects.isNull(player.getSecurity().registrationTime())
        ) {
            throw new HPLRValidationException("Registration date cannot be empty!");
        }
    }

        private PlayerValidator(){
        throw new IllegalArgumentException("utility class");
        }


}
