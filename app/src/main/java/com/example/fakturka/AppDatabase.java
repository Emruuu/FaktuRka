package com.example.fakturka;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fakturka.data.dao.ClientDao;
import com.example.fakturka.data.dao.InvoiceDao;
import com.example.fakturka.data.model.Client;
import com.example.fakturka.data.model.Invoice;
import com.example.fakturka.data.model.InvoiceItem;

@Database(entities = {Client.class, Invoice.class, InvoiceItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ClientDao clientDao();
    public abstract InvoiceDao invoiceDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fakturka_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
