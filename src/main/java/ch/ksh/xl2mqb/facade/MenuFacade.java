package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.gui.AlertUtil;

import javafx.scene.control.ButtonType;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class MenuFacade {
    private static MenuFacade INSTANCE;
    private final Path excelTemplatePath = Path.of("Moodle-Question-Converter(Xl2mQB)_Vorlage.xltm");

    public void openNewExcelFileFromTemplate(){
        throw new UnsupportedOperationException();
    }

    public void openNewExcelFileFromTemplate(String dirPath) {
        throw new UnsupportedOperationException();
    }

    public void newExcelFileFromTemplate() throws IOException {
        Desktop.getDesktop().open(excelTemplatePath.toFile());
    }
    public void arrangeExcelTemplate() throws IOException {
        String templateFolder = Path.of(System.getProperty("user.home"), "Dokumente", "Benutzerdefinierte Office-Vorlagen").toString();

        if (Files.exists(Path.of(templateFolder, "Moodle-Question-Converter(Xl2mQB)_Vorlage.xltm"))) {
            Optional<ButtonType> buttonType = AlertUtil.confirmAlert("Excel-Vorlage einrichten", "Excel-Vorlage bereits eingerichtet", "Möchten Sie die Excel-Vorlage überschreiben?");
            if (buttonType.isPresent()) {
                if (buttonType.get().equals(ButtonType.YES)) {
                    saveExcelTemplateToPathAndOverride(templateFolder);
                }
            }
        } else {
            saveExcelTemplateToPath(templateFolder);
        }
    }

    private void saveExcelTemplateToPath(String dirPath) throws IOException {
        Files.copy(excelTemplatePath, Path.of(dirPath).resolve(excelTemplatePath.getFileName()));
    }

    private void saveExcelTemplateToPathAndOverride(String dirPath) throws IOException {
        Files.copy(excelTemplatePath, Path.of(dirPath).resolve(excelTemplatePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }

    public void selectPathToSaveExcelTemplateTo() throws IOException {
        File dir = FileFacade.getInstance().directoryChooserDialog("Excel-Vorlage speichern unter");
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
    }

    public void resetSettings() {
        throw new UnsupportedOperationException();
    }

    public void addDesktopShortcut() {
        throw new UnsupportedOperationException();
    }

    public void showConversionErrors(boolean showErrors) {
        throw new UnsupportedOperationException();
    }

    public void openInstructions() {
        throw new UnsupportedOperationException();
    }

    public static MenuFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MenuFacade();
        }
        return INSTANCE;
    }
}
