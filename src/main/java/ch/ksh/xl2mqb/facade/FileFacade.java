package ch.ksh.xl2mqb.facade;

import ch.ksh.xl2mqb.gui.XL2mQB;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

public class FileFacade {
    private static FileFacade INSTANCE;

    public void readFile(File file) {
        throw new UnsupportedOperationException();
    }

    public void saveTo(File file) {
        throw new UnsupportedOperationException();
    }

    public File fileChooserOpenDialog(String title, FileChooser.ExtensionFilter extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(Path.of(System.getProperty("user.home")).toFile());
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showOpenDialog(XL2mQB.getStage());
    }

    public File directoryChooserDialog(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(Path.of(System.getProperty("user.home")).toFile());
        return directoryChooser.showDialog(XL2mQB.getStage());
    }

    public static FileFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileFacade();
        }
        return INSTANCE;
    }
}
