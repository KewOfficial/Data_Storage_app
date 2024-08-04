package com.example.datastorageapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DatabaseManager(this);
        dbManager.open();

        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText ageEditText = findViewById(R.id.ageEditText);
        Button insertButton = findViewById(R.id.insertButton);
        Button viewButton = findViewById(R.id.viewButton);

        insertButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String ageString = ageEditText.getText().toString();
            if (!name.isEmpty() && !ageString.isEmpty()) {
                int age = Integer.parseInt(ageString);
                dbManager.insertData(name, age);
                Toast.makeText(MainActivity.this, "Data has been inserted successfully", Toast.LENGTH_SHORT).show();
                nameEditText.setText("");
                ageEditText.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Please enter both name and age", Toast.LENGTH_SHORT).show();
            }
        });

        viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}
