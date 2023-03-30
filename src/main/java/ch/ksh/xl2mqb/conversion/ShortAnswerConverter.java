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
                    if (checkRow(row)) {
                        aShortAnswer += XMLUtil.getXMLForTag("name", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()));
                    }
                }
                case 1 -> {
                        aShortAnswer += XMLUtil.getXMLForTag("defaultgrade", row.getCell(colI).getStringCellValue().trim());
                }
                case 2 -> {
                        aShortAnswer += XMLUtil.getXMLForTag("generalfeedback", XMLUtil.getXMLForTag("text", row.getCell(colI).getStringCellValue().trim()),
                                "format=\"moodle_auto_format\"");
                }
                case 4 -> {
                    if (checkRow(row)) {
                        if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue().equals("")) {
                            aShortAnswer += XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTag("text", row.getCell(colI).getStringCellValue().trim()), "format=\"moodle_auto_format\"");
                        } else {
                            aShortAnswer += XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTag("text", row.getCell(colI).getStringCellValue().trim()
                                            + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(), "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")),
                                    "format=\"moodle_auto_format\"");
                        }
                    }
                }
                case 5 -> {
                    aShortAnswer += XMLUtil.getXMLForTag("hint", XMLUtil.getXMLForTag("text",row.getCell(colI).getStringCellValue().trim()), "format=\"moodle_auto_format\"");
                }
                case 6 -> {
                    aShortAnswer += XMLUtil.getXMLForTag("penalty", row.getCell(colI).getStringCellValue().trim());
                }
                case 7 -> {
                    if (checkRow(row)) {
                        aShortAnswer += XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())
                                + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI + 2).getStringCellValue().trim(),
                                "format=\"moodle_auto_format\"")), "fraction=\"" + row.getCell(colI + 1).getStringCellValue().trim()
                                + "\"", "format=\"moodle_auto_format\"");
                    }
                }
                case 10, 13, 16, 19, 22, 25, 28, 31, 34-> {
                    aShortAnswer += XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())
                            + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI+2).getStringCellValue().trim(),
                            "format=\"moodle_auto_format\"")) , "fraction=\"" + row.getCell(colI+1).getStringCellValue().trim()
                            + "\"", "format=\"moodle_auto_format\"");
                }
            }
        }
        aShortAnswer += "</question>";

        return aShortAnswer;
    }

    private boolean checkRow(XSSFRow row){
        for (int colI = 0; colI < row.getLastCellNum(); colI++){
            if (row.getCell(colI).getStringCellValue().trim().isBlank()){
                logger.info("Die Frage auf Zeile "+ (row.getRowNum() + 1)
                        + " vom Typ Shortanswer hat nicht alle benötigten Daten.");
                return false;
            }
        }
        return true;
    }
}
