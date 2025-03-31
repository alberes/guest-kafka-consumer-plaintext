package io.github.alberes.guest.kafka.consumer.plaintext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GuestKafkaConsumerPlaintextApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuestKafkaConsumerPlaintextApplication.class, args);
	}

}
