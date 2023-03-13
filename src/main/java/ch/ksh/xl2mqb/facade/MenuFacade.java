package ch.ksh.xl2mqb.facade;

public class MenuFacade {
    private static MenuFacade INSTANCE;

    public void openNewExcelFileFromTemplate(){
        throw new UnsupportedOperationException();
    }

    public void openNewExcelFileFromTemplate(String dirPath) {
        throw new UnsupportedOperationException();
    }

    public void arrangeExcelTemplate() {
        throw new UnsupportedOperationException();
    }

    public void saveExcelTemplateToPath(String dirPath) {
        throw new UnsupportedOperationException();
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
