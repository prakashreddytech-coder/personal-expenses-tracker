package com.expense.tracker.service;

import com.expense.tracker.entity.Bank;
import com.expense.tracker.entity.CreditCard;
import com.expense.tracker.entity.Expense;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService {

    public byte[] generateBankReport(List<Bank> banks, List<Expense> expenses) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Monthly Bank Report"));
        document.add(new Paragraph(" "));

        // Bank Summary Table
        PdfPTable table = new PdfPTable(3);
        addTableHeader(table, "Bank Name", "Opening Balance", "Current Balance");
        for (Bank bank : banks) {
            table.addCell(bank.getName());
            table.addCell(bank.getOpeningBalance().toString());
            table.addCell(bank.getCurrentBalance().toString());
        }
        document.add(table);
        document.add(new Paragraph(" "));

        // Expense History Table
        document.add(new Paragraph("Bank Transactions"));
        PdfPTable expenseTable = new PdfPTable(4);
        addTableHeader(expenseTable, "Date", "Description", "Amount", "Type");
        for (Expense expense : expenses) {
            expenseTable.addCell(expense.getDateTime().toString());
            expenseTable.addCell(expense.getDescription());
            expenseTable.addCell(expense.getAmount().toString());
            expenseTable.addCell(expense.getType());
        }
        document.add(expenseTable);

        document.close();
        return out.toByteArray();
    }

    public byte[] generateCardReport(List<CreditCard> cards, List<Expense> expenses) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Monthly Credit Card Report"));
        document.add(new Paragraph(" "));

        // Card Summary Table
        PdfPTable table = new PdfPTable(4);
        addTableHeader(table, "Card Name", "Total Limit", "Used Limit", "Available");
        for (CreditCard card : cards) {
            table.addCell(card.getName());
            table.addCell(card.getTotalLimit().toString());
            table.addCell(card.getUsedLimit().toString());
            table.addCell(card.getTotalLimit().subtract(card.getUsedLimit()).toString());
        }
        document.add(table);
        document.add(new Paragraph(" "));

        // Expense History Table
        document.add(new Paragraph("Card Transactions"));
        PdfPTable expenseTable = new PdfPTable(4);
        addTableHeader(expenseTable, "Date", "Description", "Amount", "Type");
        for (Expense expense : expenses) {
            expenseTable.addCell(expense.getDateTime().toString());
            expenseTable.addCell(expense.getDescription());
            expenseTable.addCell(expense.getAmount().toString());
            expenseTable.addCell(expense.getType());
        }
        document.add(expenseTable);

        document.close();
        return out.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        Stream.of(headers)
            .forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(columnTitle));
                table.addCell(header);
            });
    }
}
