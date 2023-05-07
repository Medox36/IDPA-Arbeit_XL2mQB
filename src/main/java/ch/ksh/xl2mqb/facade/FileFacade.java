package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.XL2mQB;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *  Facade to get and save the excel file.
 *
 * @author Isuf Hasani, Lorenzo Giuntini, Niklas Vogel
 * @date 05.05.2023
 * @version 1.0
 *
 */
public class FileFacade {
    private static FileFacade INSTANCE;
    private final FolderFinder folderFinder = new FolderFinder();
    private final Path documentsFolderPath = folderFinder.getUserDocumentsFolder();
    private final Path desktopFolderPath = folderFinder.getUserDesktopFolder();
    private File excelFile;
    private File saveDir;
    private XL2mQB gui;

    /**
     * sets a gui
     *
     * @param gui
     */
    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    /**
     * get an excel file
     */
    public void selectExcelFile() {
        excelFile = fileChooserOpenDialog("Datei Auswahl", new FileChooser.ExtensionFilter("Excel-Datei (*.xlsx/*.xlsm)","*.xlsx", "*.xlsm"));

        if (excelFile == null) {
            AlertUtil.warningAlert("Keine Datei ausgewählt", "Keine Datie mit der Endung .xlsx or .xlsm wurde ausgewählt.",
                    "Sie haben eine Datei oder den richtigen Dateityp nicht ausgewählt.\nBitte versuchen Sie es erneut und wählen Sie eine .xlsx oder eine .xlsm Datei.");
        } else {
            gui.setPathOfFileToConvert(excelFile.getPath());
        }
    }

    /**
     * gets a directory path
     */
    public void selectSaveDirectory() {
        saveDir = directoryChooserDialog("Speicherort auswählen");

        if (saveDir == null) {
            AlertUtil.warningAlert("Kein Ordner ausgewählt", "Es wurde kein Ordner ausgewählt.",
                    "Sie haben keinen Ordner ausgewählt.\nBitter versuchen Sie es noch einmal");
        } else {
            gui.setSaveToPathTextFieldText(saveDir.getPath());
        }
    }

    /**
     * reads an excel file
     *
     * @return ExcelHandler
     */
    public ExcelHandler readFile() {
        return new ExcelHandler(excelFile);
    }

    /**
     * saves xml string
     *
     * @param xml converted xml string
     */
    public void saveXML(String xml) {
        writeXMLTo(makeNewXML(), xml);
    }

    /**
     * wirtes xml into file
     *
     * @param path
     * @param xml
     */
    private void writeXMLTo(Path path, String xml) {
        if (path != null) {
            try {
                Files.write(path, xml.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * creates a new file
     *
     * @return file path
     */
    private Path makeNewXML() {
        File file;
        if (saveDir == null) {
            file = fileChooserStandardSaveDialog("Datei speichern in", new FileChooser.ExtensionFilter("XML-Datei (.xml)", ".xml"));
        } else {
            file = fileChooserSaveDialog("Datei speichern in", new FileChooser.ExtensionFilter("XML-Datei (.xml)", ".xml"), saveDir);
        }
        if (file == null) {
            return null;
        }

        Path xmlPath = file.toPath();
        if (Files.notExists(xmlPath)) {
            try {
                if (Files.notExists(file.getParentFile().toPath())) {
                    if (!file.getParentFile().mkdirs()) {
                        AlertUtil.errorAlert("Fehler beim Speichern", "Es gab einen Fehler beim speichern der Datei.",
                                "Die ausgewählten Ordner konnten nicht erstellt werden.\nBitter versuchen Sie es erneut oder speichern Sie in einem anderen Ordner.");
                    }
                }
                Files.createFile(xmlPath);
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtil.errorAlert("Fehler beim Speichern", "Es gab einen Fehler beim speichern der Datei.",
                        "Das Speichern der Datei war nicht erfolgreich.\nBitte versuchen Sie es erneut oder versuchen Sie die Datei an einem anderen Ort abzuspeichern.");
            }
        }
        return xmlPath;
    }

    /**
     * file chooser for opening
     *
     * @param title
     * @param extensionFilter
     * @return file
     */
    public File fileChooserOpenDialog(String title, FileChooser.ExtensionFilter extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(documentsFolderPath.toFile());
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showOpenDialog(XL2mQB.getStage());
    }

    /**
     * file chooser for saving without initial Directory
     *
     * @param title
     * @param extensionFilter
     * @return file
     */
    public File fileChooserStandardSaveDialog(String title, FileChooser.ExtensionFilter extensionFilter) {
        return fileChooserSaveDialog(title, extensionFilter, documentsFolderPath.toFile());
    }

    /**
     * file chooser for saving with initial Directory
     *
     * @param title
     * @param extensionFilter
     * @param initialDirectory
     * @return file
     */
    public File fileChooserSaveDialog(String title, FileChooser.ExtensionFilter extensionFilter, File initialDirectory) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showSaveDialog(XL2mQB.getStage());
    }

    /**
     * file chooser for directorys
     *
     * @param title
     * @return file
     */
    public File directoryChooserDialog(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(documentsFolderPath.toFile());
        return directoryChooser.showDialog(XL2mQB.getStage());
    }

    public Path getDocumentsFolderPath() {
        return documentsFolderPath;
    }

    public Path getDesktopFolderPath() {
        return desktopFolderPath;
    }

    /**
     * sets an excel file
     *
     * @param excelFile
     */
    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    /**
     * sets a directory
     *
     * @param saveDir
     */
    public void setSaveDir(File saveDir) {
        this.saveDir = saveDir;
    }

    /**
     * crates an instance of the FileFacade
     *
     * @return an instance of the Facade
     */
    public static FileFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileFacade();
        }
        return INSTANCE;
    }

    private static final class FolderFinder {

        public Path getUserDocumentsFolder() {
            String value = Advapi32Util.registryGetStringValue(
                    WinReg.HKEY_CURRENT_USER,
                    "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders\\",
                    "Personal"
            );

            return Path.of(replaceVars(value));
        }

        public Path getUserDesktopFolder() {
            String value = Advapi32Util.registryGetStringValue(
                    WinReg.HKEY_CURRENT_USER,
                    "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders\\",
                    "Desktop"
            );

            return Path.of(replaceVars(value));
        }

        private String replaceVars(String path) {
            return path.replace("%USERPROFILE%", System.getenv("USERPROFILE"));
        }
    }
}
