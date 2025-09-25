package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class SoccerPlayer {

    private String firstName;

    private String lastName;

    private String position;

    private String team;

    private int number;

    private int age;

    private int goalsScored;

    private int assists;

    public SoccerPlayer(){

    }

    public SoccerPlayer(String firstName, String lastName, String position, String team, int number, int age, int goalsScored, int assists){
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.team = team;
        this.number = number;
        this.age = age;
        this.goalsScored = goalsScored;
        this.assists = assists;
    }

    // Getters and Setters
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTeam() {
        return this.team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGoalsScored() {
        return this.goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getAssists() {
        return this.assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    // String representation of SoccerPlayer object
    @Override
    public String toString() {
        return firstName + " "
                + lastName + " Position: " + position
                + "  Team: " + team
                + "  Jersey Number: " + number
                + "  Age: " + age
                + "  Goals Scored: " + goalsScored
                + "  Assists: " + assists;
    }
}
