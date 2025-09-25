package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Nathan Church
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to the Dog database <br><br>"+
                "Add these commands to the url to see the dogs <br>" +
                "List of Dogs: /dog <br>" +
                "Get specific Dog: /whereDog/{name}";
    }
}
