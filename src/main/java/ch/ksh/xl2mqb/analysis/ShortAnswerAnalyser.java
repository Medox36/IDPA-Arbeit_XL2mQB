package ch.ksh.xl2mqb.analysis;

import org.apache.poi.xssf.usermodel.XSSFRow;

public class ShortAnswerAnalyser extends Analyser {

    private final TabbedStringBuilder shortanswerAnalyseResult;
    //steps in which the next field is
    int step = 0;

    public ShortAnswerAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Shortanswer");

        shortanswerAnalyseResult = new TabbedStringBuilder("Mappe \"Shortanswer\":\n");
    }

    @Override
    public void analyse() {
        for (int rowI = 0; rowI < sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            AnalyserUtil.questionName(shortanswerAnalyseResult, row.getCell(0), row.getRowNum());
            AnalyserUtil.points(shortanswerAnalyseResult, row.getCell(1), row.getRowNum());
            AnalyserUtil.generalFeedback(shortanswerAnalyseResult, row.getCell(2), row.getRowNum());
            AnalyserUtil.picture(shortanswerAnalyseResult, row.getCell(3), row.getRowNum());
            AnalyserUtil.hintAndPenalty(shortanswerAnalyseResult, row.getCell(5),row.getCell(7), row.getRowNum());
            AnalyserUtil.questionText(shortanswerAnalyseResult, row.getCell(7), row.getRowNum());
            for (int i = 1; i < 10; i++) {
                AnalyserUtil.questionAnswer(shortanswerAnalyseResult, row.getCell(8+step), row.getCell(9+step), row.getCell(10+step), row.getRowNum(), i);
                step+=3;
            }

            logger.info(shortanswerAnalyseResult.toString());
        }
    }
}
