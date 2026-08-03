package com.logondigital.bozacm.exceptions;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class PhoneAlreadyExistsException extends RuntimeException {

    public PhoneAlreadyExistsException(@NotNull @Pattern(regexp = "^[0-9]{9}$") String numeroDeTelephone) {

        super("Le numéro de téléphone '" + numeroDeTelephone + "' est déjà utilisé par un autre utilisateur. Veuillez en choisir un différent.");
    }
}
