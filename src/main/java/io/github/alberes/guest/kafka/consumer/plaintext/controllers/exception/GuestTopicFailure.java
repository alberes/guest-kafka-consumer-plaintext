package io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception;

import io.github.alberes.guest.kafka.consumer.plaintext.domains.Guest;

import java.time.LocalDateTime;

public record GuestTopicFailure(LocalDateTime dateTime, Guest guest, Guest guestDB, StandardError standardError) {
}
