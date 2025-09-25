package com.example.CyEvents;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Sign_up_Page.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for modifying a user's username.
 * Handles the UI for inputting the current and new username, checks if the username exists,
 * and updates the username if valid.
 */
public class ModifyUsernameActivity extends AppCompatActivity {

    // UI elements
    private EditText currentUsernameEditText;
    private EditText newUsernameEditText;
    private Button updateUsernameButton;

    // Request queue for making network requests
    private RequestQueue requestQueue;

    // Existing user credentials
    private String existingEmail;
    private String existingPassword;

    /**
     * Called when the activity is first created.
     * Initializes the UI components and sets up the event listener for the update button.
     *
     * @param savedInstanceState A bundle containing saved instance state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyusernameactivity);

        // Initialize UI components
        currentUsernameEditText = findViewById(R.id.currentUsernameEditText);
        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        updateUsernameButton = findViewById(R.id.updateUsernameButton);

        // Initialize the request queue
        requestQueue = Volley.newRequestQueue(this);

        // Set up the listener for the update button
        updateUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUsername = currentUsernameEditText.getText().toString();
                String newUsername = newUsernameEditText.getText().toString();

                if (!currentUsername.isEmpty() && !newUsername.isEmpty()) {
                    checkIfUsernameExists(currentUsername, newUsername);
                } else {
                    Toast.makeText(ModifyUsernameActivity.this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checks if the current username exists and if it does, proceeds to update it to the new username.
     *
     * @param currentUsername The current username to verify.
     * @param newUsername The new username to update to.
     */
    private void checkIfUsernameExists(final String currentUsername, final String newUsername) {
        String url = "http://10.90.73.72:8080/profiles/25"; // Adjust ID as needed

        Log.d("ModifyUsernameActivity", "Checking if username exists for current: " + currentUsername);

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Server Response", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Check for both flat and nested JSON response structures
                            String existingUsername;
                            if (jsonResponse.has("Profile")) {
                                JSONObject profile = jsonResponse.getJSONObject("Profile");
                                existingUsername = profile.optString("username", "N/A");
                                existingEmail = profile.optString("email", "N/A");
                                existingPassword = profile.optString("password", "N/A");
                            } else {
                                existingUsername = jsonResponse.optString("username", "N/A");
                                existingEmail = jsonResponse.optString("email", "N/A");
                                existingPassword = jsonResponse.optString("password", "N/A");
                            }

                            if (existingUsername.equals(currentUsername)) {
                                updateUsername(newUsername);
                            } else {
                                Toast.makeText(ModifyUsernameActivity.this, "Current username doesn't match", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error parsing response: " + e.getMessage());
                            Toast.makeText(ModifyUsernameActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.networkResponse != null
                                ? "Status code: " + error.networkResponse.statusCode + ", message: " + new String(error.networkResponse.data)
                                : "Unknown error";
                        Log.e("Volley Error", errorMessage);
                        Toast.makeText(ModifyUsernameActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(getRequest);
    }

    /**
     * Updates the username of the user to the new username provided.
     *
     * @param newUsername The new username to set.
     */
    private void updateUsername(final String newUsername) {
        String url = "http://10.90.73.72:8080/profiles/25"; // Adjust ID as needed

        // Create a JSON body for the PUT request
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", newUsername);
            jsonBody.put("email", existingEmail);
            jsonBody.put("password", existingPassword);
        } catch (JSONException e) {
            Toast.makeText(ModifyUsernameActivity.this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Update Response", response.toString());
                        try {
                            String message = response.optString("message", "Username updated successfully");
                            Toast.makeText(ModifyUsernameActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(ModifyUsernameActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage;
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMessage = new String(error.networkResponse.data);
                        } else {
                            errorMessage = error.getMessage() != null ? error.getMessage() : "Unknown error";
                        }
                        Log.e("Volley Error", errorMessage);
                        Toast.makeText(ModifyUsernameActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
