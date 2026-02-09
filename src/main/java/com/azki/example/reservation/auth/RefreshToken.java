
package com.azki.example.reservation.auth;

import com.azki.example.reservation.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Table(name = "refresh_tokens")
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiry;

    @Column(nullable = false)
    private boolean revoked;

}
