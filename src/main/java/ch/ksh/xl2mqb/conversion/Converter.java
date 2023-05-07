package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.excel.CellExtractor;
import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.facade.ConvertFacade;
import ch.ksh.xl2mqb.facade.FileFacade;

import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Abstract class for the converters
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public abstract class Converter {
    protected String xmlString = "";
    protected XSSFSheet sheet;
    protected final ExcelHandler excelHandler;
    protected final Logger logger = ConvertFacade.getInstance().getLogger();

    public Converter() {
        excelHandler = FileFacade.getInstance().readFile();
    }

    public abstract String convert();

    protected boolean isRowEmpty(XSSFRow row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (!CellExtractor.getCellValueSafe(row.getCell(i)).isBlank()) {
                return false;
            }
        }
        return true;
    }
}
