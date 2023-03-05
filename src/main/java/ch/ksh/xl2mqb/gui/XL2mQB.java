package ch.ksh.xl2mqb.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.stage.Window;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;

public class XL2mQB extends Application {

    private final MenuBar menuBar = new MenuBar();
    private final JMetro jMetro = new JMetro();
    private Stage stage;
    private BorderPane rootPane;

    private VBox homeContainer;
    private ProgressContainer progressContainer;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.getIcons().add(new Image(Objects.requireNonNull(XL2mQB.class.getResourceAsStream("/ch/ksh/xl2mqb/images/icon.png"))));
        stage.setTitle("Moodle Question Converter");
        stage.setMinWidth(686);
        stage.setMinHeight(480);
        stage.centerOnScreen();

        rootPane = new BorderPane();
        rootPane.setTop(menuBar);
        rootPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);

        jMetro.setScene(scene);
        jMetro.setStyle(Style.DARK);

        homeContainer = homeContainer();
        progressContainer = new ProgressContainer();

        homeScene();
        stage.show();
        centerStageOnScreen();
    }

    private void homeScene() {
        rootPane.setCenter(homeContainer);
    }

    private VBox homeContainer() {
        // Excel-File
        TextField pathToFile = new TextField();
        pathToFile.setPrefColumnCount(35);

        Button pathToFileButton = new Button("Durchsuchen...");
        pathToFileButton.setGraphic(getFolderImageView());
        pathToFileButton.setOnAction(event -> {

        });

        HBox excelFileContainer = new HBox(20, pathToFile, pathToFileButton);
        excelFileContainer.setPadding(new Insets(20, 40, 20, 40));
        excelFileContainer.setAlignment(Pos.CENTER);

        TitledPane excelFile = new TitledPane("Excel-Datei", excelFileContainer);
        excelFile.setCollapsible(false);

        // options
        ToggleGroup optionsToggleGroup = new ToggleGroup();

        RadioButton saveToPathRB = new RadioButton("Direkt hier abspeichern");
        saveToPathRB.setToggleGroup(optionsToggleGroup);
        saveToPathRB.setSelected(true);

        TextField saveToPath = new TextField();
        saveToPath.setPrefColumnCount(35);

        Button saveToPathButton = new Button("Durchsuchen...");
        saveToPathButton.setGraphic(getFolderImageView());
        saveToPathButton.setOnAction(event -> {

        });

        HBox saveToPathContainer = new HBox(20, saveToPath, saveToPathButton);
        saveToPathContainer.setPadding(new Insets(10, 20, 20, 20));
        saveToPathContainer.setAlignment(Pos.CENTER);
        HBox.setHgrow(pathToFile, Priority.ALWAYS);
        HBox.setHgrow(saveToPath, Priority.ALWAYS);

        RadioButton onlyCheckErrorsRB = new RadioButton("Nur auf Fehler überprüfen");
        onlyCheckErrorsRB.setToggleGroup(optionsToggleGroup);

        VBox optionsContainer = new VBox(saveToPathRB, saveToPathContainer, onlyCheckErrorsRB);
        optionsContainer.setPadding(new Insets(20, 20, 20, 20));


        TitledPane options = new TitledPane("Optionen", optionsContainer);
        options.setCollapsible(false);
        options.setPadding(new Insets(10, 0, 0, 0));

        // convert
        Button convertButton = new Button("Konvertieren");
        convertButton.setDefaultButton(true);
        convertButton.setStyle("-fx-font-size: 18px");
        convertButton.setOnAction(event -> {

        });

        BorderPane convertWrapper = new BorderPane();
        convertWrapper.setPadding(new Insets(17, 20, 0, 0));
        convertWrapper.setRight(convertButton);

        // adding all nodes/components together
        VBox mainContainer = new VBox(10, excelFile, options, convertWrapper);
        mainContainer.setPadding(new Insets(30, 20, 20, 20));

        menuBar.setDisableSettingsMenu(false);
        menuBar.setDisableTemplateMenu(false);

        return mainContainer;
    }

    private void convertRunningScene() {
        _runningProgressScene("Datei wird konvertiert...");
    }

    private void convertFinishScene() {
        Button errorAnalysisButton = new Button("Fehleranalyse");
        errorAnalysisButton.setOnAction(event -> {

        });

        Button saveButton = new Button("Speichern...");
        saveButton.setGraphic(_getFolderImageView(15));
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(event -> {

        });

        Button convertNewFileButton = new Button("Neue Datei konvertieren");
        convertNewFileButton.setStyle("-fx-background-color: #b8d593; -fx-text-fill: black");
        convertNewFileButton.setOnAction(event -> {

        });

        HBox rightButtonsWrapper = new HBox(20, saveButton, convertNewFileButton);

        BorderPane buttonWrapper = new BorderPane();
        buttonWrapper.setPadding(new Insets(10, 0, 0, 0));
        buttonWrapper.setLeft(errorAnalysisButton);
        buttonWrapper.setRight(rightButtonsWrapper);

        progressContainer.setButtonWrapper(buttonWrapper);
        progressContainer.setLabelText("Datei konvertiert");

        rootPane.setCenter(progressContainer);

        menuBar.setDisableSettingsMenu(false);
        menuBar.setDisableTemplateMenu(false);
    }

    private void validateRunningScene() {
        _runningProgressScene("Datei wird überprüft...");
    }

    private void validateFinishScene() {
        Button cancelButton = new Button("Zurück");
        cancelButton.setDefaultButton(true);
        cancelButton.setOnAction(event -> {

        });

        BorderPane cancelWrapper = new BorderPane();
        cancelWrapper.setPadding(new Insets(10, 0, 0, 0));
        cancelWrapper.setRight(cancelButton);

        progressContainer.setButtonWrapper(cancelWrapper);
        progressContainer.setLabelText("Datei vollständig konvertiert");

        rootPane.setCenter(progressContainer);

        menuBar.setDisableSettingsMenu(false);
        menuBar.setDisableTemplateMenu(false);
    }

    private void infoDialog() {
        Dialog<ButtonType> infoDialog = new Dialog<>();
        infoDialog.initOwner(stage);
        infoDialog.setTitle("Info");
        infoDialog.setHeaderText("Version: 1.0");
        infoDialog.setContentText("Lizenz");
        infoDialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> ((Window) event.getSource()).hide());
        infoDialog.show();
        centerDialogRelativeToStage(infoDialog);
    }

    private void _runningProgressScene(String labelText) {
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setStyle("-fx-background-color: #f1c0a2; -fx-text-fill: black");
        cancelButton.setOnAction(event -> {

        });

        BorderPane cancelWrapper = new BorderPane();
        cancelWrapper.setPadding(new Insets(10, 0, 0, 0));
        cancelWrapper.setRight(cancelButton);

        progressContainer.setButtonWrapper(cancelWrapper);
        progressContainer.setLabelText(labelText);

        rootPane.setCenter(progressContainer);

        menuBar.setDisableSettingsMenu(true);
        menuBar.setDisableTemplateMenu(true);

        cancelButton.requestFocus();
    }

    private void centerStageOnScreen() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = visualBounds.getWidth();
        double screenHeight = visualBounds.getHeight();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        if (stageWidth < stage.getMinWidth()) {
            stageWidth = stage.getMinWidth();
        }
        if (stageHeight < stage.getMinHeight()) {
            stageHeight = stage.getMinHeight();
        }

        double x = (screenWidth / 2.0) - (stageWidth / 2.0);
        double y = (screenHeight / 2.0) - (stageHeight / 2.0);

        stage.setX(x);
        stage.setY(y);
    }

    private void centerDialogRelativeToStage(Dialog<?> dialog) {
        double dialogWidth = dialog.getWidth();
        double dialogHeight = dialog.getHeight();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();
        double stageX = stage.getX();
        double stageY = stage.getY();

        double dialogX = ((stageX + (stageWidth / 2.0)) - (dialogWidth / 2.0));
        double dialogY = ((stageY + (stageHeight / 2.0)) - (dialogHeight / 2.0));

        dialog.setX(dialogX);
        dialog.setY(dialogY);
    }

    public void applyJMetroTheme(Style jMetrostyle) {

    }

    public void applyDarkTheme() {

    }

    public void applyLightTheme() {

    }

    public void applySystemTheme() {

    }

    private ImageView getFolderImageView() {
        return _getFolderImageView(20);
    }

    private ImageView getWhiteFolderImageView() {
        return _getWhiteFolderImageView(20);
    }

    private ImageView _getFolderImageView(double fitHeightAndWidth) {
        return _getImageView("folder", fitHeightAndWidth);
    }

    private ImageView _getWhiteFolderImageView(double fitHeightAndWidth) {
        return _getImageView("folder_w", fitHeightAndWidth);
    }

    private ImageView _getImageView(String fileName, double fitHeightAndWidth) {
        ImageView folderImageView = new ImageView(new Image(Objects.requireNonNull(XL2mQB.class.getResourceAsStream("/ch/ksh/xl2mqb/images/" + fileName + ".png"))));
        folderImageView.setFitHeight(fitHeightAndWidth);
        folderImageView.setFitWidth(fitHeightAndWidth);
        return folderImageView;
    }
}
