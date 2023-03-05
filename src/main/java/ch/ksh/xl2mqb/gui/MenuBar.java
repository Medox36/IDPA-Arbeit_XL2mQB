package ch.ksh.xl2mqb.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class MenuBar extends javafx.scene.control.MenuBar {

    private final Menu settingsMenu;
    private final Menu templateMenu;

    public MenuBar() {
        settingsMenu = settingsMenu();
        templateMenu = templateMenu();

        ObservableList<Menu> menuBaritems = getMenus();
        menuBaritems.add(settingsMenu);
        menuBaritems.add(templateMenu);
        menuBaritems.add(helpMenu());
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
        MenuItem light = new MenuItem("Hell");
        light.setOnAction(event -> {

        });
        colorSubMenuItems.add(light);

        MenuItem dark = new MenuItem("Dunkel");
        dark.setOnAction(event -> {

        });
        colorSubMenuItems.add(dark);

        MenuItem systemSetting = new MenuItem("Systemeinstellung");
        systemSetting.setOnAction(event -> {

        });
        colorSubMenuItems.add(systemSetting);

        // separator
        settingsMenuItems.add(new SeparatorMenuItem());

        MenuItem resetSettings = new MenuItem("Einstellungen zurücksetzen");
        resetSettings.setOnAction(event -> {

        });
        settingsMenuItems.add(resetSettings);

        return settingsMenu;
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
