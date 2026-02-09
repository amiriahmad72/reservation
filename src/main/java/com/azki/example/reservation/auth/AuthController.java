
package com.azki.example.reservation.auth;

import com.azki.example.reservation.user.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password()));
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshService.create(userDetails.user());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieUtil.accessCookie(accessToken).toString())
                .header(HttpHeaders.SET_COOKIE, CookieUtil.refreshCookie(refreshToken.getToken()).toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("REFRESH_TOKEN") String token) {
        RefreshToken refreshToken = refreshService.rotate(token);
        String accessToken = jwtService.generateAccessToken(new CustomUserDetails(refreshToken.getUser()));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieUtil.accessCookie(accessToken).toString())
                .header(HttpHeaders.SET_COOKIE, CookieUtil.refreshCookie(refreshToken.getToken()).toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("REFRESH_TOKEN") String token, HttpServletResponse response) {
        refreshService.revoke(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieUtil.deletedAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, CookieUtil.deletedRefreshCookie().toString())
                .header("Clear-Site-Data", "\"cookies\"")
                .build();
    }
}
