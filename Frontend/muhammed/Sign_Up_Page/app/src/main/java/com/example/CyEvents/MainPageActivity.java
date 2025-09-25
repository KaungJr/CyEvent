package com.example.CyEvents;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.Sign_up_Page.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private Button addEventButton;
    private RecyclerView eventsRecyclerView;
    private EventsAdapter eventsAdapter;
    private List<Event> eventsList = new ArrayList<>();

    private Button navMessages;
    private Button navProfile;
    private EditText searchBar;
    private Button searchButton;

    private Spinner sortSpinner; // Spinner for selecting sorting option

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        // Initialize UI components
        addEventButton = findViewById(R.id.addEventButton);
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        LinearLayout navMessages = findViewById(R.id.navMessages);
        LinearLayout navProfile = findViewById(R.id.navProfile);
        searchBar = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        sortSpinner = findViewById(R.id.sortSpinner); // Initialize sort spinner

        // Set up RecyclerView with EventsAdapter
        eventsAdapter = new EventsAdapter(eventsList);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventsAdapter);

        // Set up button listeners
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, CreateEventActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        navMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, UserChatActivity.class);
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        eventsAdapter.setOnItemClickListener(new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event, int position) {
                Intent intent = new Intent(MainPageActivity.this, EditEventActivity.class);
                intent.putExtra("eventPosition", position);
                intent.putExtra("eventTitle", event.getTitle());
                intent.putExtra("eventDate", event.getDate());
                intent.putExtra("eventTime", event.getTime());
                intent.putExtra("eventLocation", event.getLocation());
                intent.putExtra("eventDescription", event.getDescription());
                intent.putExtra("eventOrganizer", event.getOrganizerName());
                startActivityForResult(intent, 2);
            }
        });

        // Load events without sorting initially
        loadEventsWithoutSorting();

        // Search button listener
        // Search button listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBar.getText().toString().trim();
                if (query.isEmpty()) {
                    // If the search field is empty, load all events
                    loadEventsWithoutSorting();
                } else {
                    // Search for events matching the query
                    searchEvents(query);
                }
            }
        });


        // Spinner listener for sorting events
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
                if (selectedOption.equals("Date")) {
                    sortEvents("date"); // Sort by date
                } else if (selectedOption.equals("Time")) {
                    sortEvents("time"); // Sort by time
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadEventsWithoutSorting(); // Refresh events list after adding new event
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            int eventPosition = data.getIntExtra("eventPosition", -1);
            if (data.getBooleanExtra("deleteEvent", false)) {
                if (eventPosition >= 0 && eventPosition < eventsList.size()) {
                    eventsList.remove(eventPosition);
                    eventsAdapter.notifyDataSetChanged();
                }
            } else {
                String updatedTitle = data.getStringExtra("eventTitle");
                String updatedDate = data.getStringExtra("eventDate");
                String updatedTime = data.getStringExtra("eventTime");
                String updatedLocation = data.getStringExtra("eventLocation");
                String updatedDescription = data.getStringExtra("eventDescription");
                String updatedOrganizer = data.getStringExtra("eventOrganizer");

                Event updatedEvent = new Event(updatedTitle, updatedDate, updatedTime, updatedLocation, updatedDescription, updatedOrganizer, "Not Approved", "No Reason");

                if (eventPosition >= 0 && eventPosition < eventsList.size()) {
                    eventsList.set(eventPosition, updatedEvent);
                    eventsAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    // Method to load events without any sorting
    private void loadEventsWithoutSorting() {
        String url = "http://10.90.73.72:8080/events";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        eventsList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject eventJson = response.getJSONObject(i);
                                String title = eventJson.getString("name");
                                String date = eventJson.getString("date");
                                String time = eventJson.getString("time");
                                String location = eventJson.getString("location");
                                String description = eventJson.getString("description");

                                String organizerName = eventJson.isNull("organizer") ? "Unknown" : eventJson.getJSONObject("organizer").getString("username");

                                // Retrieve approval status and reason
                                String approvalStatus = eventJson.isNull("approvalStatus") ? "Not Approved" : eventJson.getString("approvalStatus");
                                String approvalReason = eventJson.isNull("approvalReason") ? "No Reason" : eventJson.getString("approvalReason");

                                // Create Event object
                                Event event = new Event(title, date, time, location, description, organizerName, approvalStatus, approvalReason);

                                event.setApprovalStatus(approvalStatus);
                                event.setApprovalReason(approvalReason);

                                eventsList.add(event);
                            } catch (JSONException e) {
                                Log.e("LoadEvents", "JSON Exception: " + e.getMessage());
                            }
                        }
                        // Reverse the list to show the newest events first
                        Collections.reverse(eventsList);
                        eventsAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = (error.networkResponse != null && error.networkResponse.data != null)
                                ? new String(error.networkResponse.data)
                                : error.getMessage();
                        Toast.makeText(MainPageActivity.this, "Error fetching events: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("LoadEvents", "Error: " + errorMessage);
                    }
                });

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    // Method to search for events based on the search term
    // Method to search events based on a name query
    private void searchEvents(String query) {
        String baseUrl = "http://coms-3090-041.class.las.iastate.edu:8080/events/name/"; // Base URL
        String url = baseUrl + query; // Dynamically construct the URL

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        eventsList.clear(); // Clear the current list before adding search results
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject eventJson = response.getJSONObject(i);
                                String title = eventJson.getString("name");
                                String date = eventJson.getString("date");
                                String time = eventJson.getString("time");
                                String location = eventJson.getString("location");
                                String description = eventJson.getString("description");

                                String organizerName = eventJson.isNull("organizer") ? "Unknown" : eventJson.getJSONObject("organizer").getString("username");

                                // Retrieve approval status and reason
                                String approvalStatus = eventJson.isNull("approvalStatus") ? "Not Approved" : eventJson.getString("approvalStatus");
                                String approvalReason = eventJson.isNull("approvalReason") ? "No Reason" : eventJson.getString("approvalReason");

                                // Create Event object
                                Event event = new Event(title, date, time, location, description, organizerName, approvalStatus, approvalReason);
                                eventsList.add(event);
                            } catch (JSONException e) {
                                Log.e("SearchEvents", "JSON Exception: " + e.getMessage());
                            }
                        }
                        // Notify the adapter to update the RecyclerView
                        eventsAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SearchEvents", "Error: " + error.toString());
                        Toast.makeText(MainPageActivity.this, "Error fetching search results", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }


    // Method to sort events based on selected sorting option (by date or time)
    private void sortEvents(String sortBy) {
        // Dynamically construct the URL based on the sortBy parameter
        String url = "http://coms-3090-041.class.las.iastate.edu:8080/events/sort/all?order=" + sortBy;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        eventsList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject eventJson = response.getJSONObject(i);
                                String title = eventJson.getString("name");
                                String date = eventJson.getString("date");
                                String time = eventJson.getString("time");
                                String location = eventJson.getString("location");
                                String description = eventJson.getString("description");

                                String organizerName = eventJson.isNull("organizer") ? "Unknown" : eventJson.getJSONObject("organizer").getString("username");

                                // Retrieve approval status and reason
                                String approvalStatus = eventJson.isNull("approvalStatus") ? "Not Approved" : eventJson.getString("approvalStatus");
                                String approvalReason = eventJson.isNull("approvalReason") ? "No Reason" : eventJson.getString("approvalReason");

                                // Create Event object
                                Event event = new Event(title, date, time, location, description, organizerName, approvalStatus, approvalReason);
                                eventsList.add(event);
                            } catch (JSONException e) {
                                Log.e("SortEvents", "JSON Exception: " + e.getMessage());
                            }
                        }
                        // Reverse the list to show the newest events first
                        Collections.reverse(eventsList);
                        eventsAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SortEvents", "Error: " + error.toString());
                        Toast.makeText(MainPageActivity.this, "Error fetching sorted events", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }


}