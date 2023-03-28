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
        int rownum = 1;
        String aShortAnswer = "<question type=\"shortanswer\">";

        for (int colI = 0; colI < row.getLastCellNum(); colI++) {
            XSSFCell cell = row.getCell(colI);
            switch (colI) {
                case 0 -> {
                    aShortAnswer += XMLUtil.getXMLForTag("name", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()));
                }
                case 1 -> {
                    aShortAnswer += XMLUtil.getXMLForTag("defaultgrade",row.getCell(colI).getStringCellValue().trim());
                    aShortAnswer += XMLUtil.getXMLForTag("penalty","0.0000000");
                    aShortAnswer += XMLUtil.getXMLForTag("idnumber", String.valueOf(rownum));
                }
                case 2 -> {
                        aShortAnswer += XMLUtil.getXMLForTag("generalfeedback", XMLUtil.getXMLForTag("text",row.getCell(colI).getStringCellValue().trim()), "format=\"moodle_auto_format\"");
                }
                case 4 -> {
                    if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue() == "") {
                        aShortAnswer += XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTag("text",row.getCell(colI).getStringCellValue().trim()), "format=\"moodle_auto_format\"");
                    } else {
                        aShortAnswer += XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTag("text",row.getCell(colI).getStringCellValue().trim()
                                + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(), "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")),
                                "format=\"moodle_auto_format\"");
                    }
                }
                case 5, 8, 11, 14, 17, 20, 23, 26, 29, 32 -> {
                    aShortAnswer += XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())
                            + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI+2).getStringCellValue().trim(),
                            "format=\"moodle_auto_format\"")) , "fraction=\"" + row.getCell(colI+1).getStringCellValue().trim()
                            + "\"", "format=\"moodle_auto_format\"");
                }
            }
        }
        aShortAnswer += "</question>";
        rownum++;
        return aShortAnswer;
    }
}
