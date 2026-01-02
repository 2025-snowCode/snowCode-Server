package snowcode.snowcode.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.auth.dto.login.LoginRequest;
import snowcode.snowcode.auth.dto.login.UserResponse;
import snowcode.snowcode.common.BaseTimeEntity;

import java.util.UUID;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private UUID username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "student_id")
    private String studentId;

    private String email;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    private Member(UUID username, String name, Role role, String studentId, String email, String provider, String providerId) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.studentId = studentId;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static Member createMember(LoginRequest dto, UserResponse userResponse) {
        return new Member(UUID.randomUUID(), dto.name() != null ? dto.name() : String.valueOf(UUID.randomUUID()), Role.of(dto.role()), dto.studentId(), dto.provider().equals("LOCAL") ? dto.email() : userResponse.email(), dto.provider(), userResponse.providerId());
    }


    public void updateStudentId(String studentId) {
        this.studentId = studentId;
    }
}
