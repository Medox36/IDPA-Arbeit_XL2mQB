package ch.ksh.xl2mqb.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;

import javafx.scene.paint.Color;
import jfxtras.styles.jmetro.Style;

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

        getDialogPane().setExpandableContent(licence);

        ButtonBar dialogButtonBar = (ButtonBar) getDialogPane().getChildren().get(2);

        Hyperlink expandLicence = (Hyperlink) dialogButtonBar.getButtons().get(0);
        expandLicence.setText("Lizenz anzeigen");
        getDialogPane().expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                expandLicence.setText("Lizenz ausblenden");
            } else {
                expandLicence.setText("Lizenz anzeigen");
            }
        });

        getDialogPane().setPrefWidth(460);

        if (XL2mQB.getCurrentJMetroStyle() == Style.DARK) {
            expandLicence.setTextFill(Color.WHITE);
        }
        AlertUtil.applyStyle(this);

        // getDialogPane().setExpanded(true);
    }
}
