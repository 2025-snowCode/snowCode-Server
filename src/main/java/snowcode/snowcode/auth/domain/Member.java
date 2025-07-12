package snowcode.snowcode.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.common.BaseTimeEntity;

@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;

    private String role;

    private int studentId;
    private String email;

    private Member(String name, String role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public static Member createMember(String name, String role, String email) {
        return new Member(name, role, email);
    }
}
