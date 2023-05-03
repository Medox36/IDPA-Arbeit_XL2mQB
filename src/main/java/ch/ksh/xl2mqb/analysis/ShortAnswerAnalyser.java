package ch.ksh.xl2mqb.analysis;

import ch.ksh.xl2mqb.excel.CellExtractor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ShortAnswerAnalyser extends Analyser {

    public ShortAnswerAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Shortanswer");

        analyseResult = new TabbedStringBuilder("Mappe \"Shortanswer\":\n");
    }

    @Override
    public void analyse() {
        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            int step = 0;
            XSSFRow row = sheet.getRow(rowI);
            AnalyserUtil.questionName(analyseResult, row.getCell(0), row.getRowNum()+1);
            AnalyserUtil.points(analyseResult, row.getCell(1), row.getRowNum()+1);
            AnalyserUtil.generalFeedback(analyseResult, row.getCell(2), row.getRowNum()+1);
            AnalyserUtil.picture(analyseResult, row.getCell(3), row.getRowNum()+1);
            AnalyserUtil.hintAndPenalty(analyseResult, row.getCell(5),row.getCell(6), row.getRowNum()+1);
            AnalyserUtil.questionText(analyseResult, row.getCell(7), row.getRowNum()+1);
            for (int i = 0; i < 10; i++) {
                AnalyserUtil.questionAnswer(analyseResult, row.getCell(8 + step), row.getCell(9 + step), row.getCell(10 + step), row.getRowNum()+1, i+1);
                step += 3;
            }
        }

        logger.info(analyseResult.toString());
    }
}
