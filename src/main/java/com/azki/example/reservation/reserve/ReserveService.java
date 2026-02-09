package com.azki.example.reservation.reserve;

public interface ReserveService {

    ReservedSlotDTO reserve();

    void cancelReservation(Long id);

}
