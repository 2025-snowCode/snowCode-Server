package snowcode.snowcode.auth.dto.login;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.domain.Role;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long memberId;
    private final Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public CustomUserDetails(Member member) {
        this.memberId = member.getId();
        this.role = member.getRole();
    }


    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
