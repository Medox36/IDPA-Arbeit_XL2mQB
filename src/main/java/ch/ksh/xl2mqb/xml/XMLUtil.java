package ch.ksh.xl2mqb.xml;

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
}
