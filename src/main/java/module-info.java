module IDPA_Arbeit_XL2mQB {
    requires org.apache.logging.log4j.core;
    requires org.apache.commons.io;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.jfxtras.styles.jmetro;
    requires com.jthemedetector;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.base;
    requires java.desktop;

    opens ch.ksh.xl2mqb.gui to javafx.graphics;
    exports ch.ksh.xl2mqb.conversion;
    exports ch.ksh.xl2mqb.settings;
    exports ch.ksh.xl2mqb.facade;
    exports ch.ksh.xl2mqb.excel;
    exports ch.ksh.xl2mqb.args;
    exports ch.ksh.xl2mqb.log;
    exports ch.ksh.xl2mqb.gui;
    exports ch.ksh.xl2mqb;
}
