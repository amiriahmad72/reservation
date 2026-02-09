package com.azki.example.reservation;

import com.azki.example.reservation.slot.AvailableSlot;
import com.azki.example.reservation.slot.AvailableSlotRepository;
import com.azki.example.reservation.user.User;
import com.azki.example.reservation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Profile("dev")
@RequiredArgsConstructor
@Component
public class DevDataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AvailableSlotRepository availableSlotRepository;

    @Override
    public void run(String... args) {
        loadUsers();
        loadAvailableSlots();
    }

    private void loadUsers() {
        if (userRepo.count() > 0) return; // prevent duplicates
        for (int index = 0; index < 10; index++) {
            User user = new User();
            user.setUsername("user" + index);
            user.setEmail("user" + index + "@gmail.com");
            user.setPassword(encoder.encode("pass" + index));
            userRepo.save(user);
        }
    }

    private void loadAvailableSlots() {
        if (availableSlotRepository.count() > 0) return; // prevent duplicates
        Instant now = Instant.now();
        for (int index = 0; index < 100; index++) {
            AvailableSlot availableSlot = new AvailableSlot();
            availableSlot.setStartTime(now.plus(index, ChronoUnit.HOURS));
            availableSlot.setEndTime(now.plus(index+1, ChronoUnit.HOURS));
            availableSlot.setIsReserved(false);
            availableSlotRepository.save(availableSlot);
        }
    }

}
