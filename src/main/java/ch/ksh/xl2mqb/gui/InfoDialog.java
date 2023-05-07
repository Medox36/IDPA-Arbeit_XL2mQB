package ch.ksh.xl2mqb.gui;

import ch.ksh.xl2mqb.facade.MenuFacade;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

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

        Label contentText = new Label(" Version: 0.0.0.0\n");
        contentText.setTextAlignment(TextAlignment.LEFT);

        Hyperlink hyperlink = new Hyperlink("Verwendete Bibliotheken");
        hyperlink.setTextAlignment(TextAlignment.LEFT);
        hyperlink.setOnAction(event -> MenuFacade.getInstance().openAttributions());

        VBox contentWrapper = new VBox(10, contentText, hyperlink);
        contentWrapper.setAlignment(Pos.CENTER_LEFT);

        getDialogPane().setContent(contentWrapper);

        TextArea licence = new TextArea();
        licence.setFocusTraversable(false);
        licence.setEditable(false);
        licence.setPrefHeight(400);
        String licenceText = "";
        try {
            licenceText = Files.readString(Paths.get(Objects.requireNonNull(InfoDialog.class.getResource("/META-INF/LICENSE")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
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
    }
}
