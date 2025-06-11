package com.example.fakturka.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fakturka.data.model.Invoice;
import com.example.fakturka.data.model.InvoiceItem;

import java.util.List;

@Dao
public interface InvoiceDao {
    @Insert
    long insert(Invoice invoice);

    @Insert
    void insertItems(List<InvoiceItem> items);

    @Transaction
    @Query("SELECT * FROM invoices ORDER BY id DESC")
    List<Invoice> getAllInvoices();

    @Query("SELECT * FROM invoice_items WHERE invoiceId = :invoiceId")
    List<InvoiceItem> getItemsForInvoice(int invoiceId);
}
