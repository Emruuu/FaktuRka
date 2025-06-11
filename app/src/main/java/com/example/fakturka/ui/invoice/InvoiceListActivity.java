package com.example.fakturka.ui.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakturka.AppDatabase;
import com.example.fakturka.R;
import com.example.fakturka.data.model.Client;
import com.example.fakturka.data.model.Invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class InvoiceListActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private List<Invoice> invoices = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private InvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        recyclerView = findViewById(R.id.recyclerInvoices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(this);

        loadData();
    }

    private void loadData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            invoices = db.invoiceDao().getAllInvoices();
            clients = db.clientDao().getAllClients();

            runOnUiThread(() -> {
                adapter = new InvoiceAdapter(invoices, clients, invoice -> {
                    Intent intent = new Intent(InvoiceListActivity.this, InvoiceDetailsActivity.class);
                    intent.putExtra("invoiceId", invoice.id);
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            });
        });
    }
}
