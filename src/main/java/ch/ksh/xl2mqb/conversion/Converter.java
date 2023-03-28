package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.facade.FileFacade;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class Converter {
    protected String xmlString;
    protected XSSFSheet sheet;
    protected ExcelHandler excelHandler;

    public Converter() {
        excelHandler = FileFacade.getInstance().readFile();
    }

    public abstract String convert();
}

