package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.InfoDialog;
import ch.ksh.xl2mqb.settings.Settings;
import ch.ksh.xl2mqb.settings.ExtendedStyle;

import javafx.scene.control.ButtonType;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

public class MenuFacade {
    private static MenuFacade INSTANCE;
    private final Path excelTemplatePath = Path.of("Moodle-Question-Converter(Xl2mQB)_Vorlage.xltm");
    private static final FileFacade fileFacade = FileFacade.getInstance();
    private final Settings settings = Settings.getInstance();

    public void newExcelFileFromTemplate() {
        try {
            Desktop.getDesktop().open(excelTemplatePath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void arrangeExcelTemplate() {
        try {
            Path templateFolder = fileFacade.getDocumentsFolderPath().resolve("Benutzerdefinierte Office-Vorlagen");

            if (Files.exists(templateFolder.resolve("Moodle-Question-Converter(Xl2mQB)_Vorlage.xltm"))) {
                Optional<ButtonType> buttonType = AlertUtil.confirmAlert("Excel-Vorlage einrichten", "Excel-Vorlage bereits eingerichtet", "Möchten Sie die Excel-Vorlage überschreiben?");
                if (buttonType.isPresent()) {
                    if (buttonType.get().equals(ButtonType.YES)) {
                        saveExcelTemplateToPathAndOverride(templateFolder.toString());
                    }
                }
            } else {
                saveExcelTemplateToPath(templateFolder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveExcelTemplateToPath(String dirPath) throws IOException {
        Files.copy(excelTemplatePath, Path.of(dirPath).resolve(excelTemplatePath.getFileName()));
    }

    private void saveExcelTemplateToPathAndOverride(String dirPath) throws IOException {
        Files.copy(excelTemplatePath, Path.of(dirPath).resolve(excelTemplatePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }

    public void selectPathToSaveExcelTemplateTo() {
        try {
            File dir = fileFacade.directoryChooserDialog("Excel-Vorlage speichern unter");

            if (Files.exists(Path.of(dir.getPath(), "Moodle-Question-Converter(Xl2mQB)_Vorlage.xltm"))) {
                Optional<ButtonType> buttonType = AlertUtil.confirmAlert("Excel-Vorlage einrichten", "Excel-Vorlage existiert bereits in diesem Ordner", "Möchten Sie die Excel-Vorlage überschreiben?");
                if (buttonType.isPresent()) {
                    if (buttonType.get().equals(ButtonType.YES)) {
                        saveExcelTemplateToPathAndOverride(dir.getPath());
                    }
                }
            } else {
                saveExcelTemplateToPath(dir.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectPAthToSaveXMLFilesTo() {
        File pathToSaveTo = fileFacade.directoryChooserDialog("Standarmässig speichern unter");
        settings.setSetting("defaultSavePath", pathToSaveTo.toPath());
    }

    public void resetSettings() {
        settings.resetSettings();
    }

    public void addDesktopShortcut() {
        try {
            String[] command = new String[]{"wscript", Objects.requireNonNull(MenuFacade.class.getResource("/ch/ksh/xl2mqb/scripts/desktopShortcut.vbs")).toString()};
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConversionErrors(boolean showErrors) {
        settings.setSetting("showErrors", showErrors);
    }

    public ExtendedStyle getStyle() {
        return  (ExtendedStyle) Settings.getInstance().getSetting("style");
    }

    public void setStyle(ExtendedStyle extendedStyle) {
        settings.setSetting("style", extendedStyle);
    }

    public boolean areConversionErrorsShown() {
        Object settingValue = settings.getSetting("showErrors");

        if (settingValue == null) {
            return false;
        } else {
            return (boolean) settingValue;
        }
    }

    public void openInstructions() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Medox36/IDPA-Arbeit_XL2mQB/wiki/Instruktionen"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void openAttributions() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Medox36/IDPA-Arbeit_XL2mQB/wiki/Verwendete-Bibliotheken"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void showInfoDialog() {
        new InfoDialog().showAndWait();
    }

    public static MenuFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MenuFacade();
        }
        return INSTANCE;
    }
}
