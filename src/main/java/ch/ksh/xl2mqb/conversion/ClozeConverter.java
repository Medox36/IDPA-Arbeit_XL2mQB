package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.analysis.AnalyserUtil;
import ch.ksh.xl2mqb.conversion.xml.XMLUtil;
import ch.ksh.xl2mqb.excel.CellExtractor;
import ch.ksh.xl2mqb.facade.MenuFacade;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Objects;

public class ClozeConverter extends Converter {

    private final XSSFSheet subQuestionSheet;

    public ClozeConverter() {
        super();
        sheet = excelHandler.getSheetByName("Cloze");
        subQuestionSheet = excelHandler.getSheetByName("Cloze_Shortanswer");
    }

    public String convert() {
        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            if (isRowEmpty(row)) {
                continue;
            }
            if (hasAllNecessaryContents(row)) {
                convertSingleQuestion(row);
            } else {
                if (MenuFacade.getInstance().areConversionErrorsShown()) {
                    logger.info("Die Frage auf Zeile " + (row.getRowNum() + 1)
                                + " vom Typ Cloze hat nicht alle benötigten Daten.");
                }
            }
        }
        return xmlString;
    }

    private boolean hasAllNecessaryContents(XSSFRow row) {
        // does the question have a Name
        if (CellExtractor.getCellValueSafe(row.getCell(0)).isBlank()) {
            return false;
        }
        // does the question have a formulation of a question
        if (CellExtractor.getCellValueSafe(row.getCell(5)).isBlank()) {
            return false;
        }
        // does the question have at least one sub-question
        return hasSubQuestions(row);
    }

    private boolean hasSubQuestions(XSSFRow row) {
        boolean hasSubQuestions = false;
        for (int i = 6; i < row.getLastCellNum(); i++) {
            String cellValue = CellExtractor.getCellValueSafe(row.getCell(i));
            if (!cellValue.isBlank() && AnalyserUtil.isNumeric(cellValue)) {
                hasSubQuestions = true;
                break;
            }
        }
        return hasSubQuestions;
    }

    private void convertSingleQuestion(XSSFRow row) {
        xmlString += "<question " + Type.CLOZE + ">";

        questionName(row.getCell(0));
        generalFeedback(row.getCell(1));
        hint(row.getCell(3));
        penalty(row.getCell(4));
        questionText(row);

        xmlString += "</question>";
    }

    private void questionName(XSSFCell cell) {
        String text = XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(cell));
        xmlString += XMLUtil.getXMLForTag("name", text);
    }

    private void hint(XSSFCell cell) {
        String text = XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(cell));
        xmlString += XMLUtil.getXMLForTag("generalfeedback ", text, Format.AUTO_FORMAT);
    }

    private void penalty(XSSFCell cell) {
        xmlString += XMLUtil.getXMLForTag("penalty", CellExtractor.getCellValueSafe(cell));
    }

    private void generalFeedback(XSSFCell cell) {
        String text = XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(cell));
        xmlString += XMLUtil.getXMLForTag("generalfeedback ", text, Format.AUTO_FORMAT);
    }

    private void questionText(XSSFRow row) {
        xmlString += "<questiontext " + Format.AUTO_FORMAT + ">";

        StringBuilder questionTextSB = new StringBuilder();

        // non sub question text
        questionTextSB.append(CellExtractor.getCellValueSafe(row.getCell(5)));
        // picture
        questionTextSB.append(picture(row.getCell(2)));

        boolean skipped = true;
        for (int cellI = 6; cellI < row.getLastCellNum(); cellI++) {
            XSSFCell cell = row.getCell(cellI);
            if (CellExtractor.getCellValueSafe(cell).isBlank()) {
                continue;
            }
            skipped = false;
            questionTextSB.append(convertSubQuestion(CellExtractor.getCellValueSafe(cell), cell.getRowIndex()));
        }

        if (skipped && MenuFacade.getInstance().areConversionErrorsShown()) {
            logger.info("Für die Frage auf Zeile " + (row.getRowNum() + 1)
                        + " vom Typ Cloze wurde(n) keine Teilfragen angegeben.");
        }

        xmlString += XMLUtil.getXMLForCDATATextTag(questionTextSB.toString());
        xmlString += "</questiontext>";
    }

    private String picture(XSSFCell cell) {
        String image = CellExtractor.getCellValueSafe(cell);
        if (image.isBlank()) {
            return "";
        }
        return XMLUtil.getXMLForImgTag(image, image);
    }

    private String convertSubQuestion(String questionNumber, int rowNum) {
        XSSFRow row = getRowForMatchingQuestionNumber(questionNumber);
        if (row == null) {
            if (MenuFacade.getInstance().areConversionErrorsShown()) {
                logger.info("Für die Frage auf Zeile " + (rowNum + 1)
                            + " vom Typ Cloze wurde keine passende Teilfrage mit der Nummer " + questionNumber
                            + " gefunden.");
            }
            return "";
        }
        if (!subQuestionHasAllNecessaryContents(row)) {
            if (MenuFacade.getInstance().areConversionErrorsShown()) {
                logger.info("Die Teilfrage auf Zeile " + (row.getRowNum() + 1) + " von Typ Cloze_Shortanswer hat " +
                            "nicht alle benötigten Angaben.");
            }
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(CellExtractor.getCellValueSafe(row.getCell(4)));

        String image = CellExtractor.getCellValueSafe(row.getCell(3));
        if (!image.isBlank()) {
            sb.append(XMLUtil.getXMLForImgTag(image, image));
        }

        sb.append(" {");
        sb.append(CellExtractor.getCellValueSafe(row.getCell(2)));
        sb.append(":");
        sb.append("SHORTANSWER:");

        for (int colI = 5; colI < row.getLastCellNum(); colI += 3) {
            if (CellExtractor.getCellValueSafe(row.getCell(colI)).isBlank()) {
                continue;
            }
            if (colI != 5) {
                sb.append("~");
            }
            sb.append("%");
            sb.append(CellExtractor.getCellValueSafe(row.getCell(colI + 1)).replaceAll("%", ""));
            sb.append("%");
            sb.append(maskSpecialChars(CellExtractor.getCellValueSafe(row.getCell(colI))));

            String feedbackCellValue = CellExtractor.getCellValueSafe(row.getCell(colI + 2));
            if (!feedbackCellValue.isBlank()) {
                sb.append("#").append(feedbackCellValue);
            }
        }

        sb.append("} ");

        return sb.toString();
    }

    private boolean subQuestionHasAllNecessaryContents(XSSFRow row) {
        // has no number
        if (!AnalyserUtil.isDecimal(CellExtractor.getCellValueSafe(row.getCell(0)))) {
            return false;
        }
        // has no question name
        if (CellExtractor.getCellValueSafe(row.getCell(1)).isBlank()) {
            return false;
        }
        // has no points
        if (CellExtractor.getCellValueSafe(row.getCell(2)).isBlank()) {
            return false;
        }
        // has no formulation of a question
        if (CellExtractor.getCellValueSafe(row.getCell(4)).isBlank()) {
            return false;
        }
        // has no first answer
        if (CellExtractor.getCellValueSafe(row.getCell(5)).isBlank()) {
            return false;
        }
        // has no points for first answer
        return !CellExtractor.getCellValueSafe(row.getCell(6)).isBlank();
    }

    private XSSFRow getRowForMatchingQuestionNumber(String questionNumber) {
        return getRowForMatchingQuestionNumber(questionNumber, subQuestionSheet);
    }

    public static XSSFRow getRowForMatchingQuestionNumber(String questionNumber, XSSFSheet subQuestionSheet) {
        for (int rowI = 1; rowI <= subQuestionSheet.getLastRowNum(); rowI++) {
            XSSFRow row = subQuestionSheet.getRow(rowI);
            XSSFCell cell = row.getCell(0);
            if (Objects.equals(CellExtractor.getCellValueSafe(cell), questionNumber)) {
                return row;
            }
        }

        return null;
    }

    private String maskSpecialChars(String str) {
        str = str.replaceAll("\\{", "\\{");
        str = str.replaceAll("}", "\\}}");
        str = str.replaceAll("#", "\\#");
        str = str.replaceAll("~", "\\~");
        str = str.replaceAll("/", "\\/");
        str = str.replaceAll("\\\\", "\\\\");

        return str;
    }
}
