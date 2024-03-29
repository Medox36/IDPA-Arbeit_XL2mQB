package ch.ksh.xl2mqb.log;

import ch.ksh.xl2mqb.gui.ProgressContainer;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import javafx.application.Platform;

import java.io.Serializable;

/**
 * A Custom Appender to append to a text area, using Log4j plugins.
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
@Plugin(name = "TextAreaAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public class TextAreaAppender extends AbstractAppender {
    private static ProgressContainer progressContainer;

    private TextAreaAppender(String name, Layout<? extends Serializable> layout) {
        super(name, null, layout, false, Property.EMPTY_ARRAY);
    }

    /**
     * Appends a log event to the text area of the progress container.
     * Used by Log4j
     *
     * @param event The LogEvent.
     */
    @Override
    public void append(LogEvent event) {
        if (progressContainer != null) {
            String log = new String(getLayout().toByteArray(event));
            Platform.runLater(() -> progressContainer.appendLineToTextArea(log));
        }
    }

    /**
     * Creates an instance of a TextAreaAppender.
     * Used by Log4j
     *
     * @param name of the appender
     * @param layout of the appender
     * @return an instance of a TextAreaAppender
     */
    @PluginFactory
    public static TextAreaAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout
    ) {
        return new TextAreaAppender(name, layout);
    }

    /**
     * Sets the progress container reference used by this appender.
     *
     * @param progressContainer reference to set
     */
    public static void setProgressContainer(ProgressContainer progressContainer) {
        TextAreaAppender.progressContainer = progressContainer;
    }
}
