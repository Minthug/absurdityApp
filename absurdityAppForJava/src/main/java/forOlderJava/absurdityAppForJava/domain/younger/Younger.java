package forOlderJava.absurdityAppForJava.domain.younger;

import forOlderJava.absurdityAppForJava.domain.younger.exception.InvalidYoungerException;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Younger extends BaseTimeEntity {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^(?=.*[a-z])[a-z0-9]{6,20}$");
    private static final int PASSWORD_LENGTH = 1000;
    private static final int ADDRESS_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Integer ratingCount = 0;

    @Builder
    public Younger(final String username, final String password, final String location) {
        validateUsername(username);
        validatePassword(password);
        validateLocation(location);
        this.username = username;
        this.password = password;
        this.location = location;
    }

    private void validateLocation(String location) {
        if (nonNull(location) && location.length() > ADDRESS_LENGTH) {
            throw new InvalidYoungerException("주소의 길이는 최대 200자 입니다");
        }
    }

    private void validatePassword(String password) {
        if (nonNull(password) && password.length() > PASSWORD_LENGTH) {
            throw new InvalidYoungerException("유효하지 않은 패스워드 입니다");
        }
    }

    private void validateUsername(String username) {
        if (nonNull(username) && !USERNAME_PATTERN.matcher(username).matches()) {
            throw new InvalidYoungerException("사용자 이름은 영어 소문자 또는 영어 소문자와 숫자 6자 이상, 20자 이하로 구성되어야 합니다");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Younger younger = (Younger) obj;
        return Objects.equals(username, younger.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public void updateRating(Double newRating) {
        this.rating = ((this.rating * this.ratingCount) + newRating) / (this.ratingCount + 1);
        this.ratingCount++;
    }
}
