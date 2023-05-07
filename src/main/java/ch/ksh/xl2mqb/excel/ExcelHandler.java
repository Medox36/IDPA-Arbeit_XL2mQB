package ch.ksh.xl2mqb.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

/**
 * Makes new Workbook out of excel file
 *
 * @author Niklas Vogel
 * @version 1.0
 */

public class ExcelHandler {
    private XSSFWorkbook workbook;

    /**
     * Makes a new workbook from a file
     *
     * @param file
     */
    public ExcelHandler(File file) {
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets a sheet by ist name
     *
     * @param sheetName
     * @return XSSFSheet
     */
    public XSSFSheet getSheetByName(String sheetName) {
        return workbook.getSheet(sheetName);
    }
}
