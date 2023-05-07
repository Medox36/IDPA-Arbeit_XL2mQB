package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.InfoDialog;
import ch.ksh.xl2mqb.settings.ExtendedStyle;
import ch.ksh.xl2mqb.settings.Settings;

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

/**
 * Menu related facade
 *
 * @author Lorenzo Giuntini
 */
public class MenuFacade {
    private static MenuFacade INSTANCE;
    private final Path excelTemplatePath = Path.of("Moodle-Question-Converter(XL2mQB)_Vorlage.xltm");
    private static final FileFacade fileFacade = FileFacade.getInstance();
    private final Settings settings = Settings.getInstance();

    public void newExcelFileFromTemplate() {
        try {
            Desktop.getDesktop().open(excelTemplatePath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Arranges the Excel Template in such a way that the Excel-Program should automatically recognise the Excel template.
     * This method just copies the Excel template to the "Benutzerdefinierte Office-Vorlagen" folder fo the user.
     */
    public void arrangeExcelTemplate() {
        try {
            Path templateFolder = fileFacade.getDocumentsFolderPath().resolve("Benutzerdefinierte Office-Vorlagen");

            if (Files.exists(templateFolder.resolve("Moodle-Question-Converter(Xl2mQB)_Vorlage.xltm"))) {
                Optional<ButtonType> buttonType = AlertUtil.confirmAlert(
                        "Excel-Vorlage einrichten",
                        "Excel-Vorlage bereits eingerichtet",
                        "Möchten Sie die Excel-Vorlage überschreiben?"
                );
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

    /**
     * Saves the Excel Template to a folder the user can select
     *
     * @param dirPath directory path to save the Excel template to
     * @throws IOException if an I/O error occurs
     */
    private void saveExcelTemplateToPath(String dirPath) throws IOException {
        Path result = Files.copy(excelTemplatePath, Path.of(dirPath).resolve(excelTemplatePath.getFileName()));
        showUserStateOfExcelTemplate(result);
    }

    /**
     * Saves the Excel Template to a folder the user can select and overrides the file if it already exists.
     *
     * @param dirPath directory path to save the Excel template to
     * @throws IOException if an I/O error occurs
     */
    private void saveExcelTemplateToPathAndOverride(String dirPath) throws IOException {
        Path result = Files.copy(
                excelTemplatePath,
                Path.of(dirPath).resolve(excelTemplatePath.getFileName()),
                StandardCopyOption.REPLACE_EXISTING
        );
        showUserStateOfExcelTemplate(result);
    }

    /**
     * Shows the user the state after arranging the Excel template
     *
     * @param excelTemplatePath path to the newly arranged Excel template
     */
    private void showUserStateOfExcelTemplate(Path excelTemplatePath) {
        if (Files.exists(excelTemplatePath)) {
            AlertUtil.infoAlert(
                    "Excel-Vorlage einrichten",
                    "Excel-Vorlage erfolgreich eingerichtet.",
                    ""
            );
        } else {
            AlertUtil.warningAlert(
                    "Excel-Vorlage einrichten",
                    "Das Einrichten der Excel-Vorlage war nicht erfolgreich.",
                    "Bitte versuchen Sie es erneut oder wählen Sie im Menu Excel-Vorlage > " +
                    "Neue Excel-Datei von Vorlage."
            );
        }
    }

    /**
     * Saves the Excel template to a directory the user can select.
     */
    public void selectPathToSaveExcelTemplateTo() {
        try {
            File dir = fileFacade.directoryChooserDialog("Excel-Vorlage speichern unter");

            if (dir == null) {
                return;
            }

            if (Files.exists(Path.of(dir.getPath(), "Moodle-Question-Converter(Xl2mQB)_Vorlage.xltm"))) {
                Optional<ButtonType> buttonType = AlertUtil.confirmAlert(
                        "Excel-Vorlage einrichten",
                        "Excel-Vorlage existiert bereits in diesem Ordner",
                        "Möchten Sie die Excel-Vorlage überschreiben?"
                );
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

    /**
     * Lets the user select the default save path of converted xml files
     */
    public void selectPathToSaveXMLFilesTo() {
        File pathToSaveTo = fileFacade.directoryChooserDialog("Standarmässig speichern unter");
        settings.setSetting("defaultSavePath", pathToSaveTo.toPath());
    }

    /**
     * Resets the settings to their default value.
     */
    public void resetSettings() {
        settings.resetSettings();
    }

    /**
     * Adds a desktop shortcut for the application via a vbs script.
     */
    public void addDesktopShortcut() {
        try {
            String[] command = new String[]{
                    "wscript",
                    Objects.requireNonNull(
                            MenuFacade.class.getResource("/ch/ksh/xl2mqb/scripts/desktopShortcut.vbs")
                    ).toString()
            };
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the settings value for showing conversion errors.
     *
     * @param showErrors value to set
     */
    public void showConversionErrors(boolean showErrors) {
        settings.setSetting("showErrors", showErrors);
    }

    /**
     * Gets the settings value for showing conversion errors.
     *
     * @return setting value of type boolean
     */
    public boolean areConversionErrorsShown() {
        Object settingValue = settings.getSetting("showErrors");

        if (settingValue == null) {
            return false;
        } else {
            return (boolean) settingValue;
        }
    }

    /**
     * Gets the style setting.
     *
     * @return Currently active style.
     * @see ExtendedStyle
     */
    public ExtendedStyle getStyle() {
        return (ExtendedStyle) Settings.getInstance().getSetting("style");
    }

    /**
     * Sets the style setting
     *
     * @param extendedStyle to set
     * @see ExtendedStyle
     */
    public void setStyle(ExtendedStyle extendedStyle) {
        settings.setSetting("style", extendedStyle);
    }

    /**
     * Opens a URL in the Browser pointing to the instructions wiki page on GitHub
     */
    public void openInstructions() {
        try {
            Desktop.getDesktop().browse(
                    new URI("https://github.com/Medox36/IDPA-Arbeit_XL2mQB/wiki/Instruktionen")
            );
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a URL in the Browser pointing to the used libraries wiki page on GitHub
     */
    public void openAttributions() {
        try {
            Desktop.getDesktop().browse(
                    new URI("https://github.com/Medox36/IDPA-Arbeit_XL2mQB/wiki/Verwendete-Bibliotheken")
            );
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the info dialog to the user.
     */
    public void showInfoDialog() {
        new InfoDialog().showAndWait();
    }

    /**
     * Creates an instance of the MenuFacade if not already done.
     * @return an instance of the Facade
     */
    public static MenuFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MenuFacade();
        }
        return INSTANCE;
    }
}
