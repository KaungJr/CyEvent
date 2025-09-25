package com.example.CyEvents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Sign_up_Page.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ProfileActivity is an activity that handles the user profile screen. It displays the user's profile data such as username and password,
 * and provides options to edit the username, interests, log out, and delete the account.
 * The class interacts with a remote server to load, update, or delete user data using Volley HTTP requests.
 */
public class ProfileActivity extends AppCompatActivity {

    // UI components
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView editUsernameTextView;
    private TextView editInterestsTextView;
    private Button logOutButton;
    private Button deleteAccountButton;

    // Request queue for network operations
    private RequestQueue requestQueue;

    // User profile data
    private String existingEmail;
    private String existingPassword;

    /**
     * Called when the activity is created. It initializes the UI components and sets up listeners for actions like
     * editing the username, interests, logging out, and deleting the account.
     *
     * @param savedInstanceState The saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        usernameTextView = findViewById(R.id.usernameTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        editUsernameTextView = findViewById(R.id.editUsernameTextView);
        editInterestsTextView = findViewById(R.id.editInterestsTextView);
        logOutButton = findViewById(R.id.logOutButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Load profile data from the server
        loadProfile();

        // Set listeners for edit and action buttons
        editUsernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ModifyUsernameActivity.class);
                startActivity(intent);
            }
        });

        editInterestsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }

    /**
     * Loads the profile data from the server and displays it in the UI.
     * The method checks if the response contains a nested "Profile" object or a flat structure.
     * It also handles errors such as JSON parsing issues or network errors.
     */
    private void loadProfile() {
        String url = "http://10.90.73.72:8080/profiles/25"; // Adjust endpoint or profile ID as needed

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API Response", response);  // Log the raw response for debugging
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Check if JSON has a "Profile" object or is flat
                            if (jsonResponse.has("Profile")) {
                                JSONObject profile = jsonResponse.getJSONObject("Profile");
                                String username = profile.optString("username", "N/A");
                                existingEmail = profile.optString("email", "N/A");
                                existingPassword = profile.optString("password", "N/A");

                                usernameTextView.setText("Username: " + username);
                                passwordTextView.setText("Password: ••••••••");  // Obfuscate password
                            } else {
                                // Assume flat structure if "Profile" object is not present
                                String username = jsonResponse.optString("username", "N/A");
                                existingEmail = jsonResponse.optString("email", "N/A");
                                existingPassword = jsonResponse.optString("password", "N/A");

                                usernameTextView.setText("Username: " + username);
                                passwordTextView.setText("Password: ••••••••");  // Obfuscate password
                            }

                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error parsing response: " + e.getMessage());
                            Toast.makeText(ProfileActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage() != null ? error.getMessage() : "Unknown error";
                        Log.e("Volley Error", errorMessage);
                        Toast.makeText(ProfileActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(getRequest);
    }

    /**
     * Handles the log out action. Displays a toast confirming successful logout and redirects to the SignInPage activity.
     */
    private void logOut() {
        Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, SignInPage.class);
        startActivity(intent);
        finish();
    }

    /**
     * Deletes the user's account by sending a DELETE request to the server. On success, it redirects the user to the SignInPage.
     * It also handles error responses such as network issues or server errors.
     */
    private void deleteAccount() {
        String url = "http://10.90.73.72:8080/profiles/25"; // Adjust endpoint or profile ID as needed

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, SignInPage.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage() != null ? error.getMessage() : "Unknown error";
                        Log.e("Volley Error", errorMessage);
                        Toast.makeText(ProfileActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(deleteRequest);
    }
}
