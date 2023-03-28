package ch.ksh.xl2mqb.xml;

import ch.ksh.xl2mqb.image.ImageConverter;

public class XMLUtil {
    public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static String getXMLForTag(String tag, String value, String... attributes) {
        StringBuilder xml = new StringBuilder();

        xml.append("<").append(tag);
        for (String attribute : attributes) {
            xml.append(" ").append(attribute);
        }
        xml.append(">");
        xml.append(value);
        xml.append("</").append(tag).append(">");

        return xml.toString();
    }

    public static String getXMLForTextTag(String value, String... attributes) {
        return getXMLForTag("text", value, attributes);
    }

    public static String getXMLForImgTag(String src, String alt, String... otherAttributes) {
        StringBuilder xml = new StringBuilder();

        src = ImageConverter.convertIfNecessary(src);

        xml.append("<img");
        xml.append(" src=\"").append(src).append("\"");
        xml.append(" alt=\"").append(alt).append("\"");
        for (String attribute : otherAttributes) {
            xml.append(" ").append(attribute);
        }
        xml.append(">");

        return xml.toString();
    }
}
