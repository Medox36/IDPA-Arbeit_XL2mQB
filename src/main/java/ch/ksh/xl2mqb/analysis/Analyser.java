package ch.ksh.xl2mqb.analysis;

import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.facade.ConvertFacade;
import ch.ksh.xl2mqb.facade.FileFacade;

import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class Analyser {
    protected XSSFSheet sheet;
    protected ExcelHandler excelHandler;
    protected TabbedStringBuilder analyseResult;
    protected Logger logger = ConvertFacade.getInstance().getLogger();

    public Analyser() {
        excelHandler = FileFacade.getInstance().readFile();
    }

    public abstract void analyse();
}
