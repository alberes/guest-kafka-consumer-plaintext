package io.github.alberes.guest.kafka.consumer.plaintext.repositories;

import io.github.alberes.guest.kafka.consumer.plaintext.domains.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, String> {
}
