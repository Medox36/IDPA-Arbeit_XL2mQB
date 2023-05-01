package ch.ksh.xl2mqb.analysis;

import ch.ksh.xl2mqb.conversion.ClozeConverter;
import ch.ksh.xl2mqb.excel.CellExtractor;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClozeAnalyser extends Analyser {
    private final XSSFSheet subQuestionSheet;
    private final TabbedStringBuilder clozeShortanswerAnalyseResult;

    public ClozeAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Cloze");
        subQuestionSheet = excelHandler.getSheetByName("Cloze_Shortanswer");

        analyseResult = new TabbedStringBuilder("Mappe \"Cloze\":\n");
        clozeShortanswerAnalyseResult = new TabbedStringBuilder("Mappe \"Cloze_Shortanswer\":\n");
    }

    @Override
    public void analyse() {
        analyzeSubQuestionNumbers();

        for (int rowI = 1; rowI <= sheet.getLastRowNum(); rowI++) {
            XSSFRow row = sheet.getRow(rowI);
            analyseSingleQuestion(row);
        }

        logger.info(analyseResult.toString());
        logger.info(clozeShortanswerAnalyseResult.toString());
    }

    private void analyseSingleQuestion(XSSFRow row) {
        final int rowNum = row.getRowNum();

        AnalyserUtil.questionName(analyseResult, row.getCell(0), rowNum);
        AnalyserUtil.generalFeedback(analyseResult, row.getCell(1), rowNum);
        AnalyserUtil.picture(analyseResult, row.getCell(2), rowNum);
        AnalyserUtil.hintAndPenalty(analyseResult, row.getCell(3), row.getCell(4), rowNum);
        AnalyserUtil.questionText(analyseResult, row.getCell(5), rowNum);

        analyseSubQuestions(row);
    }

    private void analyseSubQuestions(XSSFRow row) {
        XSSFCell[] cells = new XSSFCell[10];
        int rowNum = row.getRowNum();
        for (int i = 6; i < 16; i++) {
            cells[i - 6] = row.getCell(i);
        }
        if (Arrays.stream(cells).noneMatch(xssfCell -> CellExtractor.getCellValueSafe(xssfCell).isBlank())) {
            clozeShortanswerAnalyseResult.appendTabbed(rowNum, "hat keine angegebenen Fragenummern.");
            return;
        }
        for (XSSFCell cell : cells) {
            String cellValue = CellExtractor.getCellValueSafe(cell);
            if (cellValue.isEmpty()) {
                continue;
            }
            if (!AnalyserUtil.isNumeric(cellValue)) {
                clozeShortanswerAnalyseResult.appendTabbed(rowNum, "hat eine ungültige Fragenummer: \"" + cellValue + "\"");
            } else {
                XSSFRow subQuestionRow = ClozeConverter.getRowForMatchingQuestionNumber(cellValue, subQuestionSheet);
                if (subQuestionRow == null) {
                    clozeShortanswerAnalyseResult.appendTabbed(rowNum, "hat eine Fragenummer: \"" + cellValue + "\" zu der keine passende Frage gefunden werden kann.");
                } else {
                    analyseSubQuestion(subQuestionRow);
                }
            }
        }
    }

    private void analyzeSubQuestionNumbers() {
        Set<Integer> nums = new HashSet<>();
        for (int i = 1; i <= subQuestionSheet.getLastRowNum(); i++) {
            String cellValue = CellExtractor.getCellValueSafe(subQuestionSheet.getRow(i).getCell(0));
            if (AnalyserUtil.isNumeric(cellValue)) {
                if (!nums.add((int) Double.parseDouble(cellValue))) {
                    clozeShortanswerAnalyseResult.appendTabbed(i, "hat eine Fragenummer, die schon verwendet wird.");
                }
            } else {
                clozeShortanswerAnalyseResult.appendTabbed(i, " hat eine ungültige Fragenummer.");
            }
        }
    }

    private void analyseSubQuestion(XSSFRow row) {
        final int rowNum = row.getRowNum();

        AnalyserUtil.questionName(clozeShortanswerAnalyseResult, row.getCell(1), rowNum);
        AnalyserUtil.points(clozeShortanswerAnalyseResult, row.getCell(2), rowNum);
        AnalyserUtil.picture(clozeShortanswerAnalyseResult, row.getCell(3), rowNum);
        AnalyserUtil.questionText(clozeShortanswerAnalyseResult, row.getCell(4), rowNum);

        int answerNum = 1;
        for (int i = 5; i < row.getLastCellNum(); i += 3) {
            AnalyserUtil.questionAnswer(clozeShortanswerAnalyseResult, row.getCell(i), row.getCell(i + 1), row.getCell(i + 2), rowNum, answerNum);
            answerNum++;
        }
    }

}
