package com.example.fakturka.ui.invoice;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakturka.R;
import com.example.fakturka.data.model.InvoiceItem;

import java.util.List;

public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.ViewHolder> {

    private final List<InvoiceItem> itemList;

    public InvoiceItemAdapter(List<InvoiceItem> items) {
        this.itemList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceItem item = itemList.get(position);

        holder.editDescription.setText(item.description);
        holder.editQuantity.setText(String.valueOf(item.quantity));
        holder.editUnitPrice.setText(String.valueOf(item.unitPrice));

        ArrayAdapter<CharSequence> vatAdapter = ArrayAdapter.createFromResource(holder.itemView.getContext(),
                R.array.vat_options, android.R.layout.simple_spinner_item);
        vatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerVat.setAdapter(vatAdapter);

        int vatIndex = vatAdapter.getPosition(String.valueOf((int) item.vatRate));
        if (vatIndex >= 0) holder.spinnerVat.setSelection(vatIndex);

        holder.spinnerVat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                item.vatRate = Double.parseDouble(parent.getItemAtPosition(pos).toString());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        TextWatcher watcher = new SimpleTextWatcher() {
            public void afterTextChanged(Editable s) {
                item.description = holder.editDescription.getText().toString();
                item.unitPrice = parseDouble(holder.editUnitPrice.getText().toString());
                item.quantity = (int) parseDouble(holder.editQuantity.getText().toString());
            }
        };

        holder.editDescription.addTextChangedListener(watcher);
        holder.editUnitPrice.addTextChangedListener(watcher);
        holder.editQuantity.addTextChangedListener(watcher);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editDescription, editQuantity, editUnitPrice;
        Spinner spinnerVat;

        ViewHolder(View itemView) {
            super(itemView);
            editDescription = itemView.findViewById(R.id.editDescription);
            editQuantity = itemView.findViewById(R.id.editQuantity);
            editUnitPrice = itemView.findViewById(R.id.editUnitPrice);
            spinnerVat = itemView.findViewById(R.id.spinnerVat);
        }
    }

    private double parseDouble(String text) {
        try { return Double.parseDouble(text); } catch (Exception e) { return 0.0; }
    }

    abstract static class SimpleTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}

