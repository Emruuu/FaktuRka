package com.example.fakturka.ui.client;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakturka.AppDatabase;
import com.example.fakturka.R;
import com.example.fakturka.data.model.Client;

import java.util.concurrent.Executors;

public class AddClientActivity extends AppCompatActivity {

    private EditText editName, editNip, editAddress;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        editName = findViewById(R.id.editName);
        editNip = findViewById(R.id.editNip);
        editAddress = findViewById(R.id.editAddress);
        Button buttonSave = findViewById(R.id.buttonSave);

        db = AppDatabase.getInstance(this);

        buttonSave.setOnClickListener(v -> saveClient());
    }

    private void saveClient() {
        String name = editName.getText().toString().trim();
        String nip = editNip.getText().toString().trim();
        String address = editAddress.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Podaj nazwę firmy", Toast.LENGTH_SHORT).show();
            return;
        }

        Client client = new Client();
        client.name = name;
        client.nip = nip;
        client.address = address;

        // ⛔ Room NIE pozwala zapisywać na głównym wątku, więc:
        Executors.newSingleThreadExecutor().execute(() -> {
            db.clientDao().insert(client);
            runOnUiThread(() -> {
                Toast.makeText(this, "Zapisano klienta", Toast.LENGTH_SHORT).show();
                finish(); // wróć do listy
            });
        });
    }
}
