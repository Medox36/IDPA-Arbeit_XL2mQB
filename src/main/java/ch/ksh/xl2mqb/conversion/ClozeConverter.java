package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.xml.XMLUtil;

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
        for (int rowI = 1; rowI < sheet.getLastRowNum(); rowI++) {
            convertSingleQuestion(sheet.getRow(rowI));
        }

        return xmlString;
    }

    private void convertSingleQuestion(XSSFRow row) {
        xmlString += "<question type=\"cloze\">";

        questionName(row.getCell(0));
        penalty(row.getCell(1));
        generalFeedback(row.getCell(2));
        questionText(row);

        xmlString += "</question>";
    }

    private void questionName(XSSFCell cell) {
        String text = XMLUtil.getXMLForTextTag(cell.getStringCellValue().trim());
        xmlString += XMLUtil.getXMLForTag("name", text);
    }

    private void penalty(XSSFCell cell) {
        xmlString += XMLUtil.getXMLForTag("penalty", cell.getStringCellValue().trim());
    }

    private void generalFeedback(XSSFCell cell) {
        String text = XMLUtil.getXMLForTextTag(cell.getStringCellValue().trim());
        xmlString += XMLUtil.getXMLForTag("generalfeedback ", text, "format=\"moodle_auto_format\"");
    }

    private void questionText(XSSFRow row) {
        xmlString += "<questiontext format=\"moodle_auto_format\">";

        nonSubQuestionText(row.getCell(4));
        picture(row.getCell(3));

        boolean skipped = true;
        for (int cellI = 5; cellI < row.getLastCellNum(); cellI++) {
            skipped = false;
            subQuestion(row.getCell(cellI));
        }

        if (skipped) {
            // TODO show in log that no sub question was selected
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
        xmlString += convertSubQuestion(cell.getStringCellValue().trim());
    }

    private String convertSubQuestion(String questionNumber) {
        XSSFRow row = getRowForMatchingQuestionNumber(questionNumber);
        if (row == null) {
            // TODO show error sub question doesn't exist
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
