package coms309.roundTrip1.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;
    private String name;
    private String description;
    private String date;
    private String time;
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private Profile organizer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "approval_id", referencedColumnName = "id")
    private EventApproval approval;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RSVP> rsvps = new ArrayList<>();

    public Event() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Profile getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Profile organizer) {
        this.organizer = organizer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventApproval getApproval() {
        return approval;
    }

    public void setApproval(EventApproval approval) {
        this.approval = approval;
    }

    public List<RSVP> getRsvps() {
        return rsvps;
    }

    public void setRsvps(List<RSVP> rsvps) {
        this.rsvps = rsvps;
    }
}
