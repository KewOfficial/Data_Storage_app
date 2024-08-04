package com.example.datastorageapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    private DatabaseManager dbManager;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dataList;
    private ArrayList<Long> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Enable the "up" button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbManager = new DatabaseManager(this);
        dbManager.open();

        ListView listView = findViewById(R.id.listView);
        dataList = new ArrayList<>();
        idList = new ArrayList<>();

        Cursor cursor = dbManager.fetchData();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
            dataList.add("Name: " + name + ", Age: " + age);
            idList.add(id);
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(DisplayActivity.this)
                    .setTitle("Delete Entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        dbManager.deleteData(idList.get(position));
                        dataList.remove(position);
                        idList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(DisplayActivity.this, "Entry deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
