package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.Interest;
import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.repository.InterestRepository;
import coms309.roundTrip1.test.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class InterestController {


    @Autowired
    InterestRepository interestRepository;
    @Autowired
    ProfileRepository profileRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/interests")
    public List<Interest> getAllInterests() {
        return interestRepository.findAll();
    }

    @GetMapping("/interest/profiles/{id}")
    public Map<String, Object> getProfileById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Profile> profileOptional = profileRepository.findById(id);

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            response.put("Profile", profile);
            response.put("Interests", profile.getInterests());
        } else {
            response.put("message", "Profile not found with id: " + id);
        }

        return response;
    }

    @PostMapping(path = "/interests/profile/{id}")
    public String createInterest(@PathVariable Long id, @RequestBody Interest interest) {
        Profile profile = profileRepository.findById(id).orElse(null);
        if (profile == null) {
            return failure;
        }
        interest.setProfile(profile);
        interestRepository.save(interest);
        return success;
    }

    @PutMapping("/interests/{id}")
    public Interest updateInterest(@PathVariable Long id, @RequestBody Interest request) {
        Interest interest = interestRepository.findById(id).orElse(null);
        if (interest == null) {
            return null;
        }
        interest.setName(request.getName());
        interestRepository.save(interest);
        return interest;
    }
    @DeleteMapping("/interests/{id}")
    public String deleteInterest(@PathVariable Long id) {
        Optional<Interest> interestOptional = interestRepository.findById(id);
        if (interestOptional.isPresent()) {
            interestRepository.deleteById(id);
            return success;
        } else {
            return failure;
        }
    }
}
