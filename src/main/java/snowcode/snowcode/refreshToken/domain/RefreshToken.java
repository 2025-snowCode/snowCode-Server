package snowcode.snowcode.refreshToken.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowcode.snowcode.auth.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(name="refresh_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(nullable = false, length = 512)
    private String token;

    @Column(name = "expiry_at", nullable = false)
    private LocalDateTime expiryAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static RefreshToken create(Member member, String token, LocalDateTime expiryAt) {
        return new RefreshToken(member, token, expiryAt);
    }

    public RefreshToken(Member member, String token, LocalDateTime expiryAt) {
        this.member = member;
        this.token = token;
        this.expiryAt = expiryAt;
    }

    public void updateToken(String token, LocalDateTime expiryAt) {
        this.token = token;
        this.expiryAt = expiryAt;
    }
}