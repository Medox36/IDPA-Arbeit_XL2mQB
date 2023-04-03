package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.conversion.xml.XMLUtil;

import org.apache.poi.xssf.usermodel.XSSFRow;

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

    private void convertSingleQuestion(XSSFRow row) {
        StringBuilder aShortAnswerBuilder = new StringBuilder("<question " + Type.SHORTANSWER + ">");

        if (!checkRow(row)) {
            logger.info("Die Frage auf Zeile " + (row.getRowNum() + 1)
                    + " vom Typ Shortanswer hat nicht alle benötigten Daten.");
            return;
        }

        for (int colI = 0; colI < row.getLastCellNum(); colI++) {
            switch (colI) {
                //question name
                case 0 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("name", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())));
                }
                //points
                case 1 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("defaultgrade", row.getCell(colI).getStringCellValue().trim()));
                }
                //feedback
                case 2 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("generalfeedback", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                }
                //case sensitive
                case 4 -> {
                    if (row.getCell(colI).getStringCellValue().trim().equals("Ja")) {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("usecase","1"));
                    } else {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("usecase","0"));
                    }
                }
                //hint
                case 5 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("hint", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                }
                //penalty
                case 6 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("penalty", row.getCell(colI).getStringCellValue().trim()));
                }
                //question with picture
                case 7 -> {
                    if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue().equals("")) {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                    } else {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim() + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(),
                                "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.AUTO_FORMAT));
                    }
                }
                //question answers
                case 8, 11, 14, 17, 20, 23, 26, 29, 32, 35 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())
                            + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI + 2).getStringCellValue().trim(),
                            Format.AUTO_FORMAT)), "fraction=\"" + row.getCell(colI + 1).getStringCellValue().trim()
                            + "\"", Format.AUTO_FORMAT));
                }
            }
        }
        aShortAnswerBuilder.append("</question>");

        xmlString += aShortAnswerBuilder.toString();
    }

    private boolean checkRow(XSSFRow row) {
        // does the question have a Name
        if (row.getCell(0).getStringCellValue().isBlank()) {
            return false;
        }
        // does the question have a formulation of a question
        if (row.getCell(7).getStringCellValue().isBlank()) {
            return false;
        }
        // does the question have answer
        if (row.getCell(8).getStringCellValue().isBlank()) {
            return false;
        }
        // does the question have a fraction for the points of one answer
        if (row.getCell(9).getStringCellValue().isBlank()) {
            return false;
        }

        return true;
    }
}
