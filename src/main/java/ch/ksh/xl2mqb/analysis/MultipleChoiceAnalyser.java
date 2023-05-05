package ch.ksh.xl2mqb.analysis;

public class MultipleChoiceAnalyser extends Analyser {

    public MultipleChoiceAnalyser() {
        super();
        sheet = excelHandler.getSheetByName("Multiple-Choice");

        analyseResult = new TabbedStringBuilder("Mappe \"Multiplechoice\":\n");
    }

    @Override
    public void analyse() {
        throw new UnsupportedOperationException();
    }
}
