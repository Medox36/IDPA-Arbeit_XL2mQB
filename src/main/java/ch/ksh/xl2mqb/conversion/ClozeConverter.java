package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.xml.XMLUtil;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Objects;
import java.util.regex.Pattern;

public class ClozeConverter extends Converter {
    private final Pattern numericPattern = Pattern.compile("\\d+");

    private final XSSFSheet subQuestionSheet;

    public ClozeConverter() {
        super();
        sheet = excelHandler.getSheetByName("Cloze");
        subQuestionSheet = excelHandler.getSheetByName("Cloze_Shortanswer");
    }

    public String convert() {
        for (int rowI = 1; rowI < sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            if (hasAllNecessaryContents(row)) {
                convertSingleQuestion(row);
            } else {
                logger.info("Die Frage auf Zeile " + (row.getRowNum() + 1)
                        + " vom Typ Cloze hat nicht alle benötigten Daten.");
            }
        }

        return xmlString;
    }

    private boolean hasAllNecessaryContents(XSSFRow row) {
        // does the question have a Name
        if (row.getCell(0).getStringCellValue().isBlank()) {
            return false;
        }
        // does the question have a formulation of a question
        if (row.getCell(5).getStringCellValue().isBlank()) {
            return false;
        }
        // does the question have at least one sub-question
        if (!hasSubQuestions(row)) {
            return false;
        }

        return true;
    }

    private boolean hasSubQuestions(XSSFRow row) {
        boolean hasSubQuestions = false;
        for (int i = 6; i < row.getRowNum(); i++) {
            String cellValue = row.getCell(i).getStringCellValue().trim();
            if (!cellValue.isBlank() && !isNumeric(cellValue)) {
                hasSubQuestions = true;
                break;
            }
        }

        return hasSubQuestions;
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }

        return numericPattern.matcher(strNum).matches();
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
        String text = XMLUtil.getXMLForTextTag(cell.getStringCellValue().trim());
        xmlString += XMLUtil.getXMLForTag("name", text);
    }

    private void hint(XSSFCell cell) {
        String text = XMLUtil.getXMLForTag("text", cell.getStringCellValue().trim());
        xmlString += XMLUtil.getXMLForTag("generalfeedback ", text, Format.AUTO_FORMAT);
    }

    private void penalty(XSSFCell cell) {
        xmlString += XMLUtil.getXMLForTag("penalty", cell.getStringCellValue().trim());
    }

    private void generalFeedback(XSSFCell cell) {
        String text = XMLUtil.getXMLForTextTag(cell.getStringCellValue().trim());
        xmlString += XMLUtil.getXMLForTag("generalfeedback ", text, Format.AUTO_FORMAT);
    }

    private void questionText(XSSFRow row) {
        xmlString += "<questiontext " + Format.AUTO_FORMAT + ">";

        nonSubQuestionText(row.getCell(5));
        picture(row.getCell(2));

        boolean skipped = true;
        for (int cellI = 5; cellI < row.getLastCellNum(); cellI++) {
            skipped = false;
            subQuestion(row.getCell(cellI));
        }

        if (skipped) {
            logger.info("Für die Frage auf Zeile " + (row.getRowNum() + 1)
                    + " vom Typ Cloze wurde(n) keine Teilfragen angegeben.");
        }

        xmlString += "</questiontext>";
    }

    private void nonSubQuestionText(XSSFCell cell) {
        xmlString += XMLUtil.getXMLForTextTag(cell.getStringCellValue().trim());
    }

    private void picture(XSSFCell cell) {
        String image = cell.getStringCellValue().trim();
        if (!image.isBlank()) {
            xmlString += XMLUtil.getXMLForImgTag(image, image);
        }
    }

    private void subQuestion(XSSFCell cell) {
        xmlString += convertSubQuestion(cell.getStringCellValue().trim(), cell.getRowIndex());
    }

    private String convertSubQuestion(String questionNumber, int rowNum) {
        XSSFRow row = getRowForMatchingQuestionNumber(questionNumber);
        if (row == null) {
            logger.info("Für die Frage auf Zeile " + (rowNum + 1)
                    + " vom Typ Cloze wurde keine passende Teilfrage mit der Nummer " + questionNumber + " gefunden.");
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(row.getCell(4).getStringCellValue().trim());

        String image = row.getCell(3).getStringCellValue().trim();
        if (!image.isBlank()) {
            sb.append(XMLUtil.getXMLForImgTag(image, image));
        }

        sb.append(" {");
        sb.append(row.getCell(2).getStringCellValue().trim()).append(":");
        sb.append("SHORTANSWER:");

        for (int colI = 5; colI < row.getLastCellNum(); colI ++) {
            if (colI % 2 == 0) {
                continue;
            }
            if (colI != 5) {
                sb.append("~");
            }
            sb.append("%");
            sb.append(row.getCell(colI + 1).getStringCellValue().trim().replaceAll("%", ""));
            sb.append("%");
            sb.append(maskSpecialChars(row.getCell(colI).getStringCellValue().trim()));
        }

        sb.append("} ");

        return sb.toString();
    }

    private XSSFRow getRowForMatchingQuestionNumber(String questionNumber) {
        for (int rowI = 1; rowI < subQuestionSheet.getLastRowNum(); rowI++) {
            XSSFRow row = subQuestionSheet.getRow(rowI);
            XSSFCell cell = row.getCell(0);
            if (Objects.equals(cell.getStringCellValue(), questionNumber)) {
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
