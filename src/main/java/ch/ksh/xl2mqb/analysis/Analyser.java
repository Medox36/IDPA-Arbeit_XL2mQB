package ch.ksh.xl2mqb.analysis;

import ch.ksh.xl2mqb.excel.ExcelHandler;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.logging.log4j.core.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class Analyser {
    protected Logger logger;
    protected XSSFCell[][] cells;
    protected ExcelHandler excelHandler;

    protected XSSFCell[][] fetchRelevantCells(String sheetName) {
        throw new UnsupportedOperationException();
    }

    public abstract void analyse(XSSFWorkbook workbook);
}
