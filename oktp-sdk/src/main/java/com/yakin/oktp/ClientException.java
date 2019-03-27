package com.yakin.oktp;

public class ClientException extends Exception {

    private Boolean canceled = false;

    public ClientException(String message) {
        super("[ErrorMessage]: " + message);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public ClientException(String message, Throwable cause) {
        this(message, cause, false);
    }

    public ClientException(String message, Throwable cause, Boolean isCancelled) {
        super("[ErrorMessage]: " + message, cause);
        this.canceled = isCancelled;
    }

    public Boolean isCanceled() {
        return canceled;
    }

    @Override
    public String getMessage() {
        String base = super.getMessage();
        return getCause() == null ? base : getCause().getMessage() + "\n" + base;
    }
}
