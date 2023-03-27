package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.ProgressContainer;
import ch.ksh.xl2mqb.gui.XL2mQB;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.util.Optional;

public class AnalysisFacade {
    private static AnalysisFacade INSTANCE;
    private XL2mQB gui;
    private TextArea textArea;

    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public void startAnalysis() {
        gui.analysisRunningScene();
        ProgressContainer progressContainer = gui.getProgressContainer();
        progressContainer.clearTextArea();
        progressContainer.setProgress(0.0);


    }

    public void cancelAnalysis() {
        Optional<ButtonType> buttonType = AlertUtil.confirmAlert("Analyse", "Analyse abbrechen", "Wollen Sie wirklich die Analyse abbrechen?");

        if (buttonType.isPresent()) {
            if (buttonType.get() == ButtonType.YES) {
                // TODO cancel analysis
                gui.homeScene();
            }
        }
    }

    private void finishAnalysis() {
        throw new UnsupportedOperationException();
    }

    public static AnalysisFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnalysisFacade();
        }
        return INSTANCE;
    }
}
