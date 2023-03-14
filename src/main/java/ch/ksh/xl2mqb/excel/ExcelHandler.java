package ch.ksh.xl2mqb.excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

public class ExcelHandler {
    private ExcelReader excelReader;
    private XSSFWorkbook workbook;

    public ExcelHandler(File file) {

    }

    public XSSFCell[][] getRelevantCellsFromSheet(String sheetName) {
        throw new UnsupportedOperationException();
    }

    private XSSFRow[] getRowsFromSheet(XSSFSheet sheet) {
        throw new UnsupportedOperationException();
    }

    private XSSFSheet getSheetByName(String sheetName) {
        throw new UnsupportedOperationException();
    }
}
