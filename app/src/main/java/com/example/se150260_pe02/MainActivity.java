package com.example.se150260_pe02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnUpdate, btnDelete, btnClear;
    EditText edtName, edtPrice;
    ListView listView;
    Clock selectedClock;
    ArrayList<Clock> clocks;
    ContentResolver contentResolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnClear = findViewById(R.id.btnClear);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        listView = findViewById(R.id.listView);
        clocks = new ArrayList<>();
        ArrayAdapter<Clock> adapter = new ArrayAdapter<Clock>(this, android.R.layout.simple_list_item_1, clocks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedClock = (Clock) adapterView.getItemAtPosition(i);

                edtName.setText(selectedClock.getName());
                edtPrice.setText(String.valueOf(selectedClock.getPrice()));
            }
        });

        getAll();

        Uri uriAllClock = Uri.parse("content://com.example.se150260_pe02.ClockContentProvider/Clock");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String priceS = edtPrice.getText().toString().trim();
                if (name.isEmpty() || priceS.isEmpty()) {
                    selectedClock = null;
                    Toast.makeText(getApplicationContext(), "Nothing to Add!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int price = Integer.parseInt(priceS);
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("price", price);
                Uri newRowUri = contentResolver.insert(uriAllClock, values);
                if (newRowUri == null) {
                    Toast.makeText(getApplicationContext(), "Add failed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getAll();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Add successful!", Toast.LENGTH_SHORT).show();
                clearEdt();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedClock == null) {
                    Toast.makeText(getApplicationContext(), "Select Clock before Delete!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentResolver contentResolver = getContentResolver();
                String id = String.valueOf(selectedClock.getId());
                Uri uriOneProduct = Uri.parse("content://com.example.se150260_pe02.ClockContentProvider/Clock/" + id);
                int rowDelate = contentResolver.delete(uriOneProduct, "id=?", new String[]{id});
                if (rowDelate <= 0) {
                    selectedClock = null;
                    Toast.makeText(getApplicationContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getAll();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Delete successful!", Toast.LENGTH_SHORT).show();
                clearEdt();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedClock == null) {
                    Toast.makeText(getApplicationContext(), "Please select a clock to update!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = edtName.getText().toString().trim();
                String priceS = edtPrice.getText().toString().trim();
                if (name.isEmpty() || priceS.isEmpty()) {
                    selectedClock = null;
                    Toast.makeText(getApplicationContext(), "No empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int price = Integer.parseInt(priceS);

                ContentResolver contentResolver = getContentResolver();
                String id = String.valueOf(selectedClock.getId());
                Uri uriOneProduct = Uri.parse("content://com.example.se150260_pe02.ClockContentProvider/Clock/" + id);

                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("price", price);

                int rowUpdate = contentResolver.update(uriOneProduct, values, "id=?", new String[]{id});
                if (rowUpdate <= 0) {
                    selectedClock = null;
                    Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getAll();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                clearEdt();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEdt();
            }
        });
    }

    private void getAll() {
        Uri uriAllClock = Uri.parse("content://com.example.se150260_pe02.ClockContentProvider/Clock");
        contentResolver = getContentResolver();
        clocks.clear();
        Cursor cursor = contentResolver.query(uriAllClock, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                clocks.add(new Clock(id, name, price));
            } while (cursor.moveToNext());
        }
    }

    private void clearEdt(){
        edtPrice.setText("");
        edtName.setText("");
        selectedClock = null;
    }
}