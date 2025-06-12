package com.example.fakturka.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.fakturka.data.model.Client;
import com.example.fakturka.data.model.Invoice;
import com.example.fakturka.data.model.InvoiceItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfInvoiceGeneratorOpenPDF {

    public static void generate(Context context, Invoice invoice, Client client, List<InvoiceItem> items) {
        try {
            // Utwórz dokument
            Document document = new Document(PageSize.A4);
            String fileName = "faktura_" + invoice.number.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
            File file = new File(context.getExternalFilesDir(null), fileName);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            IssuerDataProvider issuer = new IssuerDataProvider(context);
            String logoPath = issuer.getLogoPath();

            // Dodaj logo
            if (logoPath != null && !logoPath.isEmpty()) {
                try {
                    Image logo = Image.getInstance(logoPath);
                    logo.scaleToFit(100, 60);
                    logo.setAlignment(Image.ALIGN_LEFT);
                    document.add(logo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Tytuł faktury
            Paragraph title = new Paragraph("Faktura VAT: " + invoice.number,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setSpacingBefore(10);
            title.setSpacingAfter(20);
            document.add(title);

            // Sprzedawca / Nabywca
            PdfPTable partyTable = new PdfPTable(2);
            partyTable.setWidthPercentage(100);
            partyTable.setSpacingAfter(20);

            PdfPCell left = new PdfPCell();
            left.setBorder(Rectangle.NO_BORDER);
            left.addElement(new Paragraph("Sprzedawca:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            left.addElement(new Paragraph(issuer.getName()));
            left.addElement(new Paragraph(issuer.getAddress()));
            left.addElement(new Paragraph("NIP: " + issuer.getNip()));

            PdfPCell right = new PdfPCell();
            right.setBorder(Rectangle.NO_BORDER);
            right.addElement(new Paragraph("Nabywca:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            right.addElement(new Paragraph(client.name));
            right.addElement(new Paragraph(client.address));
            right.addElement(new Paragraph("NIP: " + client.nip));

            partyTable.addCell(left);
            partyTable.addCell(right);
            document.add(partyTable);

            // Daty
            Paragraph dates = new Paragraph(
                    "Data wystawienia: " + invoice.date + "\nTermin płatności: " + invoice.dueDate,
                    FontFactory.getFont(FontFactory.HELVETICA, 11)
            );
            dates.setSpacingAfter(10);
            document.add(dates);

            // Tabela pozycji
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 3f, 1f, 2f, 1f, 2f});

            String[] headers = {"Lp", "Nazwa", "Ilość", "Cena netto", "VAT", "Brutto"};
            for (String h : headers) {
                PdfPCell header = new PdfPCell(new Phrase(h));
                table.addCell(header);
            }

            int i = 1;
            for (InvoiceItem item : items) {
                table.addCell(String.valueOf(i++));
                table.addCell(item.description);
                table.addCell(String.valueOf(item.quantity));
                table.addCell(String.format("%.2f zł", item.unitPrice));
                table.addCell(item.vatRate + " %");

                double net = item.quantity * item.unitPrice;
                double gross = net + (net * item.vatRate / 100);
                table.addCell(String.format("%.2f zł", gross));
            }

            table.setSpacingBefore(10f);
            document.add(table);

            // Podsumowanie
            Paragraph summary = new Paragraph();
            summary.setSpacingBefore(20f);
            summary.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            summary.add("SUMA NETTO: " + String.format("%.2f zł", invoice.totalNet) + "\n");
            summary.add("VAT: " + String.format("%.2f zł", invoice.totalVat) + "\n");
            summary.add("SUMA BRUTTO: " + String.format("%.2f zł", invoice.totalGross));
            document.add(summary);

            document.close();

            Toast.makeText(context, "PDF zapisany: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Błąd generowania PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
