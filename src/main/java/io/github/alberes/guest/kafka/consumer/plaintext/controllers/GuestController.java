package io.github.alberes.guest.kafka.consumer.plaintext.controllers;

import io.github.alberes.guest.kafka.consumer.plaintext.domains.Guest;
import io.github.alberes.guest.kafka.consumer.plaintext.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<Guest> find(@PathVariable String legalEntityNumber){
        Guest guest = this.service.find(legalEntityNumber);
        return ResponseEntity.status(200).body(guest);
    }
}
