package coms309.roundTrip1.test.repository;

import coms309.roundTrip1.test.model.Calendar;
import coms309.roundTrip1.test.model.Event;
import coms309.roundTrip1.test.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    //will find all calendar entries
    List<Calendar> findByProfile(Profile profile);

    //will find all calendar entries
    List<Calendar> findByEvent(Event event);
}
