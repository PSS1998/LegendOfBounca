package com.example.cpsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button switchToGyroscopeActivity;
    private Button switchToGravityActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchToGyroscopeActivity = findViewById(R.id.gyroscope);
        switchToGravityActivity = findViewById(R.id.gravity);
        switchToGyroscopeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(GyroscopeActivity.class);
            }
        });

        switchToGravityActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(GravityActivity.class);
            }
        });
    }

    private void switchActivities(Class<?> activityClass) {
        Intent switchActivityIntent = new Intent(this, activityClass);
        startActivity(switchActivityIntent);
    }




}