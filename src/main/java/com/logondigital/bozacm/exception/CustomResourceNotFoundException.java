package com.logondigital.bozacm.exception;

public class CustomResourceNotFoundException extends RuntimeException {
  public CustomResourceNotFoundException(String message) {
    super(message);
  }
}
