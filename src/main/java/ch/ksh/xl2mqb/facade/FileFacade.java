package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.excel.ExcelHandler;
import ch.ksh.xl2mqb.gui.AlertUtil;
import ch.ksh.xl2mqb.gui.XL2mQB;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

public class FileFacade {
    private static FileFacade INSTANCE;
    private final FolderFinder folderFinder = new FolderFinder();
    private final Path documentsFolderPath = folderFinder.getUserDocumentsFolder();
    private final Path desktopFolderPath = folderFinder.getUserDesktopFolder();
    private File excelFile;

    public String selectExcelFile() {
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

    public void saveTo(File file) {
        throw new UnsupportedOperationException();
    }

    public File fileChooserOpenDialog(String title, FileChooser.ExtensionFilter extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(documentsFolderPath.toFile());
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showOpenDialog(XL2mQB.getStage());
    }

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
