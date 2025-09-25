package coms309.roundTrip1.test.repository;

import coms309.roundTrip1.test.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
}
