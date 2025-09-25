package coms309.people;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@RestController
public class ArrayEditor {


    ArrayList<String> myList = new ArrayList<>();

    //List
    @GetMapping("/printList")
    public String listElements() {
        return myList.toString();
    }

    //Create
    @PostMapping("/addToList/{element}")
    public String addElement(@PathVariable String element) {
        myList.add(element);

        return "Added " + element + " to your list";
    }

    //Read
    @GetMapping("/printElement/{element}")
    public String getElement(@PathVariable String element) {
        int position = myList.indexOf(element);

        return myList.get(position);
    }

    //Update
    @PutMapping("updateElement/{oldElement}/{newElement}")
    public String updateElement(@PathVariable String oldElement, @PathVariable String newElement) {
        int position = myList.indexOf(oldElement);
        myList.set(position, newElement);

        return "Replaced " + oldElement + " with " + newElement;
    }

    //Delete
    @DeleteMapping("/deleteElement/{element}")
    public String deleteElement(@PathVariable String element) {
        int position = myList.indexOf(element);
        myList.remove(position);

        return "Removed " + element + " from the list";
    }
}
