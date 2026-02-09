package com.azki.example.reservation;

import com.azki.example.reservation.user.User;
import com.azki.example.reservation.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ReservationIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userTest() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("hash_password");
        user.setEmail("admin@example.com");
        userRepository.save(user);

        log.info("user: {}", user);

        userRepository.findAll().forEach(u -> log.info("user: {}", u));

        userRepository.deleteAll();
    }

}
