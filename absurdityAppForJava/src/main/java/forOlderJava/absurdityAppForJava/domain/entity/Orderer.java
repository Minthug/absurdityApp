package forOlderJava.absurdityAppForJava.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orderer {
    private String name;
    private Long OlderId;
    private String phoneNumber;
    private String location;


}
