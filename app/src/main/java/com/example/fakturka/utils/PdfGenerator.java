package com.example.fakturka.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.widget.Toast;

import com.example.fakturka.data.model.Client;
import com.example.fakturka.data.model.Invoice;
import com.example.fakturka.data.model.InvoiceItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {

    public static void generate(Context context, Invoice invoice, Client client, List<InvoiceItem> items) {
        IssuerDataProvider issuer = new IssuerDataProvider(context);
        PdfDocument document = new PdfDocument();

        Paint paint = new Paint();
        int pageWidth = 595;
        int pageHeight = 842;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        int y = 40;

        paint.setTextSize(14);
        canvas.drawText("FAKTURA VAT: " + invoice.number, 40, y, paint); y += 30;

        paint.setTextSize(12);
        canvas.drawText("Data wystawienia: " + invoice.date, 40, y, paint); y += 20;
        canvas.drawText("Termin płatności: " + invoice.dueDate, 40, y, paint); y += 40;

        canvas.drawText("Wystawca:", 40, y, paint); y += 20;
        canvas.drawText(issuer.getName(), 60, y, paint); y += 20;
        canvas.drawText(issuer.getAddress(), 60, y, paint); y += 20;
        canvas.drawText("NIP: " + issuer.getNip(), 60, y, paint); y += 40;

        canvas.drawText("Nabywca:", 40, y, paint); y += 20;
        canvas.drawText(client.name, 60, y, paint); y += 20;
        canvas.drawText(client.address, 60, y, paint); y += 20;
        canvas.drawText("NIP: " + client.nip, 60, y, paint); y += 40;

        canvas.drawText("Pozycje:", 40, y, paint); y += 20;

        for (InvoiceItem item : items) {
            String line = "• " + item.description + " – " + item.quantity + " × " + item.unitPrice + " zł + " + item.vatRate + "%";
            canvas.drawText(line, 60, y, paint);
            y += 20;
        }

        y += 30;
        paint.setFakeBoldText(true);
        canvas.drawText("SUMA NETTO: " + invoice.totalNet + " zł", 40, y, paint); y += 20;
        canvas.drawText("VAT: " + invoice.totalVat + " zł", 40, y, paint); y += 20;
        canvas.drawText("SUMA BRUTTO: " + invoice.totalGross + " zł", 40, y, paint); y += 20;
        paint.setFakeBoldText(false);

        document.finishPage(page);

        String fileName = "faktura_" + invoice.number.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
        File file = new File(context.getExternalFilesDir(null), fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            Toast.makeText(context, "Zapisano PDF: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Błąd zapisu PDF", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }
}
