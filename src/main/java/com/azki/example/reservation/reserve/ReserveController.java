package com.azki.example.reservation.reserve;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@RestController
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping
    public ReservedSlotDTO reserve() {
        return reserveService.reserve();
    }

    @DeleteMapping("/{id}")
    public void cancelReservation(@PathVariable Long id) {
        reserveService.cancelReservation(id);
    }

}
