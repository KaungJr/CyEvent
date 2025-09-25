package coms309.roundTrip1.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserType {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;
    private String type;

    @OneToMany(mappedBy = "userType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Profile> profiles;

    public UserType() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
