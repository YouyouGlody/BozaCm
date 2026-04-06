package com.logondigital.bozacm.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {

        super("L'email '" + email + "' est déjà utilisé par un autre utilisateur. Veuillez en choisir un différent.");
    }
}
