package com.azki.example.reservation.slot;

public interface AvailableSlotService {

    AvailableSlot getFirstAvailableSlot();

    void setIsReserved(AvailableSlot slot, boolean isReserved);

}
