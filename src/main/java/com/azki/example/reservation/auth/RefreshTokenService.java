
package com.azki.example.reservation.auth;

import com.azki.example.reservation.user.User;
import com.azki.example.reservation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final SecurityProperties securityProperties;
    private final RefreshTokenRepository repo;
    private final UserRepository userRepo;

    public RefreshToken create(User user) {
        RefreshToken t = new RefreshToken();
        t.setToken(UUID.randomUUID().toString());
        t.setUser(user);
        t.setExpiry(Instant.now().plusMillis(securityProperties.refreshExp()));
        t.setRevoked(false);
        return repo.save(t);
    }

    @Transactional
    public RefreshToken rotate(String token) {
        RefreshToken oldRefreshToken = repo.findByToken(token)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiry().isAfter(Instant.now())).orElseThrow();
        revoke(oldRefreshToken);
        RefreshToken newRefreshToken = create(oldRefreshToken.getUser());
        User justForReloadUserFromDB = newRefreshToken.getUser();
        return newRefreshToken;
    }

    @Transactional
    public void revoke(String token) {
        repo.findByToken(token).ifPresent(this::revoke);
    }

    private void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        repo.save(refreshToken);
    }

}
