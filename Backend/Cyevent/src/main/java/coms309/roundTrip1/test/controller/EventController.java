package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.Event;
import coms309.roundTrip1.test.model.EventApproval;
import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.repository.EventApprovalRepository;
import coms309.roundTrip1.test.repository.EventRepository;
import coms309.roundTrip1.test.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    EventApprovalRepository  eventApprovalRepository;

    @GetMapping("/events")
    public List<Event> GetAllEvent(){
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    public Map<String, Object> GetEvent(@PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();

        Optional<Event> existingEventOptional = eventRepository.findById(id);

        if (existingEventOptional.isPresent()) {
            Event existingEvent = existingEventOptional.get();

            response.put("Event", existingEvent);
        }
        else {
            response.put("message", "Event not found with id: " + id);
        }

        return response;
    }

    @PutMapping("/events/{id}")
    public Map<String, Object> UpdateEvent(@PathVariable Long id, @RequestBody Event event){

        Map<String, Object> response = new HashMap<>();

        Optional<Event> existingEventOptional = eventRepository.findById(id);

        if (existingEventOptional.isPresent()) {
            Event existingEvent = existingEventOptional.get();

            existingEvent.setName(event.getName());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setDate(event.getDate());
            existingEvent.setTime(event.getTime());
            existingEvent.setLocation(event.getLocation());
            existingEvent.setApproval(event.getApproval());

            EventApproval updatedApproval = event.getApproval();
            EventApproval newApproval = new EventApproval();

            newApproval.setIsApproved(updatedApproval.getIsApproved());

            newApproval.setApprovalReason(updatedApproval.getApprovalReason());

            eventApprovalRepository.save(newApproval);
            existingEvent.setApproval(newApproval);

            eventRepository.save(existingEvent);

            response.put("message", "Event updated successfully");
            response.put("Event", existingEvent);
        }
        else {
            response.put("message", "Event not found with id: " + id);
        }
        return response;
    }

    @PostMapping("/events")
    public Map<String, Object> PostEventByBody(@RequestBody Event newEvent) {
        Map<String, Object> response = new HashMap<>();

        eventRepository.save(newEvent);

        response.put("message", "Event created successfully");
        response.put("Event", newEvent);

        return response;
    }

    @DeleteMapping("/events/{id}")
    public Map<String, String> DeleteEvent(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        Optional<Event> existingEventOptional = eventRepository.findById(id);

        if (existingEventOptional.isPresent()) {
            eventRepository.deleteById(id);

            response.put("message", "Event deleted successfully");
        }
        else {
            response.put("message", "Event not found with id: " + id);
        }

        return response;
    }

    @GetMapping("/events/name/{nameOfEvent}")
    public List<Event> getEventsByName(@PathVariable String nameOfEvent) {
        return eventRepository.findByName(nameOfEvent);
    }

//sorting by date or time
    @GetMapping("/events/sort/all")
    public ResponseEntity<List<Event>> getAllEventsSortedBy(
            @RequestParam String order // Expect 'date' or 'time'
    ) {
        List<Event> sortedEvents;

        switch (order.toLowerCase()) {
            case "date":
                sortedEvents = eventRepository.findAllByOrderByDateAsc();
                break;
            case "time":
                sortedEvents = eventRepository.findAllByOrderByTimeAsc();
                break;
            default:
                return ResponseEntity.badRequest().body(null); // Invalid order parameter
        }

        return ResponseEntity.ok(sortedEvents);
    }
}
