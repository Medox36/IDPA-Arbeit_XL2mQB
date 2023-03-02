module IDPA_Arbeit_XL2mQB {
    requires org.apache.commons.io;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.jfxtras.styles.jmetro;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.base;

    opens ch.ksh.xl2mqb to javafx.graphics;
    exports ch.ksh.xl2mqb;
}
