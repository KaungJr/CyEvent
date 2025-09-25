package coms309.dog;

public class Dog {

    private String name;

    private String breed;

    private String color;

    private String weight;

    public Dog() {

    }

    public Dog(String name, String breed, String color, String weight){
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return this.breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) { this.color = color; }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return name + " "
                + breed + " "
                + color + " "
                + weight;
    }
}
