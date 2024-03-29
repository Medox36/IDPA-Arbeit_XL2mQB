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

/**
 * Facade for the converter classes
 *
 * @author Isuf Hasani, Lorenzo Giuntini, Niklas Vogel
 * @version 1.0
 */
public class ConvertFacade {

    private static ConvertFacade INSTANCE;
    private Thread converterThread;
    private String xml;
    private volatile boolean isConversionFinished;
    private XL2mQB gui;
    private final Logger logger = LogManager.getLogger(TextAreaAppender.class);

    /**
     * gives the converters a gui
     *
     * @param gui
     */
    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    /**
     * starts all converters
     */
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

            convertedQuestions += new MultipleChoiceConverter().convert();
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

    /**
     * cancels the conversion
     */
    public void cancelConversion() {
        Optional<ButtonType> buttonType = AlertUtil.confirmAlert(
                "Konvertieren",
                "Konvertiervorgang abbrechen",
                "Wollen Sie wirklich den Konvertiervorgang abbrechen?"
        );

        if (buttonType.isPresent()) {
            if (buttonType.get() == ButtonType.YES) {
                converterThread.interrupt();
                converterThread = null;
                gui.homeScene();
            }
        }
    }

    /**
     * saves the xml in a file if the conversion is finished.
     */
    public void saveIfReady() {
        if (isConversionFinished) {
            FileFacade.getInstance().saveXML(xml);
        } else {
            AlertUtil.errorAlert("Konvertieren", "XML abspeichern nicht möglich",
                    "Konvertieren der Excel-Datei scheint nicht richtig abgeschlossen zu sein, " +
                    "daher keine XML-Datei generiert werden.\n" +
                    "Versuchen sie noch einmal zu speichern oder Konvertieren sie erneut.\n" +
                    "Allenfalls empfiehlt sich eine Fehleranalyse.");
        }
    }

    /**
     * gets the Logger
     *
     * @return Logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * crates an instance of the ConverterFacade
     *
     * @return an instance of the Facade
     */
    public static ConvertFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConvertFacade();
        }
        return INSTANCE;
    }
}
