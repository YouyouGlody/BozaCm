package com.logondigital.bozacm.exceptions;

public class CustomResourceNotFoundException extends RuntimeException {
  public CustomResourceNotFoundException(String message) {
    super(message);
  }
}
