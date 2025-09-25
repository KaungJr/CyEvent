package com.example.experiment2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextClock;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickAnchorage(View view) {
        TextClock clock = findViewById(R.id.textClock);
        clock.setTimeZone("America/Anchorage");
    }

    public void onClickLouisville(View view) {
        TextClock clock = findViewById(R.id.textClock);
        clock.setTimeZone("America/Louisville");
    }

    public void onClickCologne(View view) {
        TextClock clock = findViewById(R.id.textClock);
        clock.setTimeZone("Europe/Berlin");
    }
}