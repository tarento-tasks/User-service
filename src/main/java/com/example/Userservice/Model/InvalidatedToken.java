package com.example.Userservice.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "invalidated_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidatedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Date expirationDate; // Token expiry date

    public InvalidatedToken(String token, Date expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }
}
