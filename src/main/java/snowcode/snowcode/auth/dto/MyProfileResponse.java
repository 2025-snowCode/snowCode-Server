package snowcode.snowcode.auth.dto;

import snowcode.snowcode.auth.domain.Member;

public record MyProfileResponse (String name) {

    public static MyProfileResponse from (Member member) {
        return new MyProfileResponse(member.getName());
    }
}
