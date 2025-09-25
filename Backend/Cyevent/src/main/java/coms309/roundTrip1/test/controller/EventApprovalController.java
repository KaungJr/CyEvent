package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.EventApproval;
import coms309.roundTrip1.test.repository.EventApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EventApprovalController {

    @Autowired
    EventApprovalRepository eventApprovalRepository;

    @GetMapping("/eventApprovals")
    public List<EventApproval> GetAllEventApprovals() {
        return eventApprovalRepository.findAll();
    }

    @GetMapping("/eventApprovals/{id}")
    public EventApproval GetEventApproval(@PathVariable Long id) {
        Optional<EventApproval> existingEventApprovalOptional = eventApprovalRepository.findById(id);

        return existingEventApprovalOptional.get();
    }

    @PostMapping("/eventApprovals")
    EventApproval PostEventApproval(@RequestBody EventApproval newEventApproval) {
        eventApprovalRepository.save(newEventApproval);
        return newEventApproval;
    }

    @PutMapping("/eventApprovals/{id}")
    public Map<String, Object> updateEventApproval(@PathVariable Long id, @RequestBody EventApproval updatedEventApproval) {
        Map<String, Object> response = new HashMap<>();

        Optional<EventApproval> existingEventApprovalOptional = eventApprovalRepository.findById(id);

        if (existingEventApprovalOptional.isPresent()) {
            EventApproval existingEventApproval = existingEventApprovalOptional.get();

            existingEventApproval.setIsApproved(updatedEventApproval.getIsApproved());
            existingEventApproval.setApprovalReason(updatedEventApproval.getApprovalReason());

            eventApprovalRepository.save(existingEventApproval);
            response.put("message", "Event Approval updated successfully");
            response.put("EventApproval", existingEventApproval);
        } else {
            response.put("message", "EventApproval not found with id: " + id);
        }
        return response;
    }

    @DeleteMapping("/eventApprovals/{id}")
    public Map<String, String> DeleteEventApproval(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        Optional<EventApproval> existingEventApprovalOptional = eventApprovalRepository.findById(id);

        if (existingEventApprovalOptional.isPresent()) {
            eventApprovalRepository.deleteById(id);

            response.put("message", "Event Approval deleted successfully");
        } else {
            response.put("message", "EventApproval not found with id: " + id);
        }
        return response;
    }
}











