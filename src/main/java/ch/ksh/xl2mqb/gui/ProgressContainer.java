package ch.ksh.xl2mqb.gui;

import ch.ksh.xl2mqb.facade.AnalysisFacade;
import ch.ksh.xl2mqb.facade.ConvertFacade;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProgressContainer extends VBox {

    private final Label textLabel;
    private final ProgressBar progressBar;
    private final TextArea textArea;

    public ProgressContainer() {
        textLabel = new Label("");

        VBox convertWrapper = new VBox(textLabel);
        convertWrapper.setPadding(new Insets(0, 0, 0, 5));

        progressBar = new ProgressBar();

        VBox convertPBWrapper = new VBox(progressBar);
        convertPBWrapper.setPadding(new Insets(0, 0, 10, 0));

        textArea = new TextArea("");
        textArea.setMinSize(586, 265);
        textArea.setEditable(false);
        setVgrow(textArea, Priority.ALWAYS);

        ConvertFacade.getInstance().setTextArea(textArea);
        AnalysisFacade.getInstance().setTextArea(textArea);

        setSpacing(10);
        setPadding(new Insets(30, 40, 20, 40));
        getChildren().addAll(convertWrapper, convertPBWrapper, textArea);
    }

    public void setButtonWrapper(BorderPane buttonWrapper) {
        ObservableList<Node> children = getChildren();
        if (children.size() > 3) {
            children.remove(3);
        }
        children.add(buttonWrapper);
    }

    public void appendLineToTextArea(String line) {
        appendToTextArea(line + "\n");
    }

    public void appendToTextArea(String string) {
        textArea.appendText(string);
    }

    public void clearTextArea() {
        textArea.clear();
    }

    public double getProgress() {
        return progressBar.getProgress();
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    public void setLabelText(String labelText) {
        textLabel.setText(labelText);
    }
}
