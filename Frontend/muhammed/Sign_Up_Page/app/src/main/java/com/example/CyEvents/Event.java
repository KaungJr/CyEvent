package com.example.CyEvents;

/**
 * Event class represents an event with details such as title, date, time, location, description, and organizer's name.
 * This class provides methods to get the event's attributes.
 */
public class Event {

    private String title;
    private String date;
    private String time;
    private String location;
    private String description;
    private String organizerName; // Variable for event organizer's name
    private String approvalStatus; // Approval status
    private String approvalReason; // Approval reason

    /**
     * Constructor to initialize the event with the provided details.
     *
     * @param title The title of the event.
     * @param date The date of the event.
     * @param time The time of the event.
     * @param location The location of the event.
     * @param description A description of the event.
     * @param organizerName The name of the organizer of the event.
     *
     */
    public Event(String title, String date, String time, String location, String description, String organizerName, String approvalStatus, String approvalReason) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.organizerName = organizerName;
        this.approvalStatus = approvalStatus;
        this.approvalReason = approvalReason;
    }


    /**
     * Returns the title of the event.
     *
     * @return The title of the event.
     */
    public String getTitle() {
        return title; // Corrected getter method
    }

    /**
     * Returns the date of the event.
     *
     * @return The date of the event.
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the time of the event.
     *
     * @return The time of the event.
     */
    public String getTime() {
        return time;
    }

    /**
     * Returns the location of the event.
     *
     * @return The location of the event.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the description of the event.
     *
     * @return The description of the event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the approval status of the event.
     *
     * @param approvalStatus The approval status to set.
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * Sets the approval reason for the event.
     *
     * @param approvalReason The approval reason to set.
     */
    public void setApprovalReason(String approvalReason) {
        this.approvalReason = approvalReason;
    }



    /**
     * Returns the name of the organizer of the event.
     *
     * @return The name of the organizer.
     */
    public String getOrganizerName() {
        return organizerName; // Getter for organizer's name

    }


    public String getApprovalStatus() {
        return approvalStatus;
    }

    public String getApprovalReason() {
        return approvalReason;
    }
}
