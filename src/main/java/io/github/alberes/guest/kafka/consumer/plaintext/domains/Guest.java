package io.github.alberes.guest.kafka.consumer.plaintext.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Guest implements Serializable {

    @Id
    @Column(name = "legal_entity_number")
    private String legalEntityNumber;

    @Column(name = "name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private LocalDate birthday;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "last_update_date")
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "creation_date")
    @CreatedDate
    private LocalDateTime creationDate;

    public Guest() {
    }

    public Guest(String legalEntityNumber, String name, LocalDate birthday) {
        this.legalEntityNumber = legalEntityNumber;
        this.name = name;
        this.birthday = birthday;
    }

    public String getLegalEntityNumber() {
        return legalEntityNumber;
    }

    public void setLegalEntityNumber(String legalEntityNumber) {
        this.legalEntityNumber = legalEntityNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(legalEntityNumber, guest.legalEntityNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(legalEntityNumber);
    }
}