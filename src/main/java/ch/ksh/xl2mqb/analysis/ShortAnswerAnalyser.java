package ch.ksh.xl2mqb.analysis;

import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * checks all Shortanswer questionns for errors.
 *
 * @author Niklas Vogel
 * @date 05.05.2023
 * @version 1.0
 *
 */

public class ShortAnswerAnalyser extends Analyser {

    /**
     * Constructor: gets the wright sheet and crates a new TabbedStringBuilder
     */
    public ShortAnswerAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Shortanswer");

        analyseResult = new TabbedStringBuilder("Mappe \"Shortanswer\":\n");
    }

    /**
     * checks the Shortanswer sheet for mistakes
     */
    @Override
    public void analyse() {
        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            int step = 0;
            XSSFRow row = sheet.getRow(rowI);
            AnalyserUtil.questionName(analyseResult, row.getCell(0), row.getRowNum());
            AnalyserUtil.points(analyseResult, row.getCell(1), row.getRowNum());
            AnalyserUtil.generalFeedback(analyseResult, row.getCell(2), row.getRowNum());
            AnalyserUtil.picture(analyseResult, row.getCell(3), row.getRowNum());
            AnalyserUtil.hintAndPenalty(analyseResult, row.getCell(5), row.getCell(6), row.getRowNum());
            AnalyserUtil.questionText(analyseResult, row.getCell(7), row.getRowNum());
            for (int i = 0; i < 10; i++) {
                AnalyserUtil.questionAnswer(analyseResult, row.getCell(8 + step), row.getCell(9 + step),
                        row.getCell(10 + step), row.getRowNum(), i + 1);
                step += 3;
            }
        }

        logger.info(analyseResult.toString());
    }
}
