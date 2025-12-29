package snowcode.snowcode.auth.dto.login;

import snowcode.snowcode.auth.domain.Member;

import java.util.UUID;

public record CustomUserInfoDto(Long memberId, String role, String provider, String providerId, UUID username) {

    public static CustomUserInfoDto of(Member member) {
        return new CustomUserInfoDto(member.getId(), member.getRole().toString(), member.getProvider(), member.getProviderId(), member.getUsername());
    }
}
