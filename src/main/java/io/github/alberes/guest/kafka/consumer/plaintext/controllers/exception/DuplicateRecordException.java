package io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception;

import io.github.alberes.guest.kafka.consumer.plaintext.domains.Guest;

import java.time.LocalDateTime;

public class DuplicateRecordException extends RuntimeException{

    private Guest guest;

    private StandardError standardError;

    public DuplicateRecordException(String msg, StandardError standardError, Guest guest) {
        super(msg);
        this.standardError = standardError;
        this.guest = guest;
    }

    public DuplicateRecordException(String msg, StandardError standardError, Throwable cause, Guest guest) {
        super(msg, cause);
        this.standardError = standardError;
        this.guest = guest;
    }

    public Guest getGuest() {
        return guest;
    }
}