package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.excel.ExcelHandler;

import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.File;

public class MultipleChoiceConverter extends Converter {
    protected String xmlString;
    protected XSSFCell[][] cells;
    protected ExcelHandler excelHandler;

    public String convert(File file) {
        throw new UnsupportedOperationException();
    }
}
