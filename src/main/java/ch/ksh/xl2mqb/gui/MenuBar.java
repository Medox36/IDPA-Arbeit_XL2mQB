package ch.ksh.xl2mqb.gui;

import ch.ksh.xl2mqb.facade.MenuFacade;
import ch.ksh.xl2mqb.settings.ExtendedStyle;

import com.jthemedetecor.OsThemeDetector;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import jfxtras.styles.jmetro.Style;

import java.util.function.Consumer;

public class MenuBar extends javafx.scene.control.MenuBar {

    private final MenuFacade menuFacade = MenuFacade.getInstance();
    private final Menu settingsMenu;
    private final Menu templateMenu;
    private final Consumer<Boolean> systemThemeChangeConsumer;
    private ToggleGroup themeToggleGroup;
    private boolean themeChangeListenerIsRegistered;

    public MenuBar() {
        settingsMenu = settingsMenu();
        templateMenu = templateMenu();

        ObservableList<Menu> menuBarItems = getMenus();
        menuBarItems.add(settingsMenu);
        menuBarItems.add(templateMenu);
        menuBarItems.add(helpMenu());

        // listen for os theme changes
        systemThemeChangeConsumer = isDark -> Platform.runLater(() -> {
            if (isDark) {
                menuFacade.setStyle(ExtendedStyle.DARK);
            } else {
                menuFacade.setStyle(ExtendedStyle.LIGHT);
            }
        });
        themeChangeListenerIsRegistered = false;
    }

    private Menu settingsMenu() {
        // menu itself
        Menu settingsMenu = new Menu("Einstellungen");
        ObservableList<MenuItem> settingsMenuItems = settingsMenu.getItems();

        // menu items
        MenuItem standardPath = new MenuItem("Standartmässig speichern unter...");
        standardPath.setOnAction(event -> menuFacade.selectPAthToSaveXMLFilesTo());
        settingsMenuItems.add(standardPath);

        CheckMenuItem showConversionErrors = new CheckMenuItem("Konversionsfehler anzeigen");
        showConversionErrors.setSelected(menuFacade.areConversionErrorsShown());
        settingsMenuItems.add(showConversionErrors);

        MenuItem desktopShortcut = new MenuItem("Desktop-Verknüpfung hinzufügen");
        desktopShortcut.setOnAction(event -> menuFacade.addDesktopShortcut());
        settingsMenuItems.add(desktopShortcut);

        // submenu
        Menu colorSubmenu = new Menu("Farbe wählen");
        ObservableList<MenuItem> colorSubMenuItems = colorSubmenu.getItems();
        settingsMenuItems.add(colorSubmenu);
        showConversionErrors.selectedProperty().addListener((observable, oldValue, newValue) -> {
            menuFacade.showConversionErrors(newValue);

            // briefly change style to enforce redraw of menu to prevent wrong indentation
            Style currentJMetroStyle = XL2mQB.getCurrentJMetroStyle();
            ExtendedStyle extendedStyle = menuFacade.getStyle();

            if (currentJMetroStyle == Style.DARK) {
                menuFacade.setStyle(ExtendedStyle.LIGHT);
            } else {
                menuFacade.setStyle(ExtendedStyle.DARK);
            }

            menuFacade.setStyle(extendedStyle);
        });

        // submenu items
        themeToggleGroup = new ToggleGroup();

        RadioMenuItem light = new RadioMenuItem("Hell");
        colorSubMenuItems.add(light);

        RadioMenuItem dark = new RadioMenuItem("Dunkel");
        colorSubMenuItems.add(dark);

        RadioMenuItem system = new RadioMenuItem("Systemeinstellung");
        colorSubMenuItems.add(system);

        themeToggleGroup.getToggles().addAll(light, dark, system);
        themeToggleGroup.selectToggle(light);
        themeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == light) {
                unregisterThemeChangeListenerIfRegistered();
                menuFacade.setStyle(ExtendedStyle.LIGHT);
            } else if (newValue == dark) {
                unregisterThemeChangeListenerIfRegistered();
                menuFacade.setStyle(ExtendedStyle.DARK);
            } else if (newValue == system) {
                if (!themeChangeListenerIsRegistered) {
                    OsThemeDetector.getDetector().registerListener(systemThemeChangeConsumer);
                    themeChangeListenerIsRegistered = true;
                }
                menuFacade.setStyle(ExtendedStyle.SYSTEM);
            }
        });

        // separator
        settingsMenuItems.add(new SeparatorMenuItem());

        MenuItem resetSettings = new MenuItem("Einstellungen zurücksetzen");
        resetSettings.setOnAction(event -> menuFacade.resetSettings());
        settingsMenuItems.add(resetSettings);

        return settingsMenu;
    }

    private void unregisterThemeChangeListenerIfRegistered() {
        if (themeChangeListenerIsRegistered) {
            OsThemeDetector.getDetector().removeListener(systemThemeChangeConsumer);
            themeChangeListenerIsRegistered = false;
        }
    }

    private Menu templateMenu() {
        // menu itself
        Menu templateMenu = new Menu("Excel-Vorlage");
        ObservableList<MenuItem> templateMenuItems = templateMenu.getItems();

        // menu items
        MenuItem newExcelfile = new MenuItem("Neue Excel-Date von Vorlage");
        newExcelfile.setOnAction(event -> menuFacade.newExcelFileFromTemplate());
        templateMenuItems.add(newExcelfile);

        MenuItem setUpTemplate = new MenuItem("Excel-Vorlage einrichten");
        setUpTemplate.setOnAction(event -> menuFacade.arrangeExcelTemplate());
        templateMenuItems.add(setUpTemplate);

        // separator
        templateMenuItems.add(new SeparatorMenuItem());

        MenuItem saveTemplate = new MenuItem("Excel-Vorlage speichern unter...");
        saveTemplate.setOnAction(event -> menuFacade.selectPathToSaveExcelTemplateTo());
        templateMenuItems.add(saveTemplate);

        return templateMenu;
    }

    private Menu helpMenu() {
        // menu itself
        Menu helpMenu = new Menu("Hilfe");
        ObservableList<MenuItem> helpMenuItems = helpMenu.getItems();

        // menu items
        MenuItem instructions = new MenuItem("Instruktionen");
        instructions.setOnAction(event -> menuFacade.openInstructions());
        helpMenuItems.add(instructions);

        // separator
        helpMenuItems.add(new SeparatorMenuItem());

        MenuItem info = new MenuItem("Info...");
        info.setOnAction(event -> menuFacade.showInfoDialog());
        helpMenuItems.add(info);

        return helpMenu;
    }

    public void setDisableSettingsMenu(boolean disabled) {
        settingsMenu.setDisable(disabled);
    }

    public void setDisableTemplateMenu(boolean disabled) {
        templateMenu.setDisable(disabled);
    }

    public void setStates(ExtendedStyle extendedStyle, boolean showConversionErrors) {
        ObservableList<Toggle> toggles = themeToggleGroup.getToggles();
        Toggle toggle = null;

        switch (extendedStyle) {
            case LIGHT -> toggle = toggles.get(0);
            case DARK -> toggle = toggles.get(1);
            case SYSTEM -> toggle = toggles.get(2);
        }

        themeToggleGroup.selectToggle(toggle);
        ((CheckMenuItem) settingsMenu.getItems().get(1)).setSelected(showConversionErrors);
    }
}
