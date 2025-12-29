package snowcode.snowcode.auth.dto.login;

import snowcode.snowcode.auth.dto.login.kakao.KakaoUserResponse;

public record UserResponse (String providerId, String email) {

    public static UserResponse of (KakaoUserResponse kakao) {
        String email = null;
        if (kakao.kakaoAccount() != null && kakao.kakaoAccount().email() != null) {
            email = kakao.kakaoAccount().email();
        }
        return new UserResponse(kakao.id(), email);
    }
}
