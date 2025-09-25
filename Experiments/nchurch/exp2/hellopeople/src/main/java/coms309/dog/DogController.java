package coms309.dog;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

@RestController
public class DogController {

    HashMap<String, Dog> dogList = new  HashMap<>();


    @GetMapping("/dog")
    public  HashMap<String,Dog> getAllDogs() { return dogList; }

    @PostMapping("/helloDog")
    public  String helloDog(@RequestBody Dog dog) {
        System.out.println(dog);
        dogList.put(dog.getName(), dog);
        return "New dog "+ dog.getName() + " Saved";
    }

    @GetMapping("/whereDog/{name}")
    public Dog whereDog(@PathVariable String name) {
        Dog d = dogList.get(name);
        return d;
    }

    @PutMapping("/changeDog/{name}")
    public Dog changeDog(@PathVariable String name, @RequestBody Dog d) {
        dogList.replace(name, d);
        return dogList.get(name);
    }

    @DeleteMapping("/goodbyeDog/{name}")
    public HashMap<String, Dog> goodbyeDog(@PathVariable String name) {
        dogList.remove(name);
        return dogList;
    }
}
