package com.example.fakturka.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoice_items")
public class InvoiceItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int invoiceId; // foreign key – faktura
    public String description;
    public int quantity;
    public double unitPrice;
    public double vatRate;
}
