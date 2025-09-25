package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello World and welcome to COMS 309";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello World and welcome to COMS 309 your name: " + name;

    }
    @GetMapping("/getAge/{age}")
    public String getage(@PathVariable int age){
        return "the age is: " + age;
    }
    @GetMapping("/greet/{age}")
    public String greetByAge(@PathVariable int age) {
        if (age < 18) { // less than 18
            return "You are young and have a bright future ahead!";
        } else if (age <= 40) { //less or equal to 40
            return "You are in the prime of your life!";
        } else {
            return "You have gathered wisdom through the years!";
        }
    }
    /*
    A method that take name,age,major as parmenters from url
    and returns a person object with those values.
     */
    @GetMapping("/person/{name}/{age}/{major}")
    public Person getPerson(@PathVariable String name, @PathVariable int age, @PathVariable String major) {
        return new Person(name, age, major);

    }
    // This class represent a Person with a name and an age.
    class Person {
        private String name;  // person's name
        private int age; // person's age
        private String major;

        // constructor that called when creating a new Person object.
        public Person(String name, int age, String major) {
            this.name = name;
            this.age = age;
            this.major = major;
        }

        // This gatter method to retrieve the person's name.
        public String getName() {
            return name;
        }

        // Getter method for the age field
        public int getAge() {
            return age;
        }

        public String getMajor() {
            return major;
        }

    }

    }
