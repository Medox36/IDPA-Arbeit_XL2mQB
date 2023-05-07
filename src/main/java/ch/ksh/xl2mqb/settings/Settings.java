package ch.ksh.xl2mqb.settings;

import ch.ksh.xl2mqb.facade.FileFacade;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores all settings and provides methods to read and set each setting.
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public class Settings {
    private static Settings INSTANCE;
    private final Path settingsPath = Path.of("settings.conf");
    private final Map<String, ObjectProperty<Object>> settings = new HashMap<>();

    private Settings() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> Settings.getInstance().save(),
                "Save-Settings-ShutdownHook")
        );
        try {
            readSettingsFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a setting value by the given key.
     *
     * @param settingKey to get value from
     * @return the value or null if key isn^t present
     */
    public Object getSetting(String settingKey) {
        return getSettingProperty(settingKey).get();
    }

    /**
     * Returns the ObjectProperty of a setting containing the setting value by the given key
     *
     * @param settingKey to het the ObjetProperty from
     * @return the ObjetProperty or null if key isn^t present
     */
    public ObjectProperty<Object> getSettingProperty(String settingKey) {
        return settings.getOrDefault(settingKey, null);
    }

    /**
     * Set a value of a setting by its key.
     * If the key is not yet present a new setting entry will be created.
     *
     * @param settingKey of setting to set value
     * @param valueObject the value to set
     */
    public void setSetting(String settingKey, Object valueObject) {
        if (settings.containsKey(settingKey)) {
            getSettingProperty(settingKey).set(valueObject);
        } else {
            settings.put(settingKey, new SimpleObjectProperty<>(this, settingKey, valueObject));
        }
    }

    /**
     * Reads the settings file and loads all settings.
     *
     * @throws IOException if an I/O error occurs
     */
    private void readSettingsFile() throws IOException {
        if (Files.notExists(settingsPath)) {
            resetSettings();
            return;
        }
        List<String> lines = Files.readAllLines(settingsPath);

        lines.removeIf(String::isBlank);

        for (String line : lines) {
            String[] splitLine = line.split("=");
            settings.put(splitLine[0], getProperClass(splitLine[0], splitLine[1]));
        }
    }

    /**
     * Creates a SimpleObjectProperty with the correct class as object for the value
     *
     * @param settingName fo the setting
     * @param val value of the setting
     * @return the nre SimpleObjectProperty
     * @see SimpleObjectProperty
     */
    private SimpleObjectProperty<Object> getProperClass(String settingName, String val) {
        switch (settingName) {
            case "posX", "posY" -> {
                return new SimpleObjectProperty<>(this, settingName, Double.parseDouble(val));
            }
            case "defaultSavePath" -> {
                return new SimpleObjectProperty<>(this, settingName, Path.of(val));
            }
            case "style" -> {
                return new SimpleObjectProperty<>(this, settingName, ExtendedStyle.valueOf(val));
            }
            case "showErrors" -> {
                return new SimpleObjectProperty<>(this, settingName, Boolean.parseBoolean(val));
            }
            default -> {
                return new SimpleObjectProperty<>(this, settingName, val);
            }
        }
    }

    /**
     * Resets settings to theirs default value.
     */
    public void resetSettings() {
        replaceOrCreate("posY", -1.0);
        replaceOrCreate("posX", -1.0);
        replaceOrCreate("defaultSavePath", FileFacade.getInstance().getDocumentsFolderPath());
        replaceOrCreate("style", ExtendedStyle.LIGHT);
        replaceOrCreate("showErrors", Boolean.FALSE);
    }

    /**
     * Adds the given settings pair of key and value to the settings map.
     * It either overrides if the given key is already existing or adds a new entry.
     *
     * @param key of the setting
     * @param value of the setting
     */
    private void replaceOrCreate(String key, Object value) {
        if (settings.containsKey(key)) {
            settings.get(key).set(value);
        } else {
            settings.put(key, new SimpleObjectProperty<>(this, key, value));
        }
    }

    /**
     * Invokes saving of settings.
     */
    public void save() {
        try {
            if (settings.size() < 5) {
                resetSettings();
            }
            saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all settings to the settings file.
     *
     * @throws IOException if an I/O error occurs
     */
    private void saveToFile() throws IOException {
        if (Files.notExists(settingsPath)) {
            Files.createFile(settingsPath);
        }
        BufferedWriter bufferedWriter = Files.newBufferedWriter(settingsPath);
        settings.forEach((keyString, valueObject) -> {
            try {
                bufferedWriter.write(keyString + "=" + valueObject.getValue().toString());
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    /**
     * Creates an instance of the Settings class if not already done.
     * @return an instance of the Settings class
     */
    public static Settings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }
}
