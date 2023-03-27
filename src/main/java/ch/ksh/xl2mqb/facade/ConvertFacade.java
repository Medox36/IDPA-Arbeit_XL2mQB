package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.ProgressContainer;
import ch.ksh.xl2mqb.gui.XL2mQB;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.util.Optional;

public class ConvertFacade {
    private static ConvertFacade INSTANCE;
    private XL2mQB gui;
    private TextArea textArea;

    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
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

    public static ConvertFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConvertFacade();
        }
        return INSTANCE;
    }
}
