package ch.ksh.xl2mqb.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Custom GUI Container which shows a progress bar and a text area for any errors or information
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
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

        setSpacing(10);
        setPadding(new Insets(30, 40, 20, 40));
        getChildren().addAll(convertWrapper, convertPBWrapper, textArea);
    }

    /**
     * Sets the button wrapper, containing the correct Buttons.
     *
     * @param buttonWrapper to set
     */
    public void setButtonWrapper(BorderPane buttonWrapper) {
        ObservableList<Node> children = getChildren();
        if (children.size() > 3) {
            children.remove(3);
        }
        children.add(buttonWrapper);
    }

    /**
     * Appends a line to the text area
     *
     * @param line to append
     */
    public void appendLineToTextArea(String line) {
        appendToTextArea(line + "\n");
    }

    /**
     * Appends a string to the text area
     *
     * @param string to append
     */
    public void appendToTextArea(String string) {
        textArea.appendText(string);
    }

    /**
     * Clears the text area
     */
    public void clearTextArea() {
        textArea.clear();
    }

    /**
     * Gets the progress of the progress bar
     *
     * @return the progress, value between 0.0(0%) an 1.0(100%)
     */
    public double getProgress() {
        return progressBar.getProgress();
    }

    /**
     * Sets the progress of the progress bar.
     *
     * @param progress to set, value between 0.0(0%) an 1.0(100%)
     */
    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    /**
     * Sets the text of the label, which is right above the progress bar.
     *
     * @param labelText text to set
     */
    public void setLabelText(String labelText) {
        textLabel.setText(labelText);
    }
}
