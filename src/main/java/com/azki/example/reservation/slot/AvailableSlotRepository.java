package com.azki.example.reservation.slot;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long> {

    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")
    })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AvailableSlot> findTopByIsReservedFalseOrderByStartTimeAsc();

}
