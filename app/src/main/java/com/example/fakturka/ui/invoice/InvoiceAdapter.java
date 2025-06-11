package com.example.fakturka.ui.invoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakturka.R;
import com.example.fakturka.data.model.Client;
import com.example.fakturka.data.model.Invoice;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private final List<Invoice> invoices;
    private final List<Client> clients;
    private final OnInvoiceClickListener listener;

    public interface OnInvoiceClickListener {
        void onInvoiceClick(Invoice invoice);
    }

    public InvoiceAdapter(List<Invoice> invoices, List<Client> clients, OnInvoiceClickListener listener) {
        this.invoices = invoices;
        this.clients = clients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.ViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);
        Client client = findClientById(invoice.clientId);

        holder.textInvoiceNumber.setText(invoice.number);
        holder.textInvoiceDate.setText("Data: " + invoice.date);
        holder.textClientName.setText(client != null ? client.name : "Nieznany klient");
        holder.textTotal.setText("Brutto: " + String.format("%.2f zł", invoice.totalGross));

        holder.itemView.setOnClickListener(v -> listener.onInvoiceClick(invoice));
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    private Client findClientById(int id) {
        for (Client c : clients) {
            if (c.id == id) return c;
        }
        return null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textInvoiceNumber, textInvoiceDate, textClientName, textTotal;

        ViewHolder(View itemView) {
            super(itemView);
            textInvoiceNumber = itemView.findViewById(R.id.textInvoiceNumber);
            textInvoiceDate = itemView.findViewById(R.id.textInvoiceDate);
            textClientName = itemView.findViewById(R.id.textClientName);
            textTotal = itemView.findViewById(R.id.textTotal);
        }
    }
}


