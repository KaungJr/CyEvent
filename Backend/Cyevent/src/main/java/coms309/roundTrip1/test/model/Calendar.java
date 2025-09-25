package coms309.roundTrip1.test.model;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt = new Date();

    public Calendar() {
    }

    public Calendar(Profile profile, Event event) {
        this.profile = profile;
        this.event = event;
    }

    public Calendar(Long id, Profile profile, Event event, Date addedAt) {
        this.id = id;
        this.profile = profile;
        this.event = event;
        this.addedAt = addedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "id=" + id +
                ", profile=" + profile +
                ", event=" + event +
                ", addedAt=" + addedAt +
                '}';
    }
}
