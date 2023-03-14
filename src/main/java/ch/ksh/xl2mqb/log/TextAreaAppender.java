package ch.ksh.xl2mqb.log;

import javafx.scene.control.TextArea;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.concurrent.locks.Lock;

@Plugin(name = "TextAreaAppender",
        category = "Core",
        elementType = "appender",
        printObject = true)
public class TextAreaAppender extends AbstractAppender {
    private static TextArea textArea;
    private static Lock readLock;

    private TextAreaAppender(String name) {
        super(name, null, PatternLayout.createDefaultLayout(), false, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(LogEvent event) {
        throw new UnsupportedOperationException();
    }

    @PluginFactory
    public static TextAreaAppender createAppender(@PluginAttribute("name") String name) {
        throw new UnsupportedOperationException();
    }

    public static void setTextArea(TextArea textArea) {
        throw new UnsupportedOperationException();
    }
}
