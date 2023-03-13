package ch.ksh.xl2mqb.facade;

import javafx.scene.control.TextArea;

public class AnalysisFacade {
    private static AnalysisFacade INSTANCE;
    private TextArea textArea;

    public void setTextArea(TextArea textArea) {
        throw new UnsupportedOperationException();
    }

    public void startAnalysis() {
        throw new UnsupportedOperationException();
    }

    public static AnalysisFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnalysisFacade();
        }
        return INSTANCE;
    }
}
