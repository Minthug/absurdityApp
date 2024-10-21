package forOlderJava.absurdityAppForJava.domain.coupon;

import forOlderJava.absurdityAppForJava.global.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.InvalidParameterException;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private Integer discount;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDate endAt;

    @Builder
    public Coupon(Integer discount, String name, String description, LocalDate endAt) {
        validateEndAt(endAt);
        this.discount = discount;
        this.name = name;
        this.description = description;
        this.endAt = endAt;
    }

    private void validateEndAt(LocalDate endAt) {
        LocalDate currentDate = LocalDate.now();
        if (endAt.isBefore(currentDate)) {
            throw new InvalidParameterException("쿠폰 종료일은 현재 날짜보다 이전일 수 없습니다.");
        }
    }
}
