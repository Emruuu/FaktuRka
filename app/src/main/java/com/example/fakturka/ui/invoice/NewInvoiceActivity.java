package com.example.fakturka.ui.invoice;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.fakturka.data.model.InvoiceItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class NewInvoiceActivity extends AppCompatActivity {

    private Spinner spinnerClients;
    private EditText editDate;
    private RecyclerView recyclerView;
    private InvoiceItemAdapter adapter;
    private List<InvoiceItem> itemList = new ArrayList<>();
    private List<Client> clients;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice);

        db = AppDatabase.getInstance(this);

        spinnerClients = findViewById(R.id.spinnerClients);
        editDate = findViewById(R.id.editDate);
        recyclerView = findViewById(R.id.recyclerItems);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editDate.setText(LocalDate.now().toString());
        }

        adapter = new InvoiceItemAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.buttonAddItem).setOnClickListener(v -> {
            itemList.add(new InvoiceItem());
            adapter.notifyItemInserted(itemList.size() - 1);
        });

        findViewById(R.id.buttonSaveInvoice).setOnClickListener(v -> saveInvoice());

        loadClients();
    }

    private void loadClients() {
        Executors.newSingleThreadExecutor().execute(() -> {
            clients = db.clientDao().getAllClients();
            runOnUiThread(() -> {
                ArrayAdapter<Client> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, clients);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClients.setAdapter(adapter);
            });
        });
    }

    private void saveInvoice() {
        int clientIndex = spinnerClients.getSelectedItemPosition();
        if (clientIndex < 0 || itemList.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie dane", Toast.LENGTH_SHORT).show();
            return;
        }

        Client client = clients.get(clientIndex);
        Invoice invoice = new Invoice();
        invoice.clientId = client.id;
        invoice.date = editDate.getText().toString();
        invoice.dueDate = invoice.date;
        invoice.number = "FV/" + System.currentTimeMillis();

        double net = 0, vat = 0;
        for (InvoiceItem item : itemList) {
            net += item.unitPrice * item.quantity;
            vat += (item.unitPrice * item.quantity * item.vatRate / 100.0);
        }
        invoice.totalNet = net;
        invoice.totalVat = vat;
        invoice.totalGross = net + vat;

        Executors.newSingleThreadExecutor().execute(() -> {
            long invoiceId = db.invoiceDao().insert(invoice);
            for (InvoiceItem item : itemList) item.invoiceId = (int) invoiceId;
            db.invoiceDao().insertItems(itemList);
            runOnUiThread(() -> {
                Toast.makeText(this, "Zapisano fakturę", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
