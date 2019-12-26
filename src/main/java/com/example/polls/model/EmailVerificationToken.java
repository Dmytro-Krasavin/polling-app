package com.example.polls.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {

    private static final int EXPIRATION_MINUTES = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Instant expirationDate;

    @Transient
    private boolean expired;

    public EmailVerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationDate = calculateExpirationDate(EXPIRATION_MINUTES);
    }

    private Instant calculateExpirationDate(int expiryTimeMinutes) {
        return Instant.now().plus(Duration.ofMinutes(expiryTimeMinutes));
    }

    @PostLoad
    private void fillExpired() {
        this.expired = Instant.now().isAfter(expirationDate);
    }
}
