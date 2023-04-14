package ch.ksh.xl2mqb.analysis;

public class MultipleChoiceAnalyser extends Analyser {

    private final TabbedStringBuilder multipleChoiceAnalyseResult;

    public MultipleChoiceAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Multiplechoice");

        multipleChoiceAnalyseResult = new TabbedStringBuilder("Mappe \"Multiplechoice\":\n");
    }

    @Override
    public void analyse() {
        throw new UnsupportedOperationException();
    }
}
