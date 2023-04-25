package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.analysis.ClozeAnalyser;
import ch.ksh.xl2mqb.analysis.MultipleChoiceAnalyser;
import ch.ksh.xl2mqb.analysis.ShortAnswerAnalyser;
import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.ProgressContainer;
import ch.ksh.xl2mqb.gui.XL2mQB;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AnalysisFacade {

    private Thread analyserThread;

    private volatile boolean isAnalyserFinished;
    private static AnalysisFacade INSTANCE;
    private XL2mQB gui;

    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    public void startAnalysis() {
        gui.analysisRunningScene();
        ProgressContainer progressContainer = gui.getProgressContainer();
        progressContainer.clearTextArea();
        progressContainer.setProgress(0.0);

        analyserThread = new Thread(() -> {

            new MultipleChoiceAnalyser().analyse();
            new ShortAnswerAnalyser().analyse();
            new ClozeAnalyser().analyse();

            isAnalyserFinished = true;

            Platform.runLater(() -> {
                gui.getProgressContainer().setProgress(1.0);
                gui.analysisFinishScene();
            });
        }, "Analyser-Thread");
        analyserThread.start();

    }

    public boolean isAnalyserFinished() {
        return isAnalyserFinished;
    }

    public void cancelAnalysis() {
        Optional<ButtonType> buttonType = AlertUtil.confirmAlert("Analyse", "Analyse abbrechen", "Wollen Sie wirklich die Analyse abbrechen?");

        if (buttonType.isPresent()) {
            if (buttonType.get() == ButtonType.YES) {
                analyserThread.interrupt();
                analyserThread = null;
                gui.homeScene();
            }
        }
    }

    public static AnalysisFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnalysisFacade();
        }
        return INSTANCE;
    }
}
