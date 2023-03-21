package ch.ksh.xl2mqb.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import jfxtras.styles.jmetro.Style;

import java.util.Objects;
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
        applyStyle(alert);
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
        applyStyle(alert);
        return alert.showAndWait();
    }

    protected static void applyStyle(Alert alert) {
        String cssPath;

        if (XL2mQB.getCurrentJMetroStyle() == Style.DARK) {
            cssPath = "/ch/ksh/xl2mqb/css/alert_dark.css";
        } else {
            cssPath = "/ch/ksh/xl2mqb/css/alert_light.css";
        }

        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(AlertUtil.class.getResource(cssPath)).toExternalForm());
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(AlertUtil.class.getResourceAsStream("/ch/ksh/xl2mqb/images/icon.png"))));

        XL2mQB.positionDialogRelativeToStage(alert);
        /*
        final String commonJMetroStyle = "-fx-border-width: 2;-fx-background-radius: 0;-fx-background-insets: 0;-fx-text-fill: white; -fx-font-family: Segoe UI;-fx-font-size: 1em;";

        Style style = XL2mQB.getCurrentJMetroStyle();

        ObservableList<Node> dialogChildren = alert.getDialogPane().getChildren();

        if (style == Style.DARK) {
            alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(AlertUtil.class.getResource("/ch/ksh/xl2mqb/css/alert_dark.css")).toExternalForm());
            dialogChildren.get(0).setStyle("-fx-background-color: #323232; text-emphasis: white;");
            dialogChildren.get(1).setStyle("-fx-background-color: #252525; -fx-text-fill: white");
            dialogChildren.get(2).setStyle("-fx-background-color: #252525; -fx-text-fill: white");
        }

        for (ButtonType buttonType : alert.getDialogPane().getButtonTypes()) {
            String backgroundColor;
            String backgroundPressedColor;
            String borderHoverColor;
            String backgroundHoverColor;
            if (buttonType == ButtonType.YES || buttonType == ButtonType.OK) {
                backgroundColor = "#0078d7";
                backgroundPressedColor = "#004981";
                borderHoverColor = "#004981";
                backgroundHoverColor = "#0078d7";
            } else {
                if (style == Style.LIGHT) {
                    backgroundColor = "#cccccc";
                    backgroundPressedColor = "#999999";
                    borderHoverColor = "#7a7a7a";
                    backgroundHoverColor = "#c5c5c5";
                } else {
                    backgroundColor = "#333333";
                    backgroundPressedColor = "#666666";
                    borderHoverColor = "#858585";
                    backgroundHoverColor = "#505050";
                }
            }
            Button okButton = (Button) alert.getDialogPane().lookupButton(buttonType);
            okButton.setStyle("-fx-border-color: transparent;-fx-background-color: " + backgroundColor + ";" + commonJMetroStyle);
            okButton.setOnMouseEntered(event -> okButton.setStyle("-fx-border-color: " + borderHoverColor + ";-fx-background-color: " + backgroundHoverColor + ";" + commonJMetroStyle));
            okButton.setOnMouseExited(event -> okButton.setStyle("-fx-border-color: transparent;-fx-background-color: " + backgroundColor + ";" + commonJMetroStyle));
            okButton.setOnMousePressed(event -> okButton.setStyle("-fx-border-color: transparent;-fx-background-color: derive(" + backgroundPressedColor + ", -40%);" + commonJMetroStyle));
            okButton.setOnMouseReleased(event -> okButton.setStyle("-fx-border-color: transparent;-fx-background-color: " + backgroundColor + ";" + commonJMetroStyle));
        }
        */
    }
}
