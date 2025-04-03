package io.github.alberes.guest.kafka.consumer.plaintext.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.DuplicateRecordException;
import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.GuestTopicFailure;
import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.ObjectNotFoundException;
import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.StandardError;
import io.github.alberes.guest.kafka.consumer.plaintext.domains.Guest;
import io.github.alberes.guest.kafka.consumer.plaintext.repositories.GuestRepository;
import io.github.alberes.guest.kafka.consumer.plaintext.utils.JsonUtils;
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

    @Autowired
    private JsonUtils jsonUtils;

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(String message){
        Guest guest = guest = jsonUtils.fromJson(message, Guest.class);
            Optional<Guest> guestDB = this.repository.findById(guest.getLegalEntityNumber());
            if(guestDB.isPresent()){
                GuestTopicFailure guestTopicFailure = new GuestTopicFailure(LocalDateTime.now(), guest, guestDB.get(),
                        new StandardError(System.currentTimeMillis(), HttpStatus.CONFLICT.value(), "Conflit",
                                "Registration with " + guest.getLegalEntityNumber() + " has already been registered!",
                                this.guestTopicFailure));
                this.send(guestTopicFailure);
            }
            guest = this.repository.save(guest);
            this.add(guest);
            System.out.println(message);
    }

    public void send(GuestTopicFailure guestTopicFailure){
        String message = jsonUtils.toJson(guestTopicFailure);
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

    public Guest find(String legalEntityNumber){
        Optional<Guest> optional = GUESTS.stream()
                .filter(guest -> legalEntityNumber.equals(guest.getLegalEntityNumber()))
                .findFirst();
        if(optional.isPresent()){
            return optional.get();
        }

        Optional<Guest> guestDB = this.repository.findById(legalEntityNumber);
        if(!guestDB.isPresent()){
            throw new ObjectNotFoundException("Object not found! legalEntityNumber: " +
                    legalEntityNumber + " type: " + Guest.class.getName());
        }
        return guestDB.get();
    }
}
