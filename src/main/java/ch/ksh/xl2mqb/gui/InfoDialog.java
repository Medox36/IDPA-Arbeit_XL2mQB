package ch.ksh.xl2mqb.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class InfoDialog extends Alert {

    public InfoDialog() {
        super(Alert.AlertType.INFORMATION);

    }

    private TextArea getContentPane() {
        throw new UnsupportedOperationException();
    }
}
