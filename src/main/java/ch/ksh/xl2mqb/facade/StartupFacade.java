package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.args.ArgsReader;
import ch.ksh.xl2mqb.gui.XL2mQB;
import ch.ksh.xl2mqb.log.TextAreaAppender;
import ch.ksh.xl2mqb.settings.ExtendedStyle;
import ch.ksh.xl2mqb.settings.Settings;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

/**
 * Facade for all tasks which need to be executed at start
 *
 * @author Lorenzo giuntini
 * @version 1.0
 */
public class StartupFacade {
    private final XL2mQB gui;

    public StartupFacade(XL2mQB gui) {
        this.gui = gui;
    }

    /**
     * Method to run all task of the facade
     */
    public void onStartup() {
        applySettingsToGUI();
        initChangListeners();
        readArgumentsIfAvailable();
        setGUIReferenceToFacades();
        initLogger();
    }

    /**
     * Applies all relevant setting to the gui.
     * Which includes: x and y position of stage, the default save path, theme/style of stage and show conversion errors.
     */
    private void applySettingsToGUI() {
        Settings settings = Settings.getInstance();

        if ((double) settings.getSetting("posY") == -1.0 && (double) settings.getSetting("posX") == -1.0) {
            gui.centerStageOnScreen();
        } else {
            XL2mQB.getStage().setX((double) settings.getSetting("posX"));
            XL2mQB.getStage().setY((double) settings.getSetting("posY"));
        }

        String defaultSavePath = ((Path) settings.getSetting("defaultSavePath")).toString();

        if (!defaultSavePath.isBlank()) {
            FileFacade.getInstance().setSaveDir(new File(defaultSavePath));
            gui.setSaveToPathTextFieldText(defaultSavePath);
        }

        Platform.runLater(() -> {
            switch ((ExtendedStyle) settings.getSetting("style")) {
                case DARK -> gui.applyDarkTheme();
                case LIGHT -> gui.applyLightTheme();
                case SYSTEM -> gui.applySystemTheme();
            }
        });

        gui.getMenuBar().setStates(
                (ExtendedStyle) settings.getSetting("style"),
                (boolean) settings.getSetting("showErrors")
        );
    }

    /**
     * Initializes all relevant change listeners to keep settings updated.
     * Which includes, x and y position of stage, the default save path and theme/style of stage.
     * Note that the ChangeListener for the setting show conversion errors it initialized in the MenuBar class.
     * @see javafx.beans.value.ChangeListener
     */
    private void initChangListeners() {
        Settings settings = Settings.getInstance();
        settings.getSettingProperty("style").addListener((observable, oldValue, newValue) -> {
            ExtendedStyle extendedStyle = (ExtendedStyle) newValue;

            switch (extendedStyle) {
                case DARK -> gui.applyDarkTheme();
                case LIGHT -> gui.applyLightTheme();
                case SYSTEM -> gui.applySystemTheme();
            }
        });
        settings.getSettingProperty("defaultSavePath").addListener((observable, oldValue, newValue) -> {
            Path pathToSaveTo = (Path) newValue;
            gui.setSaveToPathTextFieldText(pathToSaveTo.toString());
        });

        settings.getSettingProperty("defaultSavePath").addListener(
                (observable, oldValue, newValue) -> gui.setSaveToPathTextFieldText(((Path) newValue).toString())
        );

        Stage stage = XL2mQB.getStage();
        stage.xProperty().addListener((observable, oldValue, newValue) -> settings.setSetting("posX", newValue));
        stage.yProperty().addListener((observable, oldValue, newValue) -> settings.setSetting("posY", newValue));
        // ensure first changes get saved
        settings.setSetting("posX", stage.getX());
        settings.setSetting("posY", stage.getY());
    }

    /**
     * Reads the available arguments and sets their values to the gui if needed.
     */
    private void readArgumentsIfAvailable() {
        String path = new ArgsReader(gui.getParameters().getRaw()).getFileNameAndPathFromArgs();

        if (!path.isEmpty()) {
            FileFacade.getInstance().setExcelFile(new File(path));
            gui.setPathOfFileToConvert(path);
        }
    }

    /**
     * Sets the reference to the gui instance to all facade that need the reference.
     */
    private void setGUIReferenceToFacades() {
        ConvertFacade.getInstance().setGUI(gui);
        AnalysisFacade.getInstance().setGUI(gui);
        FileFacade.getInstance().setGUI(gui);
    }

    /**
     * Sets the reference of the progress container to the logger class.
     */
    private void initLogger() {
        TextAreaAppender.setProgressContainer(gui.getProgressContainer());
    }
}
