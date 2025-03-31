package io.github.alberes.guest.kafka.consumer.plaintext.controllers;

import io.github.alberes.guest.kafka.consumer.plaintext.controllers.exception.ObjectNotFoundException;
import io.github.alberes.guest.kafka.consumer.plaintext.domains.Guest;
import io.github.alberes.guest.kafka.consumer.plaintext.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/guests")
public class GuestController {

    @Autowired
    private GuestService service;

    @GetMapping
    public ResponseEntity<List<Guest>> allGuests(){
        List<Guest> guests = this.service.getGuests();

        if(guests.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(200).body(guests);
    }

    @GetMapping("/{legalEntityNumber}")
    public ResponseEntity<?> find(@PathVariable String legalEntityNumber){
        Optional<Guest> optional = this.service
                .getGuests()
                .stream()
                .filter(guest -> legalEntityNumber.equals(guest.getLegalEntityNumber()))
                .findFirst();
        if(!optional.isPresent()){
            throw new ObjectNotFoundException("Object not found! legalEntityNumber: " +
                legalEntityNumber + " type: " + Guest.class.getName());
        }
        return ResponseEntity.status(200).body(optional.get());
    }
}
