package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    ProfileRepository profileRepository;

    // View all friends for a profile
    @GetMapping("/{profileId}")
    public ResponseEntity<?> viewFriends(@PathVariable Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElse(null);

        if (profile != null) {
            List<Profile> friends = profile.getFriends();
            return ResponseEntity.ok(friends);
        } else {
            return ResponseEntity.badRequest().body("Profile not found");
        }
    }

    // Add a friend to a profile
    @PostMapping("/{profileId}/add/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long profileId, @PathVariable Long friendId) {
        Profile profile = profileRepository.findById(profileId).orElse(null);
        Profile friend = profileRepository.findById(friendId).orElse(null);

        if (profile != null && friend != null) {
            if (!profile.getFriends().contains(friend)) {
                profile.getFriends().add(friend);
                profileRepository.save(profile);
                return ResponseEntity.ok("Friend added successfully");
            } else {
                return ResponseEntity.badRequest().body("Friend already added");
            }
        } else {
            return ResponseEntity.badRequest().body("Profile or friend not found");
        }
    }

    // Remove a friend from a profile
    @DeleteMapping("/{profileId}/remove/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long profileId, @PathVariable Long friendId) {
        Profile profile = profileRepository.findById(profileId).orElse(null);
        Profile friend = profileRepository.findById(friendId).orElse(null);

        if (profile != null && friend != null) {
            if (profile.getFriends().contains(friend)) {
                profile.getFriends().remove(friend);
                profileRepository.save(profile);
                return ResponseEntity.ok("Friend removed successfully");
            } else {
                return ResponseEntity.badRequest().body("Friend not found in the profile's friends list");
            }
        } else {
            return ResponseEntity.badRequest().body("Profile or friend not found");
        }
    }
}
