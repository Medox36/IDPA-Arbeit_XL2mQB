package ch.ksh.xl2mqb.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class InfoDialog extends Alert {


    public InfoDialog() {
        super(Alert.AlertType.INFORMATION);

        setTitle("Info");
        setHeaderText("Moodle Question Converter \"XL2mQB\"");
        setContentText("Version: 0.0.0.0\n\n");
        getDialogPane().setExpandableContent(getContentPane());

        ButtonBar dialogButtonBar = (ButtonBar) getDialogPane().getChildren().get(2);

        Hyperlink expandLicence = (Hyperlink) dialogButtonBar.getButtons().get(0);
        expandLicence.setText("Lizenz anzeigen");
        getDialogPane().expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                expandLicence.setText("Lizenz ausblenden");
            } else {
                expandLicence.setText("Lizenz anzeigen");
            }
            System.out.println(newValue);
        });

        final String commonJMetroStyle = "-fx-border-width: 2;-fx-background-radius: 0;-fx-background-insets: 0;-fx-text-fill: white; -fx-font-family: Segoe UI;-fx-font-size: 1em;";

        Button okButton = (Button) dialogButtonBar.getButtons().get(1);
        okButton.setStyle("-fx-border-color: transparent;-fx-background-color: #0078d7;" + commonJMetroStyle);
        okButton.setOnMouseEntered(event -> okButton.setStyle("-fx-border-color: derive(#0078d7, -40%);-fx-background-color: #0078d7;" + commonJMetroStyle));
        okButton.setOnMouseExited(event -> okButton.setStyle("-fx-border-color: transparent;-fx-background-color: #0078d7;" + commonJMetroStyle));
        okButton.setOnMousePressed(event -> okButton.setStyle("-fx-border-color: transparent;-fx-background-color: derive(#0078d7, -40%);" + commonJMetroStyle));
        okButton.setOnMouseReleased(event -> okButton.setStyle("-fx-border-color: transparent;-fx-background-color: #0078d7;" + commonJMetroStyle));

        getDialogPane().setPrefWidth(460);

        // getDialogPane().setExpanded(true);
    }

    private TextArea getContentPane() {
        TextArea licence = new TextArea();
        licence.setFocusTraversable(false);
        licence.setEditable(false);
        licence.setPrefHeight(400);
        String licenceText = "";
        try {
            licenceText = Files.readString(Paths.get(Objects.requireNonNull(InfoDialog.class.getResource("/META-INF/LICENCE")).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        licence.setText(licenceText);
        licence.setStyle("-fx-font-size: 11px");

        return licence;
    }
}
