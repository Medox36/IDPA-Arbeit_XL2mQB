package ch.ksh.xl2mqb.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.List;

public class ExcelReader {
    private static List<ExcelReader> INSTANCES;
    private final File file;

    private ExcelReader(File file) {
        this.file = file;
    }

    public XSSFWorkbook readExcelFile(File file){
        throw new UnsupportedOperationException();
    }

    public ExcelReader getInstanceForFile() {
        throw new UnsupportedOperationException();
    }
}
