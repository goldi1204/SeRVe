package horizon.SeRVe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<horizon.SeRVe.entity.TeamRepository, Long> {
    Optional<horizon.SeRVe.entity.TeamRepository> findByName(String name);
}