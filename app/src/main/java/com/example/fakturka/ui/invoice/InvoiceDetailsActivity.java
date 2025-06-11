package com.example.fakturka.ui.invoice;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fakturka.AppDatabase;
import com.example.fakturka.R;
import com.example.fakturka.data.model.Client;
import com.example.fakturka.data.model.Invoice;
import com.example.fakturka.data.model.InvoiceItem;

import java.util.List;
import java.util.concurrent.Executors;

public class InvoiceDetailsActivity extends AppCompatActivity {

    private AppDatabase db;
    private Invoice invoice;
    private Client client;
    private List<InvoiceItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        db = AppDatabase.getInstance(this);
        int invoiceId = getIntent().getIntExtra("invoiceId", -1);

        if (invoiceId == -1) {
            finish();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            invoice = db.invoiceDao().getAllInvoices().stream()
                    .filter(i -> i.id == invoiceId).findFirst().orElse(null);
            client = db.clientDao().getClientById(invoice.clientId);
            items = db.invoiceDao().getItemsForInvoice(invoiceId);

            runOnUiThread(this::bindData);
        });

        findViewById(R.id.buttonGeneratePdf).setOnClickListener(v -> {
            // TODO: wywołaj PDF generator tutaj
            Toast.makeText(this, "Tu pójdzie generowanie PDF", Toast.LENGTH_SHORT).show();
        });
    }

    private void bindData() {
        ((TextView) findViewById(R.id.textInvoiceNumber)).setText(invoice.number);
        ((TextView) findViewById(R.id.textClientName)).setText(client.name);
        ((TextView) findViewById(R.id.textClientAddress)).setText(client.address);
        ((TextView) findViewById(R.id.textClientNip)).setText("NIP: " + client.nip);
        ((TextView) findViewById(R.id.textDate)).setText("Data wystawienia: " + invoice.date);
        ((TextView) findViewById(R.id.textDueDate)).setText("Termin płatności: " + invoice.dueDate);

        ((TextView) findViewById(R.id.textTotalNet)).setText("Netto: " + invoice.totalNet + " zł");
        ((TextView) findViewById(R.id.textTotalVat)).setText("VAT: " + invoice.totalVat + " zł");
        ((TextView) findViewById(R.id.textTotalGross)).setText("Brutto: " + invoice.totalGross + " zł");

        LinearLayout container = findViewById(R.id.positionContainer);
        for (InvoiceItem item : items) {
            TextView text = new TextView(this);
            text.setText("• " + item.description + " – " + item.quantity + " × "
                    + item.unitPrice + " zł + " + item.vatRate + "%");
            container.addView(text);
        }
    }
}
