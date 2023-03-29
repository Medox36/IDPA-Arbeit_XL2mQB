package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.ProgressContainer;
import ch.ksh.xl2mqb.gui.XL2mQB;
import ch.ksh.xl2mqb.log.TextAreaAppender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConvertFacade {
    private static ConvertFacade INSTANCE;
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


    }

    public void cancelConversion() {
        Optional<ButtonType> buttonType = AlertUtil.confirmAlert("Konvertieren", "Konvertiervorgang abbrechen", "Wollen Sie wirklich den Konvertiervorgang abbrechen?");

        if (buttonType.isPresent()) {
            if (buttonType.get() == ButtonType.YES) {
                // TODO cancel conversion
                gui.homeScene();
            }
        }
    }

    private void finishConversion() {
        throw new UnsupportedOperationException();
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
