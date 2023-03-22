package ch.ksh.xl2mqb.settings;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
    private static Settings INSTANCE;
    private final Path settingsPath = Path.of("settings.conf");
    private final Map<String, ObjectProperty<Object>> settings = new HashMap<>();

    private Settings() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Settings.getInstance().save(), "Save-Settings-ShutdownHook"));
        try {
            readSettingsFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getSetting(String settingKey) {
        return getSettingProperty(settingKey).get();
    }

    public ObjectProperty<Object> getSettingProperty(String settingKey) {
        return settings.getOrDefault(settingKey, null);
    }

    public void setSetting(String settingKey, Object valueObject) {
        if (settings.containsKey(settingKey)) {
            getSettingProperty(settingKey).set(valueObject);
        } else {
            settings.put(settingKey, new SimpleObjectProperty<>(this, settingKey, valueObject));
        }
    }

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

    public void resetSettings() {
        replaceOrCreate("posY", -1.0);
        replaceOrCreate("posX", -1.0);
        replaceOrCreate("defaultSavePath", Path.of(System.getProperty("user.home")));
        replaceOrCreate("style", ExtendedStyle.LIGHT);
        replaceOrCreate("showErrors", Boolean.FALSE);
    }

    private void replaceOrCreate(String key, Object value) {
        if (settings.containsKey(key)) {
            settings.get(key).set(value);
        } else {
            settings.put(key, new SimpleObjectProperty<>(this, key, value));
        }
    }

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

    public static Settings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }
}
