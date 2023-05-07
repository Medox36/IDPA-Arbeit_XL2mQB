package ch.ksh.xl2mqb.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import jfxtras.styles.jmetro.Style;

import java.util.Objects;
import java.util.Optional;

/**
 * Util class to prompt the user with alert dialogs.
 * @see Alert
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public class AlertUtil {

    /**
     * Alert dialog with no specific type.
     *
     * @param title ot the alert dialog window
     * @param headerText of the alert dialog
     * @param contextText of the alert dialog
     * @return an Optional<ButtonType> object of the pressed button by the user
     */
    public static Optional<ButtonType> alert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.NONE, title, headerText, contextText);
    }

    /**
     * Alert dialog to confirm something.
     *
     * @param title ot the alert dialog window
     * @param headerText of the alert dialog
     * @param contextText of the alert dialog
     * @return an Optional<ButtonType> object of the pressed button by the user
     */
    public static Optional<ButtonType> confirmAlert(String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        applyStyle(alert);
        return alert.showAndWait();
    }

    /**
     * Alert dialog to show information.
     *
     * @param title ot the alert dialog window
     * @param headerText of the alert dialog
     * @param contextText of the alert dialog
     * @return an Optional<ButtonType> object of the pressed button by the user
     */
    public static Optional<ButtonType> infoAlert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.INFORMATION, title, headerText, contextText);
    }

    /**
     * Alert dialog to show a warning.
     *
     * @param title ot the alert dialog window
     * @param headerText of the alert dialog
     * @param contextText of the alert dialog
     * @return an Optional<ButtonType> object of the pressed button by the user
     */
    public static Optional<ButtonType> warningAlert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.WARNING, title, headerText, contextText);
    }

    /**
     * Alert dialog to show an error.
     *
     * @param title ot the alert dialog window
     * @param headerText of the alert dialog
     * @param contextText of the alert dialog
     * @return an Optional<ButtonType> object of the pressed button by the user
     */
    public static Optional<ButtonType> errorAlert(String title, String headerText, String contextText) {
        return alert(Alert.AlertType.ERROR, title, headerText, contextText);
    }

    /**
     * Creates an alert dialog with the given parameters.
     *
     * @param alertType of the alert dialog
     * @param title ot the alert dialog window
     * @param headerText of the alert dialog
     * @param contextText of the alert dialog
     * @return an Optional<ButtonType> object of the pressed button by the user
     */
    private static Optional<ButtonType> alert(
            Alert.AlertType alertType,
            String title,
            String headerText,
            String contextText
    ) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        applyStyle(alert);
        return alert.showAndWait();
    }

    /**
     * Applies the current gui style to the alert dialog.
     * Also, it positiones the dialog window relative to the main window.
     *
     * @param alert dialog to apply the style to
     * @see XL2mQB#positionDialogRelativeToStage(Dialog)
     */
    protected static void applyStyle(Alert alert) {
        String cssPath;

        if (XL2mQB.getCurrentJMetroStyle() == Style.DARK) {
            cssPath = "/ch/ksh/xl2mqb/css/alert_dark.css";
        } else {
            cssPath = "/ch/ksh/xl2mqb/css/alert_light.css";
        }

        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(AlertUtil.class.getResource(cssPath)).toExternalForm()
        );
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(
                new Image(Objects.requireNonNull(
                        AlertUtil.class.getResourceAsStream("/ch/ksh/xl2mqb/images/icon.png"))
                )
        );

        XL2mQB.positionDialogRelativeToStage(alert);
    }
}
