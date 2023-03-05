package ch.ksh.xl2mqb.gui;

import com.jthemedetecor.OsThemeDetector;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

import java.util.function.Consumer;

public class MenuBar extends javafx.scene.control.MenuBar {

    private final Menu settingsMenu;
    private final Menu templateMenu;
    private final XL2mQB gui;
    private final Consumer<Boolean> systemThemeChangeConsumer;
    private boolean themeChangeListenerIsRegistered;

    public MenuBar(XL2mQB gui) {
        settingsMenu = settingsMenu();
        templateMenu = templateMenu();

        ObservableList<Menu> menuBaritems = getMenus();
        menuBaritems.add(settingsMenu);
        menuBaritems.add(templateMenu);
        menuBaritems.add(helpMenu());

        this.gui = gui;

        // listen for os theme changes
        systemThemeChangeConsumer = isDark -> Platform.runLater(() -> {
            if (isDark) {
                gui.applyDarkTheme();
            } else {
                gui.applyLightTheme();
            }
        });
        themeChangeListenerIsRegistered = false;
    }

    private Menu settingsMenu() {
        // menu itself
        Menu settingsMenu = new Menu("Einstellungen");
        ObservableList<MenuItem> settingsMenuItems = settingsMenu.getItems();

        // menu items
        MenuItem standardPath = new MenuItem("Standartmässig speicher unter...");
        standardPath.setOnAction(event -> {

        });
        settingsMenuItems.add(standardPath);

        CheckMenuItem showConversionErrors = new CheckMenuItem("Konversionsfehler anzeigen");
        showConversionErrors.selectedProperty().addListener((observable, oldValue, newValue) -> {

        });
        settingsMenuItems.add(showConversionErrors);

        MenuItem desktopShortcut = new MenuItem("Desktop-Verknüpfung hinzufügen");
        desktopShortcut.setOnAction(event -> {

        });
        settingsMenuItems.add(desktopShortcut);

        // submenu
        Menu colorSubmenu = new Menu("Farbe wählen");
        ObservableList<MenuItem> colorSubMenuItems = colorSubmenu.getItems();
        settingsMenuItems.add(colorSubmenu);

        // submenu items
        ToggleGroup themeToggleGroup = new ToggleGroup();

        RadioMenuItem light = new RadioMenuItem("Hell");
        colorSubMenuItems.add(light);

        RadioMenuItem dark = new RadioMenuItem("Dunkel");
        colorSubMenuItems.add(dark);

        RadioMenuItem systemSetting = new RadioMenuItem("Systemeinstellung");
        colorSubMenuItems.add(systemSetting);

        themeToggleGroup.getToggles().addAll(light, dark, systemSetting);
        themeToggleGroup.selectToggle(light);
        themeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == light) {
                unregisterThemeChangeListenerIfRegistered();
                gui.applyLightTheme();
            } else if (newValue == dark) {
                unregisterThemeChangeListenerIfRegistered();
                gui.applyDarkTheme();
            } else if (newValue == systemSetting) {
                if (!themeChangeListenerIsRegistered) {
                    OsThemeDetector.getDetector().registerListener(systemThemeChangeConsumer);
                    themeChangeListenerIsRegistered = true;
                }
                gui.applySystemTheme();
            }
        });

        // separator
        settingsMenuItems.add(new SeparatorMenuItem());

        MenuItem resetSettings = new MenuItem("Einstellungen zurücksetzen");
        resetSettings.setOnAction(event -> {

        });
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
        newExcelfile.setOnAction(event -> {

        });
        templateMenuItems.add(newExcelfile);

        MenuItem setUpTemplate = new MenuItem("Excel-Vorlage einrichten");
        setUpTemplate.setOnAction(event -> {

        });
        templateMenuItems.add(setUpTemplate);

        // separator
        templateMenuItems.add(new SeparatorMenuItem());

        MenuItem saveTemplate = new MenuItem("Excel-Vorlage speichern unter...");
        saveTemplate.setOnAction(event -> {

        });
        templateMenuItems.add(saveTemplate);

        return templateMenu;
    }

    private Menu helpMenu() {
        // menu itself
        Menu helpMenu = new Menu("Hilfe");
        ObservableList<MenuItem> helpMenuItems = helpMenu.getItems();

        // menu items
        MenuItem instructions = new MenuItem("Instruktionen");
        instructions.setOnAction(event -> {

        });
        helpMenuItems.add(instructions);

        // separator
        helpMenuItems.add(new SeparatorMenuItem());

        MenuItem info = new MenuItem("Info...");
        info.setOnAction(event -> {

        });
        helpMenuItems.add(info);

        return helpMenu;
    }

    public void setDisableSettingsMenu(boolean disabled) {
        settingsMenu.setDisable(disabled);
    }

    public void setDisableTemplateMenu(boolean disabled) {
        templateMenu.setDisable(disabled);
    }
}
