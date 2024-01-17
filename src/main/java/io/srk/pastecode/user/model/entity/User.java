package io.srk.pastecode.user.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(schema = "pastecode", name = "user")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String username;
}
