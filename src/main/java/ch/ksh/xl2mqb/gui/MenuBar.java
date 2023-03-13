package ch.ksh.xl2mqb.gui;

import javafx.scene.control.Menu;

import java.util.function.Consumer;

public class MenuBar extends javafx.scene.control.MenuBar {

    private final Menu settingsMenu;
    private final Menu templateMenu;
    private final XL2mQB gui;
    private final Consumer<Boolean> systemThemeChangeConsumer;
    private boolean themeChangeListenerIsRegistered;

    public MenuBar(XL2mQB gui) {
        throw new UnsupportedOperationException();
    }

    private Menu settingsMenu() {
        throw new UnsupportedOperationException();
    }

    private void unregisterThemeChangeListenerIfRegistered() {
        throw new UnsupportedOperationException();
    }

    private Menu templateMenu() {
        throw new UnsupportedOperationException();
    }

    private Menu helpMenu() {
        throw new UnsupportedOperationException();
    }

    public void setDisableSettingsMenu(boolean disabled) {
        throw new UnsupportedOperationException();
    }

    public void setDisableTemplateMenu(boolean disabled) {
        throw new UnsupportedOperationException();
    }
}
