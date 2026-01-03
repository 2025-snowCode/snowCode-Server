package snowcode.snowcode.common;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import snowcode.snowcode.auth.dto.login.LoginRequest;
import snowcode.snowcode.auth.service.AuthService;

import java.security.GeneralSecurityException;

@Component
@Profile("local")
@RequiredArgsConstructor
public class TestDataInitializer {

    private final AuthService authService;
    private final int ADMIN_ROOP = 3;
    private final int USER_ROOP = 1;

    @PostConstruct
    @Transactional
    public void init() throws GeneralSecurityException {
        System.out.println("test");
        createMember();

    }

    private void createMember() throws GeneralSecurityException {
        // admin
        for (int i=0; i<ADMIN_ROOP; i++) {
            LoginRequest dto = new LoginRequest("LOCAL", "test"+i, "ADMIN", String.valueOf(i), "test"+i+"@gmail.com", "sM0yOK1FPuGJaq8x/U76gkKNfT64GQKsityED54zG9M=");
            authService.login(dto);
        }

        // user
        for (int i=ADMIN_ROOP; i<ADMIN_ROOP+USER_ROOP; i++) {
            LoginRequest dto = new LoginRequest("LOCAL", "test"+i, "USER", String.valueOf(i), "test"+i+"@gmail.com", "sM0yOK1FPuGJaq8x/U76gkKNfT64GQKsityED54zG9M=");
            authService.login(dto);
        }
    }
}
