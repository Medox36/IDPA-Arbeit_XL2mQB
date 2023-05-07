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

/**
 *  Facade for the Analyser classes
 *
 * @author Lorenzo Giuntini, Niklas Vogel
 * @version 1.0
 *
 */
public class AnalysisFacade {

    private static AnalysisFacade INSTANCE;
    private Thread analyserThread;
    private volatile boolean isAnalyserFinished;
    private XL2mQB gui;

    /**
     * gives the analysers a gui
     *
     * @param gui
     */
    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    /**
     * starts all the analysers
     */
    public void startAnalysis() {
        gui.analysisRunningScene();
        ProgressContainer progressContainer = gui.getProgressContainer();
        progressContainer.clearTextArea();
        progressContainer.setProgress(0.0);

        analyserThread = new Thread(() -> {

            new MultipleChoiceAnalyser().analyse();
            Platform.runLater(() -> progressContainer.setProgress(0.3));
            new ShortAnswerAnalyser().analyse();
            Platform.runLater(() -> progressContainer.setProgress(0.6));
            new ClozeAnalyser().analyse();
            Platform.runLater(() -> progressContainer.setProgress(0.9));

            isAnalyserFinished = true;

            Platform.runLater(() -> {
                gui.getProgressContainer().setProgress(1.0);
                gui.analysisFinishScene();
            });
        }, "Analyser-Thread");
        analyserThread.start();

    }

    /**
     * checks if analyser is finished
     *
     * @return boolean
     */
    public boolean isAnalyserFinished() {
        return isAnalyserFinished;
    }

    /**
     * cancels the analysis
     */
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

    /**
     * crates an instance of the AnalyserFacade
     *
     * @return an instance of the Facade
     */
    public static AnalysisFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnalysisFacade();
        }
        return INSTANCE;
    }
}
