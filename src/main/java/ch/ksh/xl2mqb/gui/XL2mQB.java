package ch.ksh.xl2mqb.gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;

public class XL2mQB extends Application {

    private final MenuBar menuBar = new MenuBar();
    private final JMetro jMetro = new JMetro();
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.getIcons().add(new Image(Objects.requireNonNull(XL2mQB.class.getResourceAsStream("/ch/ksh/xl2mqb/images/icon.png"))));

    }

    private void homeScene() {

    }

    private void convertRunningScene() {

    }

    private void convertFinishScene() {

    }

    private void validateRunningScene() {

    }

    private void validateFinishScene() {

    }

    private void centerStageOnScreen() {

    }

    public void applyJMetroTheme(Style jMetrostyle) {

    }

    public void applyDarkTheme() {

    }

    public void applyLightTheme() {

    }

    public void applySystemTheme() {

    }
}
