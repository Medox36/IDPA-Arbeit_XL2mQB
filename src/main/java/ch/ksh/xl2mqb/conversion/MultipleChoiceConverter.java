package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.conversion.xml.XMLUtil;

import org.apache.poi.xssf.usermodel.XSSFRow;


public class MultipleChoiceConverter extends Converter {

    public MultipleChoiceConverter(){
        super();
        sheet = excelHandler.getSheetByName("Multiplechoice");
    }
    public String convert() {
        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            convertSingleQuestion(row);
        }

        return xmlString;
    }

    private void convertSingleQuestion(XSSFRow row){
        StringBuilder mcQuestionBuilder = new StringBuilder("<question " + Type.MULTICHOICE + ">");

        if (!checkRow(row)) {
            logger.info("Die Frage auf Zeile " + (row.getRowNum() + 1)
                    + " vom Typ Multiple Choice hat nicht alle benötigen Daten");
            return;
        }
        mcQuestionBuilder.append(XMLUtil.getXMLForTag("question", "", "type=\"multichoice\""));

        for (int colI = 0; colI < row.getLastCellNum(); colI++) {
            switch (colI) {
                //question name
                case 0 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("name",
                            XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())));
                }
                //points
                case 1 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("defaultgrade",
                            row.getCell(colI).getStringCellValue().trim()));
                }
                //penalty
                case 2 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("penalty",
                            row.getCell(colI).getStringCellValue().trim()));
                }
                //single
                case 3 -> {
                    if (row.getCell(colI).getStringCellValue().trim().equals("Ja")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("single","true"));
                    } else {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("single","false"));
                    }
                }
                //shuffle answers
                case 4 -> {
                    if (row.getCell(colI).getStringCellValue().trim().equals("Ja")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "true"));
                    } else {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "false"));
                    }
                }
                //feedback
                case 5 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("generalfeedback",
                            XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                }
                //correct answer feedback
                case 6 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("correctfeedback",
                            XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()),
                            "format=\"html\""));
                }
                //incorrect answer feedback
                case 7 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("partiallycorrectfeedback",
                            XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()),
                            "format=\"html\""));
                }
                //no answer feedback
                case 8 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("incorrectfeedback",
                            XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()),
                            "format=\"html\""));
                }
                //answer numbering
                case 10 ->{
                    if (row.getCell(colI).getStringCellValue().trim().equals("keine")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "keine"));
                    } else if (row.getCell(colI).getStringCellValue().trim().equals("abc")){
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "abc"));
                    }else if (row.getCell(colI).getStringCellValue().trim().equals("ABCD")){
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "abcd"));
                    }else if (row.getCell(colI).getStringCellValue().trim().equals("123")){
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "123"));
                    }
                }
                //question text
                case 9 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                            XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), "format=\"html\""));
                }
                //question with picture
                case 12 -> {
                    if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue().equals("")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                    } else {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim() + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(),
                                "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.AUTO_FORMAT));
                    }
                }
                //question answers
                case 14, 17, 20, 23, 26, 29, 32, 35, 38 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())
                            + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI + 2).getStringCellValue().trim(),
                            Format.AUTO_FORMAT)), "fraction=\"" + row.getCell(colI + 1).getStringCellValue().trim()
                            + "\"", Format.AUTO_FORMAT));
                }
                //question format
                case 11 -> {
                    if (row.getCell(colI).getStringCellValue().trim().equals("Klartext")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("generalfeedback",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.PLATIN_TEXT));
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.PLATIN_TEXT));
                        if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue().equals("")) {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                    XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.PLATIN_TEXT));
                        } else {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim() + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(),
                                    "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.PLATIN_TEXT));
                        }
                        for (int i = 0; i < 10; i++){
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())
                                    + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI + 2).getStringCellValue().trim(),
                                    Format.PLATIN_TEXT)), "fraction=\"" + row.getCell(colI + 1).getStringCellValue().trim()
                                    + "\"", Format.PLATIN_TEXT));
                        }
                    } else if (row.getCell(colI).getStringCellValue().trim().equals("HTML")){
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("generalfeedback",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.HTML));
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.HTML));
                        if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue().equals("")) {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                    XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.HTML));
                        } else {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim() + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(),
                                    "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.HTML));
                        }
                        for (int i = 0; i < 10; i++){
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim())
                                    + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI + 2 ).getStringCellValue().trim(),
                                    Format.HTML)), "fraction=\"" + row.getCell(colI + 1 ).getStringCellValue().trim()
                                    + "\"", Format.HTML));
                        }
                    }else if (row.getCell(colI).getStringCellValue().trim().equals("Markdown")){
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("generalfeedback",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.MARKDOWN));
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.MARKDOWN));
                        if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue().equals("")) {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                    XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.MARKDOWN));
                        } else {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim() + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(),
                                    "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.MARKDOWN));
                        }
                        for (int i = 0; i < 10; i++){
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI ).getStringCellValue().trim())
                                    + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI + 2 ).getStringCellValue().trim(),
                                    Format.MARKDOWN)), "fraction=\"" + row.getCell(colI + 1 ).getStringCellValue().trim()
                                    + "\"", Format.MARKDOWN));
                        }
                    }else if (row.getCell(colI).getStringCellValue().trim().equals("moodle_auto_format")){
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("generalfeedback",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                        if (row.getCell(3).getStringCellValue() == null || row.getCell(4).getStringCellValue().equals("")) {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                    XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                        } else {
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim() + XMLUtil.getXMLForImgTag(row.getCell(3).getStringCellValue().trim(),
                                    "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.AUTO_FORMAT));
                        }
                        for (int i = 0; i < 10; i++){
                            mcQuestionBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(row.getCell(colI ).getStringCellValue().trim())
                                    + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(row.getCell(colI + 2 ).getStringCellValue().trim(),
                                    Format.AUTO_FORMAT)), "fraction=\"" + row.getCell(colI + 1 ).getStringCellValue().trim()
                                    + "\"", Format.AUTO_FORMAT));
                        }
                    }
                }
                //hint
                case 39 -> {
                    mcQuestionBuilder.append(XMLUtil.getXMLForTag("hint", XMLUtil.getXMLForTextTag(row.getCell(colI).getStringCellValue().trim()), Format.AUTO_FORMAT));
                }
            }
        }

        mcQuestionBuilder.append("</question>");
        xmlString += mcQuestionBuilder.toString();
    }
    private boolean checkRow(XSSFRow row){
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
