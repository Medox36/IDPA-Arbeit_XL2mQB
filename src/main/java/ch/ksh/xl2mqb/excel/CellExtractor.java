package ch.ksh.xl2mqb.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class CellExtractor {

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
                return String.valueOf(cell.getNumericCellValue()).trim();
            }
            default -> {
                return "";
            }
        }
    }
}
