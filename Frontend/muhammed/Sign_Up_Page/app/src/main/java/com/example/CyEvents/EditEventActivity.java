package com.example.CyEvents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Sign_up_Page.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * EditEventActivity class allows users to update or delete event details.
 * It provides an interface to edit the event's name, date, time, location, description, and organizer.
 * This activity is used to update existing events or delete them from the backend.
 */
public class EditEventActivity extends AppCompatActivity {

    // UI elements for event input fields and buttons
    private EditText eventTitleInput, eventDateInput, eventTimeInput, eventLocationInput, eventDescriptionInput, eventOrganizerInput;
    private Button saveEventButton, deleteEventButton;
    private EditText eventApprovalStatusInput, eventApprovalReasonInput;

    // Variable to store the event ID
    private int eventId;

    /**
     * Initializes the activity and sets up the UI elements.
     * Also sets the data from the Intent to populate the event details for editing.
     * @param savedInstanceState Bundle containing the previous state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Initialize UI elements
        eventTitleInput = findViewById(R.id.eventTitleInput);
        eventDateInput = findViewById(R.id.eventDateInput);
        eventTimeInput = findViewById(R.id.eventTimeInput);
        eventLocationInput = findViewById(R.id.eventLocationInput);
        eventDescriptionInput = findViewById(R.id.eventDescriptionInput);
        eventOrganizerInput = findViewById(R.id.eventOrganizerInput);
        eventApprovalStatusInput = findViewById(R.id.eventApprovalStatusInput);
        eventApprovalReasonInput = findViewById(R.id.eventApprovalReasonInput);
        saveEventButton = findViewById(R.id.saveEventButton);
        deleteEventButton = findViewById(R.id.deleteEventButton);

        // Retrieve event details passed via Intent
        Intent intent = getIntent();
        eventId = intent.getIntExtra("eventId", -1);
        String eventTitle = intent.getStringExtra("eventTitle");
        String eventDate = intent.getStringExtra("eventDate");
        String eventTime = intent.getStringExtra("eventTime");
        String eventLocation = intent.getStringExtra("eventLocation");
        String eventDescription = intent.getStringExtra("eventDescription");
        String eventOrganizer = intent.getStringExtra("eventOrganizer");
        String approvalStatus = intent.getStringExtra("approvalStatus");
        String approvalReason = intent.getStringExtra("approvalReason");

        // Populate the input fields with the current event details
        eventTitleInput.setText(eventTitle);
        eventDateInput.setText(eventDate);
        eventTimeInput.setText(eventTime);
        eventLocationInput.setText(eventLocation);
        eventDescriptionInput.setText(eventDescription);
        eventOrganizerInput.setText(eventOrganizer);
        eventApprovalStatusInput.setText(approvalStatus);  // Set approval status
        eventApprovalReasonInput.setText(approvalReason);  // Set approval reason

        // Set up listeners for buttons
        saveEventButton.setOnClickListener(v -> updateEvent(eventId));
        deleteEventButton.setOnClickListener(v -> deleteEvent(eventId));
    }

    /**
     * Sends a request to update the event details on the backend.
     * Updates the event using the provided event ID.
     * @param id The event ID to be updated.
     */
    private void updateEvent(int id) {
        String url = "http://10.90.73.72:8080/events/98"; // Use the actual event ID in the URL

        try {
            // Validate inputs
            if (eventTitleInput.getText().toString().trim().isEmpty() ||
                    eventDateInput.getText().toString().trim().isEmpty() ||
                    eventTimeInput.getText().toString().trim().isEmpty() ||
                    eventLocationInput.getText().toString().trim().isEmpty() ||
                    eventDescriptionInput.getText().toString().trim().isEmpty() ||
                    eventOrganizerInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create JSON object with the new structure expected by the backend
            JSONObject event = new JSONObject();
            event.put("name", eventTitleInput.getText().toString().trim());
            event.put("description", eventDescriptionInput.getText().toString().trim());
            event.put("date", eventDateInput.getText().toString().trim());
            event.put("time", eventTimeInput.getText().toString().trim());
            event.put("location", eventLocationInput.getText().toString().trim());

            // Add organizer details (id)
            try {
                int organizerId = Integer.parseInt(eventOrganizerInput.getText().toString().trim());
                JSONObject organizer = new JSONObject();
                organizer.put("id", organizerId);
                event.put("organizer", organizer);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid organizer ID", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add approval details from the input fields
            String approvalStatus = eventApprovalStatusInput.getText().toString().trim();
            String approvalReason = eventApprovalReasonInput.getText().toString().trim();

            // Check if approval status or reason is empty
            if (approvalStatus.isEmpty() || approvalReason.isEmpty()) {
                Toast.makeText(this, "Please provide approval status and reason", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject approval = new JSONObject();
            approval.put("isApproved", approvalStatus);
            approval.put("approvalReason", approvalReason);
            event.put("approval", approval);

            // Send PUT request
            saveEventButton.setEnabled(false);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, event,
                    response -> {
                        saveEventButton.setEnabled(true);
                        Toast.makeText(EditEventActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> {
                        saveEventButton.setEnabled(true);
                        String errorMessage = error.getMessage();
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMessage = new String(error.networkResponse.data);
                        }
                        Log.e("UpdateEventError", errorMessage);
                        Toast.makeText(EditEventActivity.this, "Error updating event: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });

            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating JSON object", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends a request to delete the event from the backend.
     * @param id The event ID to be deleted.
     */
    private void deleteEvent(int id) {
        String url = "http://10.90.73.72:8080/events/97"; // Use the actual event ID in the URL

        // Show loading message
        deleteEventButton.setEnabled(false); // Disable button to prevent multiple requests

        // Send a DELETE request to remove the event
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    deleteEventButton.setEnabled(true); // Re-enable button after request is complete
                    Toast.makeText(EditEventActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close this activity and return to the previous one
                },
                error -> {
                    deleteEventButton.setEnabled(true);
                    String errorMessage = error.getMessage();
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage = new String(error.networkResponse.data);
                    }
                    // Log the error details
                    Log.e("DeleteEventError", errorMessage);
                    Toast.makeText(EditEventActivity.this, "Error deleting event: " + errorMessage, Toast.LENGTH_SHORT).show();
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
