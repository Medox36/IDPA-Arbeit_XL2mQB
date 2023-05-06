package ch.ksh.xl2mqb.analysis;

import ch.ksh.xl2mqb.excel.CellExtractor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class MultipleChoiceAnalyser extends Analyser {

    public MultipleChoiceAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Multiplechoice");

        analyseResult = new TabbedStringBuilder("Mappe \"Multiplechoice\":\n");
    }

    @Override
    public void analyse() {
        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            int step = 0;
            XSSFRow row = sheet.getRow(rowI);
            AnalyserUtil.questionName(analyseResult, row.getCell(0), row.getRowNum());
            AnalyserUtil.points(analyseResult, row.getCell(1), row.getRowNum());
            AnalyserUtil.generalFeedback(analyseResult, row.getCell(2), row.getRowNum());
            AnalyserUtil.incorrectFeedback(analyseResult, row.getCell(3), row.getRowNum());
            AnalyserUtil.partiallyCorrect(analyseResult, row.getCell(4), row.getRowNum());
            AnalyserUtil.correctFeedback(analyseResult, row.getCell(5), row.getRowNum());
            AnalyserUtil.hintAndPenalty(analyseResult, row.getCell(10), row.getCell(11), row.getRowNum());
            AnalyserUtil.questionText(analyseResult, row.getCell(12), row.getRowNum());
            for (int i = 0; i < 11; i++) {
                AnalyserUtil.questionAnswer(analyseResult, row.getCell(13 + step), row.getCell(14 + step), row.getCell(15 + step), row.getRowNum(), i+1);
                step += 3;
            }
        }
        logger.info(analyseResult.toString());
    }
}
