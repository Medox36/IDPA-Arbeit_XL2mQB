package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.excel.ExcelHandler;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class Converter {
    protected String xmlString;
    protected XSSFCell[][] cells;
    protected ExcelHandler excelHandler;

    protected XSSFCell[][] fetchRelevantCells(String sheetName) {
        throw new UnsupportedOperationException();
    }

    public abstract String convert(XSSFWorkbook workbook);
}

