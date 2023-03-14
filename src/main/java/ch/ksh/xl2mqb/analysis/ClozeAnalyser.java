package ch.ksh.xl2mqb.analysis;

import ch.ksh.xl2mqb.excel.ExcelHandler;

import org.apache.logging.log4j.core.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.File;

public class ClozeAnalyser extends Analyser {
    protected Logger logger;
    protected XSSFCell[][] cells;
    protected ExcelHandler excelHandler;

    @Override
    public void analyse(File file) {
        throw new UnsupportedOperationException();
    }
}
