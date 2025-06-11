package com.example.fakturka.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fakturka.data.model.Client;

import java.util.List;

@Dao
public interface ClientDao {
    @Insert
    long insert(Client client);

    @Update
    void update(Client client);

    @Delete
    void delete(Client client);

    @Query("SELECT * FROM clients ORDER BY name ASC")
    List<Client> getAllClients();

    @Query("SELECT * FROM clients WHERE id = :id")
    Client getClientById(int id);
}
