package com.example.fakturka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakturka.ui.client.AddClientActivity;
import com.example.fakturka.ui.client.ClientListActivity;
import com.example.fakturka.ui.common.SettingsActivity;
import com.example.fakturka.ui.invoice.InvoiceListActivity;
import com.example.fakturka.ui.invoice.NewInvoiceActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddClient, buttonListClients, buttonListInvoices, buttonCreateInvoice, buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FaktuRka – Panel");
        }

        buttonAddClient = findViewById(R.id.buttonAddClient);
        buttonListClients = findViewById(R.id.buttonListClients);
        buttonListInvoices = findViewById(R.id.buttonListInvoices);

        buttonAddClient.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddClientActivity.class);
            startActivity(intent);
        });

        buttonListClients.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientListActivity.class);
            startActivity(intent);
        });

        buttonListInvoices.setOnClickListener(v -> {
            Intent intent = new Intent(this, InvoiceListActivity.class);
            startActivity(intent);
        });

        buttonCreateInvoice = findViewById(R.id.buttonCreateInvoice);

        buttonCreateInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewInvoiceActivity.class);
            startActivity(intent);
        });

        buttonSettings = findViewById(R.id.buttonSettings);

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}
