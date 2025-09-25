package coms309.roundTrip1.test.controller;

import coms309.roundTrip1.test.model.UserType;
import coms309.roundTrip1.test.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserTypeController {

    @Autowired
    UserTypeRepository userTypeRepository;

    @GetMapping("/userTypes")
    public List<UserType> GetAllUserTypes(){
        return userTypeRepository.findAll();
    }

    @GetMapping("userTypes/{id}")
    public UserType GetUserType(@PathVariable Long id) {
        Optional<UserType> existingUserTypeOptional = userTypeRepository.findById(id);

        return existingUserTypeOptional.get();
    }

    @PostMapping("/userTypes")
    UserType PostUserType(@RequestBody UserType newUserType) {
        userTypeRepository.save(newUserType);
        return newUserType;
    }

    @PutMapping("/userTypes/{id}")
    public Map<String, Object> updateUserType(@PathVariable Long id, @RequestBody UserType updatedUserType) {
        Map<String, Object> response = new HashMap<>();

        Optional<UserType> existingUserTypeOptional = userTypeRepository.findById(id);

        if (existingUserTypeOptional.isPresent()) {
            UserType existingUserType = existingUserTypeOptional.get();

            existingUserType.setType(updatedUserType.getType());

            userTypeRepository.save(existingUserType);
            response.put("message", "UserType updated successfully");
            response.put("UserType", existingUserType);
        } else {
            response.put("message", "UserType not found with id: " + id);
        }

        return response;
    }

    @DeleteMapping("/userTypes/{id}")
    public Map<String, String> DeleteUserType(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        Optional<UserType> existingUserTypeOptional = userTypeRepository.findById(id);

        if (existingUserTypeOptional.isPresent()) {
            userTypeRepository.deleteById(id);

            response.put("message","UserType deleted successfully");
        }
        else {
            response.put("message", "UserType not found with id: " + id);
        }

        return response;
    }
}
