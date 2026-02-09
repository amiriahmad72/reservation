package com.azki.example.reservation.slot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
class AvailableSlotServiceImpl implements AvailableSlotService {

    private final AvailableSlotRepository availableSlotRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public AvailableSlot getFirstAvailableSlot() {
        return availableSlotRepository.findTopByIsReservedFalseOrderByStartTimeAsc()
                .orElseThrow(); //todo
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setIsReserved(AvailableSlot slot, boolean isReserved) {
        slot.setIsReserved(isReserved);
        availableSlotRepository.save(slot);
    }
}
