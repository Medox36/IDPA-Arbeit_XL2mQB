package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.XL2mQB;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileFacade {
    private static FileFacade INSTANCE;
    private File excelFile;

    public String selectExcelFile(Stage parent) {
        excelFile = fileChooserOpenDialog("Datei Auswahl", new FileChooser.ExtensionFilter("Excel-Datei (*.xlsx/*.xlsm)","*.xlsx", "*.xlsm"));

        if (excelFile == null) {
            AlertUtil.warningAlert("No file selected error", "No File .xlsx or .xlsm file was selected",
                    "You did not select a file or the right type of file. \n Pleas try again and select an .xlsx or an .xlsm");

            return "";
        }

        return excelFile.getPath();
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
        File file = fileChooserOpenDialog("Datei speichern in", new FileChooser.ExtensionFilter("XML-Datei (.xml)", ".xml"));
        if(file == null) {
            return null;
        }
        Path xmlPath = file.toPath();
        if (Files.notExists(xmlPath)) {
            try {
                Files.createFile(xmlPath);
            } catch (IOException e) {
                e.printStackTrace();
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

    public File directoryChooserDialog(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(Path.of(System.getProperty("user.home"), "Documents").toFile());
        return directoryChooser.showDialog(XL2mQB.getStage());
    }

    public static FileFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileFacade();
        }
        return INSTANCE;
    }
}
