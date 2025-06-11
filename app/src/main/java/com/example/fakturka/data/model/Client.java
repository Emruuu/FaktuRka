package com.example.fakturka.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clients")
public class Client {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    public String nip;
    public String address;
}
