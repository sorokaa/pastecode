package io.srk.pastecode.code.model.entity;

import io.srk.pastecode.code.model.enumeration.Availability;
import io.srk.pastecode.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(schema = "pastecode", name = "code")
public class Code {

    @Id
    @GeneratedValue
    private UUID id;

    private String snippet;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Enumerated(EnumType.STRING)
    private Availability availability;

    @ToString.Exclude
    private String password;

    private Instant expireAt;

    @CreationTimestamp
    private Instant created;

    public boolean isExpired() {
        if (expireAt == null) {
            return false;
        }
        return Instant.now().compareTo(expireAt) >= 0;
    }
}
