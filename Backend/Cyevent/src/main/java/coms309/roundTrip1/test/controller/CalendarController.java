package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.Calendar;
import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.model.Event;
import coms309.roundTrip1.test.repository.CalendarRepository;
import coms309.roundTrip1.test.repository.ProfileRepository;
import coms309.roundTrip1.test.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CalendarController {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/calendar")
    public List<Calendar> getAllCalendarEntries() {
        return calendarRepository.findAll();
    }
    // 1. Add Event to Calendar
    @PostMapping("/calendar/{profileId}/{eventId}")
    public ResponseEntity<String> addEventToCalendar(@PathVariable Long profileId, @PathVariable Long eventId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (profileOpt.isPresent() && eventOpt.isPresent()) {
            Calendar calendarEntry = new Calendar(profileOpt.get(), eventOpt.get());
            calendarRepository.save(calendarEntry);
            return ResponseEntity.ok("Event added to calendar successfully");
        }
        return ResponseEntity.badRequest().body("Profile or Event not found");
    }

    // 2. Get Calendar Events for a Profile
    @GetMapping("/calendar/profile/{profileId}")
    public ResponseEntity<List<Calendar>> getCalendarByProfile(@PathVariable Long profileId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);

        if (profileOpt.isPresent()) {
            List<Calendar> calendarEntries = calendarRepository.findByProfile(profileOpt.get());
            return ResponseEntity.ok(calendarEntries);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Remove Event from Calendar
    @DeleteMapping("/calendar/{profileId}/{eventId}")
    public ResponseEntity<String> removeEventFromCalendar(@PathVariable Long profileId, @PathVariable Long eventId) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (profileOpt.isPresent() && eventOpt.isPresent()) {
            List<Calendar> calendarEntries = calendarRepository.findByProfile(profileOpt.get());

            for (Calendar entry : calendarEntries) {
                if (entry.getEvent().equals(eventOpt.get())) {
                    calendarRepository.delete(entry);
                    return ResponseEntity.ok("Event removed from calendar successfully");
                }
            }
            return ResponseEntity.badRequest().body("Event not found in user's calendar");
        }
        return ResponseEntity.badRequest().body("Profile or Event not found");
    }
}
