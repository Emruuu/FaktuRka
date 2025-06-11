package com.example.fakturka.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoices")
public class Invoice {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String number;
    public String date;
    public String dueDate;

    public int clientId;  // foreign key – klient
    public double totalNet;
    public double totalVat;
    public double totalGross;
}
