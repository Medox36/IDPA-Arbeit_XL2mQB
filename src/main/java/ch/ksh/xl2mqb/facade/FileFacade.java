package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.XL2mQB;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileFacade {
    private static FileFacade INSTANCE;
    private File excelFile;
    private File saveDir;
    private XL2mQB gui;

    public void setGUI(XL2mQB gui) {
        this.gui = gui;
    }

    public void selectExcelFile() {
        excelFile = fileChooserOpenDialog("Datei Auswahl", new FileChooser.ExtensionFilter("Excel-Datei (*.xlsx/*.xlsm)","*.xlsx", "*.xlsm"));

        if (excelFile == null) {
            AlertUtil.warningAlert("Keine Datei ausgewählt", "Keine Datie mit der Endung .xlsx or .xlsm wurde ausgewählt.",
                    "Sie haben eine Datei oder den richtigen Dateityp nicht ausgewählt.\nBitte versuchen Sie es erneut und wählen Sie eine .xlsx oder eine .xlsm Datei.");
        } else {
            gui.setPathOfFileToConvert(excelFile.getPath());
        }
    }

    public void selectSaveDirectory() {
        saveDir = directoryChooserDialog("Speicherort auswählen");

        if (saveDir == null) {
            AlertUtil.warningAlert("Kein Ordner ausgewählt", "Es wurde kein Ordner ausgewählt.",
                    "Sie haben keinen Ordner ausgewählt.\nBitter versuchen Sie es noch einmal");
        } else {
            gui.setSaveToPathTextFieldText(saveDir.getPath());
        }
    }

    public ExcelHandler readFile() {
        return new ExcelHandler(excelFile);
    }

    public void saveXML(String xml) {
        writeXMLTo(makeNewXML(), xml);
    }

    private void writeXMLTo(Path path, String xml) {
        if (path != null) {
            try {
                Files.write(path, xml.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
                if (file.getParentFile().mkdirs()) {
                    Files.createFile(xmlPath);
                } else {
                    AlertUtil.errorAlert("Fehler beim Speichern", "Es gab einen Fehler beim speichern der Datei.",
                            "Die ausgewählten Ordner konnten nicht erstellt werden.\nBitter versuchen Si es erneut oder speichern Sie in einem anderen Ordner.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtil.errorAlert("Fehler beim Speichern", "Es gab einen Fehler beim speichern der Datei.",
                        "Das Speichern der Datei war nicht erfolgreich.\nBitte versuchen Sie es erneut oder versuchen Sie die Datei an einem anderen Ort abzuspeichern.");
            }
        }
        return xmlPath;
    }

    public File fileChooserOpenDialog(String title, FileChooser.ExtensionFilter extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(Path.of(System.getProperty("user.home"), "Documents").toFile());
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showOpenDialog(XL2mQB.getStage());
    }

    public File fileChooserStandardSaveDialog(String title, FileChooser.ExtensionFilter extensionFilter) {
        return fileChooserSaveDialog(title, extensionFilter, Path.of(System.getProperty("user.home"), "Documents").toFile());
    }

    public File fileChooserSaveDialog(String title, FileChooser.ExtensionFilter extensionFilter, File initialDirectory) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showSaveDialog(XL2mQB.getStage());
    }

    public File directoryChooserDialog(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(Path.of(System.getProperty("user.home"), "Documents").toFile());
        return directoryChooser.showDialog(XL2mQB.getStage());
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public void setSaveDir(File saveDir) {
        this.saveDir = saveDir;
    }

    public static FileFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileFacade();
        }
        return INSTANCE;
    }
}
