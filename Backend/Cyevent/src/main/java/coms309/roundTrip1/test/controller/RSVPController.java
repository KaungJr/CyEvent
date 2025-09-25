package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.RSVP;
import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.model.Event;
import coms309.roundTrip1.test.repository.EventRepository;
import coms309.roundTrip1.test.repository.ProfileRepository;
import coms309.roundTrip1.test.repository.RSVPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RSVPController {

    @Autowired
    RSVPRepository rsvpRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    EventRepository eventRepository;

    // Get all RSVPs
    @GetMapping
    public List<RSVP> getAllRSVPs() {
        return rsvpRepository.findAll();
    }

    // Get all RSVPs by Profile ID
    @GetMapping("rsvps/profile/{profileId}")
    public Map<String, Object> getRSVPsByProfileId(@PathVariable Long profileId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Profile> profileOptional = profileRepository.findById(profileId);

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            List<RSVP> rsvps = rsvpRepository.findByProfile(profile);
            response.put("RSVPs", rsvps);
        } else {
            response.put("message", "Profile not found with id: " + profileId);
        }

        return response;
    }

    // Get all RSVPs by Event ID
    @GetMapping("rsvps/event/{eventId}")
    public Map<String, Object> getRSVPsByEventId(@PathVariable Long eventId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            List<RSVP> rsvps = rsvpRepository.findByEvent(event);
            response.put("RSVPs", rsvps);
        } else {
            response.put("message", "Event not found with id: " + eventId);
        }

        return response;
    }

    // Create a new RSVP
    @PostMapping("/rsvps/{eventId}/{profileId}")
    public Map<String, Object> createRSVP(@PathVariable Long eventId, @PathVariable Long profileId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Profile> profileOptional = profileRepository.findById(profileId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (profileOptional.isPresent() && eventOptional.isPresent()) {
            Profile profile = profileOptional.get();
            Event event = eventOptional.get();

            // Check if RSVP already exists
            if (rsvpRepository.findByProfileAndEvent(profile, event).isPresent()) {
                response.put("message", "RSVP already exists for this profile and event.");
            } else {
                RSVP rsvp = new RSVP(profile, event);
                rsvpRepository.save(rsvp);
                response.put("message", "RSVP created successfully.");
                response.put("RSVP", rsvp);
            }
        } else {
            if (!profileOptional.isPresent()) {
                response.put("message", "Profile not found with id: " + profileId);
            }
            if (!eventOptional.isPresent()) {
                response.put("message", "Event not found with id: " + eventId);
            }
        }

        return response;
    }

    // Delete an RSVP by ID
    @DeleteMapping("rsvps/{id}")
    public Map<String, Object> deleteRSVP(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<RSVP> rsvpOptional = rsvpRepository.findById(id);

        if (rsvpOptional.isPresent()) {
            rsvpRepository.delete(rsvpOptional.get());
            response.put("message", "RSVP deleted successfully.");
        } else {
            response.put("message", "RSVP not found with id: " + id);
        }

        return response;
    }
}


