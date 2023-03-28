package ch.ksh.xl2mqb.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class ExcelHandler {
    private XSSFWorkbook workbook;

    public ExcelHandler(File file) {
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public XSSFCell[][] getRelevantCellsFromSheet(String sheetName) {
        XSSFSheet sheet = getSheetByName(sheetName);
        int noOfRows = sheet.getLastRowNum() + 1;
        int noOfColumns = sheet.getRow(0).getLastCellNum();

        XSSFRow[] relRows = getRowsFromSheet(sheet);
        XSSFCell[][] relCell = new XSSFCell[noOfRows][noOfColumns];

        for (int i = 0; i < relRows.length; i++) {
            Row row = relRows[i];
            for (int j = row.getFirstCellNum(); j < noOfColumns; j++) {
                relCell[i][j] = (XSSFCell) row.getCell(j);
            }
        }

        return relCell;
    }

    private XSSFRow[] getRowsFromSheet(XSSFSheet sheet) {
        int noOfRows = sheet.getLastRowNum() + 1;
        XSSFRow[] sheetRows = new XSSFRow[noOfRows];

        for (int i = sheet.getFirstRowNum(); i < noOfRows; i++) {
            sheetRows[i] = sheet.getRow(i);
        }

        return sheetRows;
    }

    private XSSFSheet getSheetByName(String sheetName) {
        return workbook.getSheet(sheetName);
    }
}
