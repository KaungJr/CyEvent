package coms309.roundTrip1.test.repository;

import coms309.roundTrip1.test.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Long>{
}
