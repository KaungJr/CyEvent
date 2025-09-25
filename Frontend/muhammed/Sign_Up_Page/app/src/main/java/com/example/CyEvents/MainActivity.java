package com.example.CyEvents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Sign_up_Page.R;

/**
 * MainActivity is the main entry point of the application.
 * It contains buttons for navigation to different activities such as sign-in, account creation,
 * guest login, and username modification.
 * This activity manages user interactions with the main screen.
 */
public class MainActivity extends AppCompatActivity {

    // Button variables for various actions on the main screen
    Button signinButton, createAccountButton, continueAsGuestButton, modifyUsernameButton, addEventButton;

    /**
     * Called when the activity is created. This method sets up the layout and initializes the buttons.
     * It also sets up click listeners for each button to navigate to different activities.
     *
     * @param savedInstanceState The state of the activity when it was last destroyed. If this is the
     *                           first time the activity is created, this parameter will be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity

        // Initialize buttons by finding them from the layout
        signinButton = findViewById(R.id.signin);
        createAccountButton = findViewById(R.id.createAccountText);
        continueAsGuestButton = findViewById(R.id.continueAsGuestText);
        modifyUsernameButton = findViewById(R.id.modifyUsernameButton);

        // Set click listeners for each button to navigate to different activities
        setButtonClickListener(signinButton, SignInPage.class);
        setButtonClickListener(createAccountButton, CreateAccountActivity.class);
        setButtonClickListener(continueAsGuestButton, MainPageActivity.class);
        setButtonClickListener(modifyUsernameButton, ModifyUsernameActivity.class);
    }

    /**
     * Sets a click listener for a given button that navigates to the specified target activity.
     *
     * @param button The button for which the click listener is set.
     * @param targetActivity The activity to navigate to when the button is clicked.
     */
    private void setButtonClickListener(Button button, Class<?> targetActivity) {
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, targetActivity); // Create an intent to navigate
            startActivity(intent); // Start the target activity
        });
    }
}
