package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class WordMachine {

    @GetMapping("/")
    public String menu() {
        return "Welcome to the Word Machine!<br>" +
                "There are several options to choose from:<br><br>" +
                "1. type /r/ after the url followed by any amount of words to get them in reverse order<br><br>";
    }

    @GetMapping("/r/{word}")
    public String optionOne(@PathVariable String word) {

        /*
        reverseWord is used to reverse the word added after the / in the URL of the page and
        display it back to the user
         */
        StringBuilder reverseWord = new StringBuilder();

        for (int i=word.length()-1; i>-1; i--) {
            reverseWord.append(word.charAt(i));
        }

        return "This is your reversed word: " + reverseWord;
    }
}

