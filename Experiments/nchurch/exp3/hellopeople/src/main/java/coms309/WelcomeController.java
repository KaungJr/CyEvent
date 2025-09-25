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
        return "Hello and welcome to your list editor <br><br>" +
                "These are your options: <br>" +
                "Print your list: add /printList to the url <br>" +
                "Read element: add /printElement/{element} to the url";
    }
}
