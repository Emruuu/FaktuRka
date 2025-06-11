package com.example.fakturka.ui.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakturka.R;
import com.example.fakturka.data.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Client> clientList = new ArrayList<>();

    public void setClients(List<Client> clients) {
        this.clientList = clients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clientList.get(position);
        holder.textName.setText(client.name);
        holder.textNip.setText("NIP: " + (client.nip != null ? client.nip : "-"));
        holder.textAddress.setText(client.address != null ? client.address : "-");
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textNip, textAddress;

        ClientViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textNip = itemView.findViewById(R.id.textNip);
            textAddress = itemView.findViewById(R.id.textAddress);
        }
    }
}

