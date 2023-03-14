package ch.ksh.xl2mqb.facade;

import javafx.scene.control.TextArea;

public class ConvertFacade {
    private static ConvertFacade INSTANCE;
    private TextArea textArea;

    public void setTextArea(TextArea textArea) {
        throw new UnsupportedOperationException();
    }

    public void startConvert() {
        throw new UnsupportedOperationException();
    }

    public static ConvertFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConvertFacade();
        }
        return INSTANCE;
    }
}
