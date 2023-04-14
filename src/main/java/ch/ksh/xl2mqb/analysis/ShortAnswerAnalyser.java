package ch.ksh.xl2mqb.analysis;

public class ShortAnswerAnalyser extends Analyser {

    private final TabbedStringBuilder shortanswerAnalyseResult;

    public ShortAnswerAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Shortanswer");

        shortanswerAnalyseResult = new TabbedStringBuilder("Mappe \"Shortanswer\":\n");
    }

    @Override
    public void analyse() {
        throw new UnsupportedOperationException();
    }
}
