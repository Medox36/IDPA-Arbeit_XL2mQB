package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.conversion.ClozeConverter;
import ch.ksh.xl2mqb.conversion.MultipleChoiceConverter;
import ch.ksh.xl2mqb.conversion.ShortAnswerConverter;
import ch.ksh.xl2mqb.conversion.xml.XMLUtil;
import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.ProgressContainer;
import ch.ksh.xl2mqb.gui.XL2mQB;
import ch.ksh.xl2mqb.log.TextAreaAppender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConvertFacade {

    private static ConvertFacade INSTANCE;
    private Thread converterThread;
    private String xml;
    private volatile boolean isConversionFinished;
    private XL2mQB gui;
    private final Logger logger = LogManager.getLogger(TextAreaAppender.class);

    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    public void startConvert() {
        gui.convertRunningScene();
        ProgressContainer progressContainer = gui.getProgressContainer();
        progressContainer.clearTextArea();
        progressContainer.setProgress(0.0);

        isConversionFinished = false;
        xml = "";

        converterThread = new Thread(() -> {
            xml = XMLUtil.xmlHeader;
            String convertedQuestions = "";

            //convertedQuestions += new MultipleChoiceConverter().convert();
            Platform.runLater(() -> progressContainer.setProgress(0.3));
            convertedQuestions += new ShortAnswerConverter().convert();
            Platform.runLater(() -> progressContainer.setProgress(0.6));
            convertedQuestions += new ClozeConverter().convert();
            Platform.runLater(() -> progressContainer.setProgress(0.9));

            xml += XMLUtil.getXMLForTag("quiz", convertedQuestions);

            isConversionFinished = true;

            Platform.runLater(() -> {
                gui.getProgressContainer().setProgress(1.0);
                gui.convertFinishScene();
            });
        }, "Converter-Thread");
        converterThread.start();
    }

    public void cancelConversion() {
        Optional<ButtonType> buttonType = AlertUtil.confirmAlert("Konvertieren", "Konvertiervorgang abbrechen", "Wollen Sie wirklich den Konvertiervorgang abbrechen?");

        if (buttonType.isPresent()) {
            if (buttonType.get() == ButtonType.YES) {
                converterThread.interrupt();
                converterThread = null;
                gui.homeScene();
            }
        }
    }

    public void saveIfReady() {
        if (isConversionFinished) {
            FileFacade.getInstance().saveXML(xml);
        } else {
            AlertUtil.errorAlert("Konvertieren", "XML abspeichern nicht möglich",
                    """
                            Konvertieren der Excel-Datei scheint nicht richtig abgeschlossen zu sein, daher keine XML-Datei generiert werden.
                            Versuchen sie noch einmal zu speichern oder Konvertieren sie erneut.
                            Allenfalls empfiehlt sich eine Fehleranalyse.""");
        }
    }
    public Logger getLogger() {
        return logger;
    }

    public static ConvertFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConvertFacade();
        }
        return INSTANCE;
    }
}
