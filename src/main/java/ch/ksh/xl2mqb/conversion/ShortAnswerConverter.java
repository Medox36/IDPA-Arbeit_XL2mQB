package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.xml.XMLUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ShortAnswerConverter extends Converter {

    public ShortAnswerConverter() {
        super();
        sheet = excelHandler.getSheetByName("Shortanswer");
    }

    public String convert() {
        for (int rowI = 1; rowI < sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            convertSingleQuestion(row);
        }
        return xmlString;
    }

    private String convertSingleQuestion(XSSFRow row){

        String aShortAnswer = "<question type=\"shortanswer\">";

        for (int colI = 0; colI < row.getLastCellNum(); colI++) {
            XSSFCell cell = row.getCell(colI);
            switch (colI) {
                case 0 -> {
                    aShortAnswer += "<name><text>" + row.getCell(colI).getStringCellValue() + "</text>\n</name>";
                }
                case 1 -> {
                    aShortAnswer += "<defaultgrade>" + row.getCell(colI).getStringCellValue() + "</defaultgrade>";
                }
                case 2 -> {
                        aShortAnswer += "<generalfeedback format=\"moodle_auto_format\">\n<text>"
                                + row.getCell(colI).getStringCellValue()
                                + "</text></generalfeedback>";
                }
                case 4 -> {
                    if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue() == "") {
                        aShortAnswer += "<questiontext format=\"moodle_auto_format\"><text>"
                                + row.getCell(colI).getStringCellValue()
                                + "</text></questiontext>";
                    } else {
                        aShortAnswer += "<questiontext format=\"moodle_auto_format\"><text>"
                                + row.getCell(colI).getStringCellValue()
                                + "<img src=\"" + row.getCell(3).getStringCellValue()
                                + "\" alt=\"image\" role=\"presentation\" class=\"atto_image_button_text-bottom\">"
                                + "</text></questiontext>";
                    }
                }
                case 5, 8, 11, 14, 17, 20, 23, 26, 29, 32 -> {
                    aShortAnswer += XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue())
                            + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI+2).getStringCellValue(),
                            "format=\"moodle_auto_format\"")) , "fraction=\"" + row.getCell(colI+1).getStringCellValue()
                            + "\"", "format=\"moodle_auto_format\"");
                }
            }
        }
        aShortAnswer += "</question>";
        return aShortAnswer;
    }
}
