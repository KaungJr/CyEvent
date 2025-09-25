package com.example.CyEvents;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link SignInPage} class represents the sign-in screen where users can log into the application.
 * It validates user input, handles login attempts, and provides options for creating an account or continuing as a guest.
 */
public class SignInPage extends AppCompatActivity {

    // UI Components
    private EditText username, password;
    private Button signinButton, createAccountButton, continueAsGuestButton;
    private CheckBox showPasswordToggle;

    // API URL
    private static String URL = "http://10.90.73.72:8080/profiles/25";

    /**
     * Initializes the activity by setting up UI components and defining the behavior for the buttons and other interactive elements.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the data it most recently supplied in {@link #onSaveInstanceState(Bundle)}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinpage);

        // Initialize UI components
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signinButton = findViewById(R.id.signin);
        createAccountButton = findViewById(R.id.createAccountText);
        continueAsGuestButton = findViewById(R.id.continueAsGuestText);
        showPasswordToggle = findViewById(R.id.showPasswordToggle);

        // Define onClick behavior for the sign-in button
        signinButton.setOnClickListener(view -> {
            String usernameInput = username.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if (!usernameInput.isEmpty() && !passwordInput.isEmpty()) {
                loginUser(usernameInput, passwordInput);
            } else {
                Toast.makeText(SignInPage.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Define onClick behavior for the continue as guest button
        continueAsGuestButton.setOnClickListener(view -> startActivity(new Intent(SignInPage.this, MainPageActivity.class)));

        // Define onClick behavior for the create account button
        createAccountButton.setOnClickListener(view -> startActivity(new Intent(SignInPage.this, CreateAccountActivity.class)));

        // Define behavior for show password toggle checkbox
        showPasswordToggle.setOnClickListener(v -> {
            if (showPasswordToggle.isChecked()) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            password.setSelection(password.getText().length());
        });
    }

    /**
     * Attempts to log in the user by sending a GET request to the specified URL and validating the entered credentials.
     * If the login is successful, the user is redirected to the main page. If failed, a message is shown.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    private void loginUser(final String username, final String password) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                response -> {
                    try {
                        // Get the existing username and password from the response
                        String existingUsername = response.getString("username");
                        String existingPassword = response.getString("password");

                        // Check if the username and password match
                        if (existingUsername.equals(username) && existingPassword.equals(password)) {
                            Toast.makeText(SignInPage.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInPage.this, MainPageActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignInPage.this, "Login Failed: Invalid username or password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignInPage.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VolleyError", "Error: " + error.toString());
                    String errorMessage = "";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMessage = new String(error.networkResponse.data);
                    } else if (error.getMessage() != null) {
                        errorMessage = error.getMessage();
                    } else {
                        errorMessage = "Unknown error occurred";
                    }
                    Log.e("VolleyError", errorMessage);
                    Toast.makeText(SignInPage.this, errorMessage, Toast.LENGTH_SHORT).show();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
