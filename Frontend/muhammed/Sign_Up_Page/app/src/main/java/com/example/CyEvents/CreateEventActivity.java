package com.example.CyEvents;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Sign_up_Page.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * CreateEventActivity is an Android activity that allows the user to create a new event by filling out a form.
 * This activity collects event details such as the title, date, time, location, description, organizer ID, and approval status.
 */
public class CreateEventActivity extends AppCompatActivity {

    private EditText eventTitleInput;
    private EditText eventDateInput;
    private EditText eventTimeInput;
    private EditText eventLocationInput;
    private EditText eventDescriptionInput;
    private EditText eventOrganizerInput;
    private EditText eventApprovalStatusInput;
    private EditText eventApprovalReasonInput;
    private Button saveEventButton;

    private static final String TAG = "CreateEventActivity";
    private static final String CREATE_EVENT_URL = "http://10.90.73.72:8080/events";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize input fields and button
        eventTitleInput = findViewById(R.id.eventTitleInput);
        eventDateInput = findViewById(R.id.eventDateInput);
        eventTimeInput = findViewById(R.id.eventTimeInput);
        eventLocationInput = findViewById(R.id.eventLocationInput);
        eventDescriptionInput = findViewById(R.id.eventDescriptionInput);
        eventOrganizerInput = findViewById(R.id.eventOrganizerInput);
        eventApprovalStatusInput = findViewById(R.id.eventApprovalStatusInput);
        eventApprovalReasonInput = findViewById(R.id.eventApprovalReasonInput);
        saveEventButton = findViewById(R.id.saveEventButton);

        // Set click listener for saving event
        saveEventButton.setOnClickListener(view -> createEvent());
    }

    private void createEvent() {
        JSONObject event = new JSONObject();
        try {
            // Collect event data from input fields
            event.put("name", eventTitleInput.getText().toString().trim());
            event.put("date", eventDateInput.getText().toString().trim());
            event.put("time", eventTimeInput.getText().toString().trim());
            event.put("location", eventLocationInput.getText().toString().trim());
            event.put("description", eventDescriptionInput.getText().toString().trim());

            // Add the 'organizer' object
            String organizerIdString = eventOrganizerInput.getText().toString().trim();
            if (organizerIdString.isEmpty()) {
                Toast.makeText(this, "Organizer ID cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            long organizerId = Long.parseLong(organizerIdString);
            JSONObject organizer = new JSONObject();
            organizer.put("id", organizerId);
            event.put("organizer", organizer);

            // Add approval status and reason
            String approvalStatus = eventApprovalStatusInput.getText().toString().trim();
            String approvalReason = eventApprovalReasonInput.getText().toString().trim();
            if (approvalStatus.isEmpty() || approvalReason.isEmpty()) {
                Toast.makeText(this, "Approval status and reason cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject approval = new JSONObject();
            approval.put("isApproved", approvalStatus);
            approval.put("approvalReason", approvalReason);
            event.put("approval", approval);

            // Log the created event JSON for debugging
            Log.d(TAG, "Event JSON: " + event.toString());
        } catch (JSONException | NumberFormatException e) {
            Log.e(TAG, "Error creating event JSON: " + e.getMessage());
            Toast.makeText(this, "Error creating event JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_EVENT_URL, event,
                response -> {
                    // Handle successful event creation
                    Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Response: " + response.toString());
                    setResult(RESULT_OK); // Set result for the calling activity
                    finish(); // Close the activity
                },
                error -> {
                    // Handle error during the network request
                    String message = error.getMessage();
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String errorResponse = new String(error.networkResponse.data);
                        Log.e(TAG, "Error Status Code: " + statusCode);
                        Log.e(TAG, "Error Response: " + errorResponse);
                        message = "Error: " + statusCode + " " + errorResponse;
                    } else {
                        Log.e(TAG, "Network error: " + message);
                    }
                    Toast.makeText(this, "Error creating event: " + message, Toast.LENGTH_SHORT).show();
                });

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }
}
