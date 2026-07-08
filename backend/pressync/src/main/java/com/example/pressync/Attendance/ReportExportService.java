package com.example.pressync.Attendance;

import com.example.pressync.Attendance.Model.AttendanceReportDTO;
import com.example.pressync.Attendance.Model.AttendanceReportRecord;
import com.example.pressync.Attendance.Model.AttendanceReportStats;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportExportService {

    public byte[] generateCsv(AttendanceReportDTO report) {
        StringBuilder sb = new StringBuilder();
        sb.append("Attendance Report: ").append(report.getCategoryName()).append("\n");
        if (!report.getCreatedByEmail().isEmpty()) {
            sb.append("Created by: ").append(report.getCreatedByEmail()).append("\n");
        }
        sb.append("\n");

        AttendanceReportStats stats = report.getStats();
        sb.append("Total Events,").append(stats.getTotalEvents()).append("\n");
        sb.append("Total Records,").append(stats.getTotalRecords()).append("\n");
        sb.append("Average per Event,").append(String.format("%.1f", stats.getAveragePerEvent())).append("\n");
        sb.append("Peak Attendance,").append(stats.getPeakAttendance()).append("\n");
        sb.append("Overall Rate,").append(stats.getOverallRate()).append("%\n");
        sb.append("\n");

        List<AttendanceReportRecord> records = report.getRecords();
        boolean hasUserInfo = records.stream().anyMatch(r -> r.getName() != null);

        if (hasUserInfo) {
            sb.append("Name,Surname,Email,Event Date,Joined At\n");
            for (AttendanceReportRecord r : records) {
                sb.append(escapeCsv(r.getName())).append(",");
                sb.append(escapeCsv(r.getSurname())).append(",");
                sb.append(escapeCsv(r.getEmail())).append(",");
                sb.append(r.getEventDate()).append(",");
                sb.append(r.getJoinedAt()).append("\n");
            }
        } else {
            sb.append("Event Date,Joined At\n");
            for (AttendanceReportRecord r : records) {
                sb.append(r.getEventDate()).append(",");
                sb.append(r.getJoinedAt()).append("\n");
            }
        }

        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    public byte[] generatePdf(AttendanceReportDTO report) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, baos);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        document.add(new Paragraph("Attendance Report: " + report.getCategoryName(), titleFont));
        if (!report.getCreatedByEmail().isEmpty()) {
            document.add(new Paragraph("Created by: " + report.getCreatedByEmail(), subtitleFont));
        }
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Statistics", sectionFont));
        document.add(Chunk.NEWLINE);

        AttendanceReportStats stats = report.getStats();
        PdfPTable statsTable = new PdfPTable(2);
        statsTable.setWidthPercentage(50);
        statsTable.setSpacingAfter(15);

        addStatRow(statsTable, "Total Events", String.valueOf(stats.getTotalEvents()));
        addStatRow(statsTable, "Total Records", String.valueOf(stats.getTotalRecords()));
        addStatRow(statsTable, "Average per Event", String.format("%.1f", stats.getAveragePerEvent()));
        addStatRow(statsTable, "Peak Attendance", String.valueOf(stats.getPeakAttendance()));
        addStatRow(statsTable, "Overall Rate", stats.getOverallRate() + "%");

        document.add(statsTable);
        document.add(Chunk.NEWLINE);

        List<AttendanceReportRecord> records = report.getRecords();
        boolean hasUserInfo = records.stream().anyMatch(r -> r.getName() != null);

        document.add(new Paragraph("Attendance Records", sectionFont));
        document.add(Chunk.NEWLINE);

        if (hasUserInfo) {
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 2, 3, 2, 2});

            addHeaderCell(table, "Name", headerFont);
            addHeaderCell(table, "Surname", headerFont);
            addHeaderCell(table, "Email", headerFont);
            addHeaderCell(table, "Event Date", headerFont);
            addHeaderCell(table, "Joined At", headerFont);

            for (AttendanceReportRecord r : records) {
                addCell(table, r.getName(), cellFont);
                addCell(table, r.getSurname(), cellFont);
                addCell(table, r.getEmail(), cellFont);
                addCell(table, r.getEventDate(), cellFont);
                addCell(table, r.getJoinedAt(), cellFont);
            }

            document.add(table);
        } else {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(50);
            table.setWidths(new float[]{2, 2});

            addHeaderCell(table, "Event Date", headerFont);
            addHeaderCell(table, "Joined At", headerFont);

            for (AttendanceReportRecord r : records) {
                addCell(table, r.getEventDate(), cellFont);
                addCell(table, r.getJoinedAt(), cellFont);
            }

            document.add(table);
        }

        document.close();
        return baos.toByteArray();
    }

    private void addStatRow(PdfPTable table, String label, String value) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(0xE0, 0xE0, 0xE0));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
