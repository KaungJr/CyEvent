package coms309.roundTrip1.test.model;

import jakarta.persistence.*;

@Entity
public class EventApproval {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;
    private String isApproved;
    private String approvalReason;

    @OneToOne(mappedBy = "approval")
    private Event event;


    public EventApproval() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getApprovalReason() {
        return approvalReason;
    }

    public void setApprovalReason(String approvalReason) {
        this.approvalReason = approvalReason;
    }
}
