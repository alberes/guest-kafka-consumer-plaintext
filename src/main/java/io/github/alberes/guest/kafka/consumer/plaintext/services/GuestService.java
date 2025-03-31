package io.github.alberes.guest.kafka.consumer.plaintext.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.DuplicateRecordException;
import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.GuestTopicFailure;
import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.StandardError;
import io.github.alberes.guest.kafka.consumer.plaintext.domains.Guest;
import io.github.alberes.guest.kafka.consumer.plaintext.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GuestRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topic-failure}")
    private String guestTopicFailure;

    private static final List<Guest> GUESTS = new ArrayList<Guest>();

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(String message){
        Guest guest = null;
        try {            
            guest = this.objectMapper.readValue(message, Guest.class);
            Optional<Guest> guestDB = this.repository.findById(guest.getLegalEntityNumber());
            if(guestDB.isPresent()){
                GuestTopicFailure guestTopicFailure = new GuestTopicFailure(LocalDateTime.now(), guest, guestDB.get(),
                        new StandardError(System.currentTimeMillis(), HttpStatus.CONFLICT.value(), "Conflit",
                                "Registration with " + guest.getLegalEntityNumber() + " has already been registered!",
                                this.guestTopicFailure));
                this.send(guestTopicFailure);
            }
            this.repository.save(guest);
            this.add(guest);
            System.out.println(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void send(GuestTopicFailure guestTopicFailure){
        String message = null;
        try {
            message = this.objectMapper.writeValueAsString(guestTopicFailure);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.kafkaTemplate.send(this.guestTopicFailure, message);
    }

    private void add(Guest guest){
        synchronized (GUESTS) {
            if (GUESTS.size() > 10) {
                GUESTS.remove(0);
            }
            GUESTS.add(guest);
        }
    }

    public List<Guest> getGuests(){
        return GUESTS;
    }
}
