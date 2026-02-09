package com.azki.example.reservation.reserve;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservedSlotRepository extends JpaRepository<ReservedSlot, Long> {
}
