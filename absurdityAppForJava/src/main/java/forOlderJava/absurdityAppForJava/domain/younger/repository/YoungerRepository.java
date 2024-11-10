package forOlderJava.absurdityAppForJava.domain.younger.repository;

import forOlderJava.absurdityAppForJava.domain.younger.Younger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YoungerRepository extends JpaRepository<Younger, Long> {

    boolean existsByUsername(String username);

    Optional<Younger> findByUsername(String username);
}
