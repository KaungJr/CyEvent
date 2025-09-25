package coms309.roundTrip1.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Profile {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;
    private String username;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> interests;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RSVP> rsvps = new ArrayList<>();

    // New relationship for friends
    @ManyToMany
    @JoinTable(
            name = "friends", // Table name for the many-to-many relationship
            joinColumns = @JoinColumn(name = "profile_id"), // Current profile
            inverseJoinColumns = @JoinColumn(name = "friend_id") // Friend profile
    )
    private List<Profile> friends = new ArrayList<>();


    public Profile() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<RSVP> getRsvps() {
        return rsvps;
    }

    public void setRsvps(List<RSVP> rsvps) {
        this.rsvps = rsvps;
    }

    public List<Profile> getFriends() {
        return friends;
    }

    public void setFriends(List<Profile> friends) {
        this.friends = friends;
    }
}
