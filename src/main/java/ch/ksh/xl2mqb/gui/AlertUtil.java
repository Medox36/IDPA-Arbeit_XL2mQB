package ch.ksh.xl2mqb.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtil {

    public static Optional<ButtonType> alert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.NONE, title, headerText, contextText);
    }

    public static Optional<ButtonType> confirmAlert(String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        XL2mQB.centerDialogRelativeToStage(alert);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> infoAlert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.INFORMATION, title, headerText, contextText);
    }

    public static Optional<ButtonType> warningAlert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.WARNING, title, headerText, contextText);
    }

    public static Optional<ButtonType> errorAlert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.ERROR, title, headerText, contextText);
    }

    private static Optional<ButtonType> alert(Alert.AlertType alertType, String title, String headerText, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        XL2mQB.centerDialogRelativeToStage(alert);
        return alert.showAndWait();
    }
}
