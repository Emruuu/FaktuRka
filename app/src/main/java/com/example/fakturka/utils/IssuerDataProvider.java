package com.example.fakturka.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class IssuerDataProvider {
    private SharedPreferences prefs;

    public IssuerDataProvider(Context context) {
        prefs = context.getSharedPreferences("issuer_prefs", Context.MODE_PRIVATE);
    }

    public String getName() {
        return prefs.getString("name", "Nazwa firmy");
    }

    public String getAddress() {
        return prefs.getString("address", "Adres firmy");
    }

    public String getNip() {
        return prefs.getString("nip", "NIP");
    }

    public String getLogoPath() {
        return prefs.getString("logoPath", null);
    }
}
