package com.azki.example.reservation.reserve;

import com.azki.example.reservation.slot.AvailableSlot;

import java.time.Instant;

public record ReservedSlotDTO(Long id, AvailableSlot slot, Instant reservedAt, Boolean cancelled,
                              Instant cancelledAt) {
}
