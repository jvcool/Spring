package com.springboot.app;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class PDFExporter {
    private List<User> listUsers;

    public PDFExporter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(Color.cyan);
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.blue);

        cell.setPhrase(new Phrase("User ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Login ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("HTML URL", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("URL", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Site admin", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA);

        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        for (User user : listUsers) {
            cell.setPhrase(new Phrase(user.getId(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(user.getLogin(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(user.getHtml_url(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(user.getUrl(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(user.getType(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(user.getSite_admin(), font));
            table.addCell(cell);
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(14);
        font.setColor(Color.blue);

        Paragraph p = new Paragraph("List of Github Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.0f, 3.0f, 3.0f, 1.5f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }
}
