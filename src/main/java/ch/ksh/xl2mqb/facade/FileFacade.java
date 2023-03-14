package ch.ksh.xl2mqb.facade;

import java.io.File;

public class FileFacade {
    private static FileFacade INSTANCE;

    public void readFile(File file) {
        throw new UnsupportedOperationException();
    }

    public void saveTo(File file) {
        throw new UnsupportedOperationException();
    }

    public static FileFacade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileFacade();
        }
        return INSTANCE;
    }
}
