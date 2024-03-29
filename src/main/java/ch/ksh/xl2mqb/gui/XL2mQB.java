package ch.ksh.xl2mqb.gui;

import ch.ksh.xl2mqb.facade.AnalysisFacade;
import ch.ksh.xl2mqb.facade.ConvertFacade;
import ch.ksh.xl2mqb.facade.FileFacade;
import ch.ksh.xl2mqb.facade.StartupFacade;

import com.jthemedetecor.OsThemeDetector;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;

/**
 * The main GUI class
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public class XL2mQB extends Application {

    private final ConvertFacade convertFacade = ConvertFacade.getInstance();
    private final AnalysisFacade analysisFacade = AnalysisFacade.getInstance();

    private final MenuBar menuBar = new MenuBar();
    private static final JMetro jMetro = new JMetro();
    private static Stage stage;
    private BorderPane rootPane;

    private VBox homeContainer;
    private ProgressContainer progressContainer;
    private Button saveToPathButton;
    private Button pathToFileButton;
    private Button saveButton;
    private TextField saveToPath;
    private TextField pathToFile;

    /**
     * Method to start the gui.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.getIcons().add(new Image(
                Objects.requireNonNull(XL2mQB.class.getResourceAsStream("/ch/ksh/xl2mqb/images/icon.png"))
        ));
        stage.setTitle("Moodle Question Converter");
        stage.setMinWidth(350);
        stage.setMinHeight(70);
        stage.centerOnScreen();

        rootPane = new BorderPane();
        rootPane.setTop(menuBar);
        rootPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);

        jMetro.setScene(scene);

        homeContainer = homeContainer();
        progressContainer = new ProgressContainer();

        homeScene();
        stage.show();
        new StartupFacade(this).onStartup();
    }

    /**
     * Show the home scene
     */
    public void homeScene() {
        rootPane.setCenter(homeContainer);
    }

    /**
     * Generates the home container.
     *
     * @return home container
     */
    private VBox homeContainer() {
        // Excel-File
        pathToFile = new TextField();
        pathToFile.setPrefColumnCount(35);

        pathToFileButton = new Button("Durchsuchen...");
        pathToFileButton.setGraphic(getFolderImageView());
        pathToFileButton.setMinWidth(137);
        pathToFileButton.setOnAction(event -> FileFacade.getInstance().selectExcelFile());

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

        saveToPath = new TextField();
        saveToPath.setPrefColumnCount(35);

        saveToPathButton = new Button("Durchsuchen...");
        saveToPathButton.setGraphic(getFolderImageView());
        saveToPathButton.setMinWidth(137);
        saveToPathButton.setOnAction(event -> FileFacade.getInstance().selectSaveDirectory());

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
            if (saveToPathRB.isSelected()) {
                convertFacade.startConvert();
            } else {
                analysisFacade.startAnalysis();
            }
        });

        optionsToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == onlyCheckErrorsRB) {
                convertButton.setText("Analysieren");
            } else {
                convertButton.setText("Konvertieren");
            }
        });

        BorderPane convertWrapper = new BorderPane();
        convertWrapper.setPadding(new Insets(17, 20, 7, 0));
        convertWrapper.setRight(convertButton);

        // adding all nodes/components together
        VBox mainContainer = new VBox(10, excelFile, options, convertWrapper);
        mainContainer.setPadding(new Insets(30, 20, 20, 20));

        menuBar.setDisableSettingsMenu(false);
        menuBar.setDisableTemplateMenu(false);

        return mainContainer;
    }

    /**
     * Generates a scene where the conversion is running/in progress.
     */
    public void convertRunningScene() {
        _runningProgressScene("Datei wird konvertiert...", true);
    }

    /**
     * Generates a scene where the conversion is finished.
     */
    public void convertFinishScene() {
        Button errorAnalysisButton = new Button("Fehleranalyse");
        errorAnalysisButton.setOnAction(event -> analysisFacade.startAnalysis());

        saveButton = new Button("Speichern...");
        saveButton.setGraphic(_getFolderImageView(15));
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(event -> convertFacade.saveIfReady());

        Button convertNewFileButton = new Button("Neue Datei konvertieren");
        convertNewFileButton.setStyle("-fx-background-color: #b8d593; -fx-text-fill: black");
        convertNewFileButton.setOnAction(event -> homeScene());

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

    /**
     * Generates a scene where the analysis is running/in progress.
     */
    public void analysisRunningScene() {
        _runningProgressScene("Datei wird überprüft...", false);
    }

    /**
     * Generates the scene where the analysis is finished
     */
    public void analysisFinishScene() {
        Button cancelButton = new Button("Zurück");
        cancelButton.setDefaultButton(true);
        cancelButton.setOnAction(event -> homeScene());

        BorderPane cancelWrapper = new BorderPane();
        cancelWrapper.setPadding(new Insets(10, 0, 0, 0));
        cancelWrapper.setRight(cancelButton);

        progressContainer.setButtonWrapper(cancelWrapper);
        progressContainer.setLabelText("Datei vollständig überprüft");

        rootPane.setCenter(progressContainer);

        menuBar.setDisableSettingsMenu(false);
        menuBar.setDisableTemplateMenu(false);
    }

    /**
     * Generates a scene where a process is running.
     *
     * @param labelText of the scene
     * @param isConverting if the current process is conversion
     */
    private void _runningProgressScene(String labelText, boolean isConverting) {
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setStyle("-fx-background-color: #f1c0a2; -fx-text-fill: black");
        cancelButton.setOnAction(event -> {
            if (isConverting) {
                convertFacade.cancelConversion();
            } else {
                analysisFacade.cancelAnalysis();
            }
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

    /**
     * Correctly centers the stage on screen.
     */
    public void centerStageOnScreen() {
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

    /**
     * Positiones the given Dialog relative to the stage by an offset of 50 on both x and y.
     *
     * @param dialog to position
     */
    protected static void positionDialogRelativeToStage(Dialog<?> dialog) {
        dialog.setX(stage.getX() + 50);
        dialog.setY(stage.getY() + 50);
    }

    /**
     * Sets a given String representing a path to the saveToPath text filed
     *
     * @param path String to set.
     */
    public void setSaveToPathTextFieldText(String path) {
        saveToPath.setText(path);
    }

    /**
     * Sets a given String representing a path to the pathToFile text filed
     *
     * @param path String to set.
     */
    public void setPathOfFileToConvert(String path) {
        pathToFile.setText(path);
    }

    /**
     * Applies a given JMetroStyle
     *
     * @param jMetroStyle to apply
     * @see Style
     */
    private void applyJMetroTheme(Style jMetroStyle) {
        jMetro.setStyle(jMetroStyle);
    }

    /**
     * Applies the dark theme.
     */
    public void applyDarkTheme() {
        applyJMetroTheme(Style.DARK);
        if (saveToPathButton != null) pathToFileButton.setGraphic(getWhiteFolderImageView());
        if (saveToPathButton != null) saveToPathButton.setGraphic(getWhiteFolderImageView());
        if (saveButton != null) saveButton.setGraphic(_getWhiteFolderImageView(15));
    }

    /**
     * Applies the light theme.
     */
    public void applyLightTheme() {
        applyJMetroTheme(Style.LIGHT);
        if (saveToPathButton != null) pathToFileButton.setGraphic(getFolderImageView());
        if (saveToPathButton != null) saveToPathButton.setGraphic(getFolderImageView());
        if (saveButton != null) saveButton.setGraphic(_getFolderImageView(15));
    }

    /**
     * Applies the light or dark theme depending on what the system theme is.
     */
    public void applySystemTheme() {
        if (OsThemeDetector.getDetector().isDark()) {
            applyDarkTheme();
        } else {
            applyLightTheme();
        }
    }

    /**
     * Creates Creates a ImageView of a black folder icon/image.
     * ImageView has the height and width of 20
     *
     * @return ImageView with a black folder icon/image
     */
    private ImageView getFolderImageView() {
        return _getFolderImageView(20);
    }

    /**
     * Creates Creates a ImageView of a white folder icon/image.
     * ImageView has the height and width of 20
     *
     * @return ImageView with a white folder icon/image
     */
    private ImageView getWhiteFolderImageView() {
        return _getWhiteFolderImageView(20);
    }

    /**
     * Creates Creates a ImageView of a black folder icon/image.
     *
     * @param fitHeightAndWidth of the ImageView
     * @return ImageView with a black folder icon/image
     */
    private ImageView _getFolderImageView(double fitHeightAndWidth) {
        return _getImageView("folder", fitHeightAndWidth);
    }

    /**
     * Creates Creates a ImageView of a white folder icon/image.
     *
     * @param fitHeightAndWidth of the ImageView
     * @return ImageView with a white folder icon/image
     */
    private ImageView _getWhiteFolderImageView(double fitHeightAndWidth) {
        return _getImageView("folder_w", fitHeightAndWidth);
    }

    /**
     * Creates a ImageView of a folder icon/image, given by its file name.
     *
     * @param fileName of the folder icon/image
     * @param fitHeightAndWidth of the ImageView
     * @return ImageView with a folder icon/image
     */
    private ImageView _getImageView(String fileName, double fitHeightAndWidth) {
        ImageView folderImageView = new ImageView(new Image(Objects.requireNonNull(
                XL2mQB.class.getResourceAsStream("/ch/ksh/xl2mqb/images/" + fileName + ".png")
        )));
        folderImageView.setFitHeight(fitHeightAndWidth);
        folderImageView.setFitWidth(fitHeightAndWidth);
        return folderImageView;
    }

    /**
     * Gets the progress container.
     *
     * @return the progress container
     * @see ProgressContainer
     */
    public ProgressContainer getProgressContainer() {
        return progressContainer;
    }

    /**
     * Gets the menu bar.
     *
     * @return the menu bar
     * @see MenuBar
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Gets the stage.
     *
     * @return the gui stage
     */
    public static Stage getStage() {
        if (stage == null) {
            throw new IllegalStateException("No stage available");
        }
        return stage;
    }

    /**
     * Gets the current JMetroStyle.
     *
     * @return current JMetroStyle
     * @see Style
     */
    public static Style getCurrentJMetroStyle() {
        return jMetro.getStyle();
    }
}
