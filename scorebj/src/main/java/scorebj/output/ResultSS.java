package scorebj.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ResultSS {
    private static final Logger logger =  LogManager.getLogger();
    private final List<String> ssHeaderRow = new ArrayList<>();
    private final List<SSRow> ssRows = new ArrayList<>();

    private final Workbook wb;
    private final Sheet sheet;
    private CellStyle headerStyle;
    private CellStyle bodyStyle;
    private CellStyle highlightStyle;

    public List<String> getSsHeaderRow() {
        return ssHeaderRow;
    }

    public List<SSRow> getSsRows() {
        return ssRows;
    }

    public ResultSS(){
        wb = new XSSFWorkbook();
        sheet = wb.createSheet("Results");
        PrintSetup ps = sheet.getPrintSetup();

        sheet.setHorizontallyCenter(true);
        sheet.setVerticallyCenter(true);

        ps.setLandscape(true);
        ps.setFitHeight((short)1);
        ps.setFitWidth((short)1);
    }

    public void createSpreadsheet(OutputStream outputStream) throws IOException {
        sortAndRank();

        setUpStyles();



        int maxWidth = getMaxColumnWidth() + 4;

        int noColumns = ssHeaderRow.size();


        //Set column widths.
        sheet.setColumnWidth(0, 6*256);
        for (int i=1; i<noColumns-1; i++) {
            sheet.setColumnWidth(i, maxWidth*256);
        }
        sheet.setColumnWidth(noColumns-1, 6*256);

        //Create header row.
        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex);
        row.setRowStyle(headerStyle);

        Cell headerCell;
        int i=0;
        for (String el: ssHeaderRow) {
            headerCell = row.createCell(i);
            headerCell.setCellValue(el);
            headerCell.setCellStyle(headerStyle);
            i++;
        }

        //Complete body of table.
        rowIndex++;
        Cell bodyCell;
        int colIndex;
        for (SSRow ssRow: ssRows) {
            row = sheet.createRow(rowIndex);
            row.setHeight((short) (2*row.getHeight()));

            colIndex = 0;
            bodyCell = row.createCell(colIndex);
            bodyCell.setCellValue(ssRow.getRank());
            bodyCell.setCellStyle(highlightStyle);
            colIndex++;

            bodyCell = row.createCell(colIndex);
            bodyCell.setCellValue(ssRow.getPair());
            bodyCell.setCellStyle(highlightStyle);
            colIndex++;

            logger.debug("Spreadsheet line:" + Integer.toString(rowIndex));

            for (String contents: ssRow.getSetResult() ) {
                bodyCell = row.createCell(colIndex);
                bodyCell.setCellValue(contents);
                bodyCell.setCellStyle(bodyStyle);
                colIndex++;
            }
            bodyCell = row.createCell(colIndex);
            bodyCell.setCellValue(ssRow.getTotal());
            bodyCell.setCellStyle(highlightStyle);
            rowIndex++;
        }


        wb.write(outputStream);
        outputStream.close();
        
    }
    private int getMaxColumnWidth() {
        int maxWidth = 0;
        int width;
        for (SSRow r: ssRows) {
            width = r.getPair().length();
            maxWidth = Math.max(width,maxWidth);
        }
        return maxWidth;
    }
    public void sortAndRank(){
        ssRows.sort(null);

        int rowNo = 1;
        int lastRank = 0;
        String rank = "";
        int lastTotal = -1;
        for (SSRow row: ssRows) {
            if (row.getTotal()==lastTotal) {
                rank = Integer.toString(lastRank) + "=";
            }
            else {
                rank = Integer.toString(rowNo) + ")";
                lastTotal = row.getTotal();
                lastRank = rowNo;
            }
            row.setRank(rank);
            rowNo++;
        }
    }
    private void setUpStyles(){
        Font normalFont = wb.createFont();
        Font boldFont = wb.createFont();
        boldFont.setBold(true);

        headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFont(boldFont);

        bodyStyle = wb.createCellStyle();
        bodyStyle.setWrapText(true);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        bodyStyle.setFont(normalFont);

        highlightStyle = wb.createCellStyle();
        highlightStyle.setWrapText(true);
        highlightStyle.setAlignment(HorizontalAlignment.RIGHT);
        highlightStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        highlightStyle.setFont(boldFont);
    }
}
