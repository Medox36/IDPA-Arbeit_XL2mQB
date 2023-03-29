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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Plugin(name = "TextAreaAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public class TextAreaAppender extends AbstractAppender {
    private static ProgressContainer progressContainer;
    private final Lock appendLock = new ReentrantLock();

    private TextAreaAppender(String name, Layout<? extends Serializable> layout) {
        super(name, null, layout, false, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(LogEvent event) {
        try {
            appendLock.lockInterruptibly();
            if (progressContainer != null) {
                String log = new String(getLayout().toByteArray(event));
                Platform.runLater(() -> progressContainer.appendLineToTextArea(log));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            appendLock.unlock();
        }
    }

    @PluginFactory
    public static TextAreaAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout
    ) {
        return new TextAreaAppender(name, layout);
    }

    public static void setProgressContainer(ProgressContainer progressContainer) {
        TextAreaAppender.progressContainer = progressContainer;
    }
}
