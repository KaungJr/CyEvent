package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProfileController {

    @Autowired
    ProfileRepository profileRepository;

    //List all the profiles in the database
    @GetMapping("/profiles")
    public List<Profile> GetAllProfile(){
        return profileRepository.findAll();
    }

    //Get one of the profiles in the database
    @GetMapping("/profiles/{id}")
    public Profile GetProfile(@PathVariable Long id) {
        Optional<Profile> existingProfileOptional = profileRepository.findById(id);

        return existingProfileOptional.get();
    }


    //Create a new profile
    @PostMapping("/profiles")
    Profile PostProfileByBody(@RequestBody Profile newProfile) {
        profileRepository.save(newProfile);
        return newProfile;
    }
    // Update an existing profile
    @PutMapping("/profiles/{id}")
    public Map<String, Object> updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        Map<String, Object> response = new HashMap<>();

        Optional<Profile> existingProfileOptional = profileRepository.findById(id);

        if (existingProfileOptional.isPresent()) {
            Profile existingProfile = existingProfileOptional.get();

            // Update fields
            existingProfile.setUsername(updatedProfile.getUsername());
            existingProfile.setEmail(updatedProfile.getEmail());
            existingProfile.setPassword(updatedProfile.getPassword());
            existingProfile.setUserType(updatedProfile.getUserType());

            // Save the updated profile
            profileRepository.save(existingProfile);
            response.put("message", "Profile updated successfully");
            response.put("Profile", existingProfile);
        } else {
            response.put("message", "Profile not found with id: " + id);
        }

        return response;
    }


    //Delete a profile
    @DeleteMapping("/profiles/{id}")
    public Map<String, String> DeleteProfile(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        Optional<Profile> existingProfileOptional = profileRepository.findById(id);

        if (existingProfileOptional.isPresent()) {
            profileRepository.deleteById(id);

            response.put("message","Profile deleted successfully");
        }
        else {
            response.put("message", "Profile not found with id: " + id);
        }

        return response;
    }

}
