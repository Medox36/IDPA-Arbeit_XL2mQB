package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.conversion.xml.XMLUtil;
import ch.ksh.xl2mqb.excel.CellExtractor;

import org.apache.poi.xssf.usermodel.XSSFRow;

public class MultipleChoiceConverter extends Converter {

    public MultipleChoiceConverter(){
        super();
        sheet = excelHandler.getSheetByName("Multiple-Choice");
    }

    public String convert() {
        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            if (isRowEmpty(row)) {
                continue;
            }
            if (!checkRow(row)) {
                logger.info("Die Frage auf Zeile " + (row.getRowNum() + 1)
                        + " vom Typ Multiple Choice hat nicht alle benötigen Daten.");
            }else {
                convertSingleQuestion(row);
            }
        }
        return xmlString;
    }

    private void convertSingleQuestion(XSSFRow row){
        StringBuilder mcQuestionBuilder = new StringBuilder("<question " + Type.MULTICHOICE + ">");
        if (isRowEmpty(row)) {
            return;
        }
        if (!checkRow(row)) {
            logger.info("Die Frage auf Zeile " + (row.getRowNum() + 1)
                    + " vom Typ Multiple Choice hat nicht alle benötigen Daten.");
            return;
        }
        mcQuestionBuilder.append(XMLUtil.getXMLForTag("question", "", "type=\"multichoice\""));
        for (int colI = 0; colI < row.getLastCellNum(); colI++) {
            switch (colI) {
                //question name
                case 0 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("name",
                        XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI)))));
                //points
                case 1 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("defaultgrade",
                        CellExtractor.getCellValueSafe(row.getCell(colI))));
                //feedback
                case 2 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("generalfeedback",
                        XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI))), Format.AUTO_FORMAT));
                //incorrect feedback
                case 3 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("incorrectfeedback",
                        XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI))), Format.AUTO_FORMAT));
                //partially correct feedback
                case 4 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("partiallycorrectfeedback",
                        XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI))),
                        Format.AUTO_FORMAT));
                //correct feedback
                case 5 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("correctfeedback",
                        XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI))),
                        Format.AUTO_FORMAT));
                //answer numbering
                case 6 -> {
                    String cellValue = CellExtractor.getCellValueSafe(row.getCell(colI));
                    switch (cellValue) {
                        case "keine" -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "keine"));
                        case "abc" -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "abc"));
                        case "ABCD" -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "ABCD"));
                        case "123" -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "123"));
                    }
                }
                //single
                case 7 -> {
                    if (CellExtractor.getCellValueSafe(row.getCell(colI)).equals("Ja")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("single", "true"));
                    } else {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("single", "false"));
                    }
                }
                //shuffle answers
                case 8 ->{
                    if (CellExtractor.getCellValueSafe(row.getCell(colI)).equals("Ja")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "true"));
                    } else {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("shuffleanswers", "false"));
                    }
                }
                //hint
                case 10 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("hint",
                        XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI))), Format.AUTO_FORMAT));
                //penalty
                case 11 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("penalty",
                        CellExtractor.getCellValueSafe(row.getCell(colI))));
                //question with picture
                case 12 -> {
                    if (CellExtractor.getCellValueSafe(row.getCell(9)).equals("")) {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI))), Format.AUTO_FORMAT));
                    } else {
                        mcQuestionBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI)) + XMLUtil.getXMLForImgTag(CellExtractor.getCellValueSafe(row.getCell(9)),
                                "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.AUTO_FORMAT));
                    }
                }
                //question answers
                case 13, 16, 19, 22, 25, 28, 31, 34, 37, 40, 43 -> mcQuestionBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI)))
                        + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForCDATATextTag(CellExtractor.getCellValueSafe(row.getCell(colI+2)),
                        Format.AUTO_FORMAT)), "fraction=\"" + CellExtractor.getCellValueSafe(row.getCell(colI+1))
                        + "\"", Format.AUTO_FORMAT));
            }
        }
        mcQuestionBuilder.append("</question>");
        xmlString += mcQuestionBuilder.toString();
    }

    private boolean checkRow(XSSFRow row){
        // does the question have a Name
        if (CellExtractor.getCellValueSafe(row.getCell(0)).isBlank()) {
            return false;
        }
        // does the question have a formulation of a question
        if (CellExtractor.getCellValueSafe(row.getCell(12)).isBlank()) {
            return false;
        }
        // does the question have answer
        if (CellExtractor.getCellValueSafe(row.getCell(13)).isBlank()) {
            return false;
        }
        // does the question have a fraction for the points of one answer
        if (CellExtractor.getCellValueSafe(row.getCell(14)).isBlank()) {
            return false;
        }
        return true;
    }
}