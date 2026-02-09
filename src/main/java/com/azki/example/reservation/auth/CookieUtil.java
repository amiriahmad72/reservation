
package com.azki.example.reservation.auth;

import org.springframework.http.ResponseCookie;

public final class CookieUtil {

    private CookieUtil() {
    }

    public static ResponseCookie accessCookie(String token) {
        return cookie("ACCESS_TOKEN", token, "/", 900);
    }

    public static ResponseCookie deletedAccessCookie() {
        return cookie("ACCESS_TOKEN", null, "/", 0);
    }

    public static ResponseCookie refreshCookie(String token) {
        return cookie("REFRESH_TOKEN", token, "/auth", 2592000);
    }

    public static ResponseCookie deletedRefreshCookie() {
        return cookie("REFRESH_TOKEN", null, "/auth", 0);
    }

    private static ResponseCookie cookie(String name, String value, String path, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true).secure(true).sameSite("Strict")
                .path(path).maxAge(maxAge).build();
    }
}
