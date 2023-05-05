package ch.ksh.xl2mqb.conversion;

import ch.ksh.xl2mqb.conversion.xml.XMLUtil;

import ch.ksh.xl2mqb.excel.CellExtractor;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * coverts all questions in the Shortanswer sheet into a XML String.
 *
 * @author Niklas Vogel
 * @date 05.05.2023
 * @version 1.0
 *
 */

public class ShortAnswerConverter extends Converter {

    /**
     * Constructor: gets the wright sheet.
     */
    public ShortAnswerConverter() {
        super();
        sheet = excelHandler.getSheetByName("Shortanswer");
    }

    /**
     * iterates through the sheet rows and checks for empty row or rows with not enough data.
     *
     * @return XML String
     */
    public String convert() {
        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            if (isRowEmpty(row)) {
                continue;
            }
            if (!checkRow(row)) {
                logger.info("Die Frage auf Zeile " + (row.getRowNum()+1)
                        + " vom Typ Shortanswer hat nicht alle benötigten Daten.");
            }else {
                convertSingleQuestion(row);
            }
        }

        return xmlString;
    }

    /**
     * iterates through a row and converts the single cells into a XML String.
     *
     * @param row
     */
    private void convertSingleQuestion(XSSFRow row) {
        StringBuilder aShortAnswerBuilder = new StringBuilder("<question " + Type.SHORTANSWER + ">");

        for (int colI = 0; colI < row.getLastCellNum(); colI++) {
                switch (colI) {
                //question name
                case 0 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("name", XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI)))));
                }
                //points
                case 1 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("defaultgrade", CellExtractor.getCellValueSafe(row.getCell(colI))));
                }
                //feedback
                case 2 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("generalfeedback", XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI))), Format.AUTO_FORMAT));
                }
                //case sensitive
                case 4 -> {
                    if (CellExtractor.getCellValueSafe(row.getCell(colI)).equals("Ja")) {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("usecase","1"));
                    } else {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("usecase","0"));
                    }
                }
                //hint
                case 5 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("hint", XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI))), Format.AUTO_FORMAT));
                }
                //penalty
                case 6 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("penalty", CellExtractor.getCellValueSafe(row.getCell(colI))));
                }
                //question with picture
                case 7 -> {
                    if (CellExtractor.getCellValueSafe(row.getCell(3)).equals("")) {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("questiontext",
                                XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI))), Format.AUTO_FORMAT));
                    } else {
                        aShortAnswerBuilder.append(XMLUtil.getXMLForTag("questiontext", XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI)) + XMLUtil.getXMLForImgTag(CellExtractor.getCellValueSafe(row.getCell(3)),
                                "image", "role=\"presentation\"", "class=\"atto_image_button_text-bottom\"")), Format.AUTO_FORMAT));
                    }
                }
                //question answers
                case 8, 11, 14, 17, 20, 23, 26, 29, 32, 35 -> {
                    aShortAnswerBuilder.append(XMLUtil.getXMLForTag("answer", XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI)))
                            + XMLUtil.getXMLForTag("feedback", XMLUtil.getXMLForTextTag(CellExtractor.getCellValueSafe(row.getCell(colI+2)),
                            Format.AUTO_FORMAT)), "fraction=\"" + CellExtractor.getCellValueSafe(row.getCell(colI+1))
                            + "\"", Format.AUTO_FORMAT));
                }
            }
        }
        aShortAnswerBuilder.append("</question>");

        xmlString += aShortAnswerBuilder.toString();
    }

    /**
     * checks the wor for missing data.
     *
     * @param row
     * @return true if there is no data missing
     */
    private boolean checkRow(XSSFRow row) {
        try {
            // does the question have a Name
            if (CellExtractor.getCellValueSafe(row.getCell(0)).isBlank()) {
                return false;
            }
            // does the question have a formulation of a question
            if (CellExtractor.getCellValueSafe(row.getCell(7)).isBlank()) {
                return false;
            }
            // does the question have answer
            if (CellExtractor.getCellValueSafe(row.getCell(8)).isBlank()) {
                return false;
            }
            // does the question have a fraction for the points of one answer
            if (CellExtractor.getCellValueSafe(row.getCell(9)).isBlank()) {
                return false;
            }
        }catch(Exception e){
            return false;
        }

        return true;
    }
}
