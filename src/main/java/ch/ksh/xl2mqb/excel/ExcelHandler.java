package ch.ksh.xl2mqb.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

/**
 * Makes new Workbook out of excel file
 */
public class ExcelHandler {
    private XSSFWorkbook workbook;

    public ExcelHandler(File file) {
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public XSSFSheet getSheetByName(String sheetName) {
        return workbook.getSheet(sheetName);
    }
}
