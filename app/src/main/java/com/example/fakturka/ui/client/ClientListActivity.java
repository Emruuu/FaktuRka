package com.example.fakturka.ui.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakturka.AppDatabase;
import com.example.fakturka.R;
import com.example.fakturka.data.model.Client;

import java.util.List;
import java.util.concurrent.Executors;

public class ClientListActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private ClientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        recyclerView = findViewById(R.id.recyclerClients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClientAdapter();
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getInstance(this);

        loadClients();
    }

    private void loadClients() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Client> clients = db.clientDao().getAllClients();
            runOnUiThread(() -> adapter.setClients(clients));
        });
    }
}
