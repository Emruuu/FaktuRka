package com.example.fakturka.ui.common;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakturka.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsActivity extends AppCompatActivity {

    private EditText editName, editAddress, editNip;
    private static final int PICK_LOGO_REQUEST = 1001;
    private Button buttonSelectLogo;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editName = findViewById(R.id.editIssuerName);
        editAddress = findViewById(R.id.editIssuerAddress);
        editNip = findViewById(R.id.editIssuerNip);
        prefs = getSharedPreferences("issuer_prefs", MODE_PRIVATE);

        loadData();

        findViewById(R.id.buttonSaveIssuer).setOnClickListener(v -> {
            prefs.edit()
                    .putString("name", editName.getText().toString())
                    .putString("address", editAddress.getText().toString())
                    .putString("nip", editNip.getText().toString())
                    .apply();
            Toast.makeText(this, "Zapisano dane wystawcy", Toast.LENGTH_SHORT).show();
            finish(); // zamknij aktywność po zapisie
        });

        buttonSelectLogo = findViewById(R.id.buttonSelectLogo);
        buttonSelectLogo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Wybierz logo"), PICK_LOGO_REQUEST);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_LOGO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                String path = copyLogoToInternalStorage(imageUri);
                if (path != null) {
                    prefs.edit().putString("logoPath", path).apply();
                    Toast.makeText(this, "Logo zapisane", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadData() {
        editName.setText(prefs.getString("name", ""));
        editAddress.setText(prefs.getString("address", ""));
        editNip.setText(prefs.getString("nip", ""));
    }
    private String copyLogoToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File logoFile = new File(getFilesDir(), "logo.png");
            OutputStream outputStream = new FileOutputStream(logoFile);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return logoFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
