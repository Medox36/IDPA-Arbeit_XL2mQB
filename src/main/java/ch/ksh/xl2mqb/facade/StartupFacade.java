package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.args.ArgsReader;
import ch.ksh.xl2mqb.gui.XL2mQB;
import ch.ksh.xl2mqb.log.TextAreaAppender;
import ch.ksh.xl2mqb.settings.ExtendedStyle;
import ch.ksh.xl2mqb.settings.Settings;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.nio.file.Path;

public class StartupFacade {
    private final XL2mQB gui;

    public StartupFacade(XL2mQB gui) {
        this.gui = gui;
    }

    public void onStartup() {
        applySettingsToGUI();
        initChangListeners();
        readArgumentsIfAvailable();
        setGUIReferenceToFacades();
        initLogger();
    }

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
            gui.setSaveToPathTextFieldText(defaultSavePath);
        }

        Platform.runLater(() -> {
            switch ((ExtendedStyle) settings.getSetting("style")) {
                case DARK -> gui.applyDarkTheme();
                case LIGHT -> gui.applyLightTheme();
                case SYSTEM -> gui.applySystemTheme();
            }
        });

        gui.getMenuBar().setStates((ExtendedStyle) settings.getSetting("style"), (boolean) settings.getSetting("showErrors"));
    }

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

        Stage stage = XL2mQB.getStage();
        stage.xProperty().addListener((observable, oldValue, newValue) -> settings.setSetting("posX", newValue));
        stage.yProperty().addListener((observable, oldValue, newValue) -> settings.setSetting("posY", newValue));
    }

    private void readArgumentsIfAvailable() {
        String path = new ArgsReader(gui.getParameters().getRaw()).getFileNameAndPathFromArgs();

        if (!path.isEmpty()) {
            gui.setPathOfFileToConvert(path);
        }
    }

    private void setGUIReferenceToFacades() {
        ConvertFacade.getInstance().setGUI(gui);
        AnalysisFacade.getInstance().setGUI(gui);
    }

    private void initLogger() {
        TextAreaAppender.setProgressContainer(gui.getProgressContainer());
    }
}
