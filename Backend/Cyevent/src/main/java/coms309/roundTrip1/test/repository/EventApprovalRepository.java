package coms309.roundTrip1.test.repository;

import coms309.roundTrip1.test.model.EventApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventApprovalRepository  extends JpaRepository<EventApproval, Long> {
}
