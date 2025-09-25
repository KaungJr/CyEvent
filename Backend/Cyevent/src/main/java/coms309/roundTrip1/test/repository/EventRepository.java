package coms309.roundTrip1.test.repository;

import coms309.roundTrip1.test.model.Event;
import coms309.roundTrip1.test.model.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByName(String name);

    List<Event> findAllByOrderByDateAsc(); // Sort by date
    List<Event> findAllByOrderByTimeAsc(); // Sort by time

}
