package com.example.cyevents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {
    private Button interest1;
    private Button interest2;
    private Button interest3;
    private Button interest4;
    private Button interest5;
    private Button interest6;
    private Button interest7;
    private Button interest8;

    private TextView backBtn;
    private Button finishBtn;

    private ArrayList<String> interests;

    private static final String postURL = "https://36356748-026b-4950-8df9-247002157157.mock.pstmn.io";

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_interest);

        interest1 = findViewById(R.id.interest1);
        interest2 = findViewById(R.id.interest2);
        interest3 = findViewById(R.id.interest3);
        interest4 = findViewById(R.id.interest4);
        interest5 = findViewById(R.id.interest5);
        interest6 = findViewById(R.id.interest6);
        interest7 = findViewById(R.id.interest7);
        interest8 = findViewById(R.id.interest8);

        backBtn = findViewById(R.id.backBtn);
        finishBtn = findViewById(R.id.finishBtn);

        interests = new ArrayList<String>();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InterestActivity.this, SignUpActivity.class));
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postInterests();
                Toast.makeText(InterestActivity.this, "Finished Account Creation", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InterestActivity.this, MainActivity.class));
            }
        });

        interest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 1");
            }
        });

        interest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 2");
            }
        });

        interest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 3");
            }
        });

        interest4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 4");
            }
        });

        interest5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 5");
            }
        });

        interest6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 6");
            }
        });

        interest7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 7");
            }
        });

        interest8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interests.add("Interest 8");
            }
        });
    }

    private void postInterests() {
    }
}
