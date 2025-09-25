package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.Profile;
import coms309.roundTrip1.test.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    ProfileRepository profileRepository;

    @PostMapping("/login")
    Profile Login(@RequestBody Profile newLogin) {
        String username = newLogin.getUsername();
        String password = newLogin.getPassword();

        Optional<Profile> existingProfileOptional = profileRepository.findByUsername(username);

        if (existingProfileOptional.isPresent()) {
            Profile loginProfile = existingProfileOptional.get();

            if (password.equals(loginProfile.getPassword())) {
                return loginProfile;
            }
            return null;
        }
        return null;
    }
}
