package coms309.roundTrip1.test.repository;

import coms309.roundTrip1.test.model.Event;
import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.model.RSVP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RSVPRepository extends JpaRepository<RSVP, Long> {
    List<RSVP> findByProfile(Profile profile);
    List<RSVP> findByEvent(Event event);
    Optional<RSVP> findByProfileAndEvent(Profile profile, Event event);
}
