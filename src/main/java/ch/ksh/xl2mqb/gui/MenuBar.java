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
import javafx.scene.control.ToggleGroup;

import java.util.function.Consumer;

public class MenuBar extends javafx.scene.control.MenuBar {

    private final MenuFacade menuFacade = MenuFacade.getInstance();
    private final Menu settingsMenu;
    private final Menu templateMenu;
    private final Consumer<Boolean> systemThemeChangeConsumer;
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
            if (!newValue) {
                showConversionErrors.setText("     Konversionsfehler anzeigen");
                colorSubmenu.setText("     Farbe wählen");
            } else {
                showConversionErrors.setText("Konversionsfehler anzeigen");
                colorSubmenu.setText("Farbe wählen");
            }
        });

        // submenu items
        ToggleGroup themeToggleGroup = new ToggleGroup();

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
        Platform.runLater(() -> {
            ExtendedStyle extendedStyle = menuFacade.getStyle();
            switch (extendedStyle) {
                case LIGHT -> themeToggleGroup.selectToggle(light);
                case DARK -> themeToggleGroup.selectToggle(dark);
                case SYSTEM -> themeToggleGroup.selectToggle(system);
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
}
