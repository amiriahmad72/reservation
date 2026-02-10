package com.azki.example.reservation.reserve;

import com.azki.example.reservation.exception.NotFoundException;
import com.azki.example.reservation.slot.AvailableSlot;
import com.azki.example.reservation.slot.AvailableSlotService;
import com.azki.example.reservation.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
class ReserveServiceImpl implements ReserveService {


    private final ReservedSlotRepository reservedSlotRepository;
    private final AvailableSlotService availableSlotService;

    @Transactional
    @Override
    public ReservedSlotDTO reserve() {
        AvailableSlot firstAvailableSlot = availableSlotService.getFirstAvailableSlot();
        ReservedSlot reservedSlot = new ReservedSlot();
        reservedSlot.setSlot(firstAvailableSlot);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        reservedSlot.setUser(userDetails.user());
        reservedSlotRepository.save(reservedSlot);
        availableSlotService.setIsReserved(firstAvailableSlot, true);
        return new ReservedSlotDTO(reservedSlot.getId(), reservedSlot.getSlot(), reservedSlot.getReservedAt(), reservedSlot.getCancelled(), reservedSlot.getReservedAt());
    }

    @Transactional
    @Override
    public void cancelReservation(Long id) {
        ReservedSlot reservedSlot = reservedSlotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ReservedSlot.class, id));
        reservedSlot.cancel();
        reservedSlotRepository.save(reservedSlot);
        availableSlotService.setIsReserved(reservedSlot.getSlot(), false);
    }
}
