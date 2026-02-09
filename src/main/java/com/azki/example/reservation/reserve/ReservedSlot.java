package com.azki.example.reservation.reserve;

import com.azki.example.reservation.slot.AvailableSlot;
import com.azki.example.reservation.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@ToString
@Getter
@Setter
@Table(name = "reserved_slots")
@Entity
public class ReservedSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AvailableSlot slot;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant reservedAt;

    private Boolean cancelled;

    private Instant cancelledAt;

    public void cancel() {
        this.cancelled = true;
        this.cancelledAt = Instant.now();
    }

}
