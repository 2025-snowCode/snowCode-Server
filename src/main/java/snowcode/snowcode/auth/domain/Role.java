package snowcode.snowcode.auth.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import snowcode.snowcode.auth.exception.AuthErrorCode;
import snowcode.snowcode.auth.exception.AuthException;

import java.util.Arrays;

@Getter
public enum Role {
    ADMIN, USER;

    public static Role of(@NotNull String role) {
        return Arrays.stream(Role.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_USER_ROLE));
    }
}
