package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.os.Bundle;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {

    private TextView title;
    private TextView name;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        title = findViewById(R.id.Title);
        name = findViewById(R.id.NameYear);
        version = findViewById(R.id.Version);

        title.setText("Android Notes");
        name.setText("@ 2021, Wasif Ahmad");
        version.setText("Version 1.0");

    }
}