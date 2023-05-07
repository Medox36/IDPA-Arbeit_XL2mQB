package ch.ksh.xl2mqb.excel;

import ch.ksh.xl2mqb.analysis.AnalyserUtil;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * class to get the wright cell type
 *
 * @author Niklas Vogel
 * @version 1.0
 */

public class CellExtractor {

    /**
     * gets the wright cell type and reads values accordingly
     *
     * @param cell
     * @return the cell value
     */
    public static String getCellValueSafe(XSSFCell cell) {
        if (cell == null) {
            return "";
        }

        CellType type = cell.getCellType();
        switch (type) {
            case STRING -> {
                return cell.getStringCellValue().trim();
            }
            case BOOLEAN -> {
                return String.valueOf(cell.getBooleanCellValue()).trim();
            }
            case NUMERIC -> {
                return AnalyserUtil.removeTailingDecimalZeros(String.valueOf(cell.getNumericCellValue()).trim());
            }
            default -> {
                return "";
            }
        }
    }
}
