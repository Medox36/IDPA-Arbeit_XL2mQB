package ch.ksh.xl2mqb.conversion.xml;

import ch.ksh.xl2mqb.conversion.image.ImageConverter;

/**
 * Class for generating xml String
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public class XMLUtil {

    public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * Generates the xml for a given tag with the given value an attributes.
     *
     * @param tag the xml tag (without < and >)
     * @param value of the xml tag
     * @param attributes for the xml tag
     * @return xml tag with value and attributes
     */
    public static String getXMLForTag(String tag, String value, String... attributes) {
        if (value.isBlank()) {
            return "";
        }

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

    /**
     * Generates a xml text tag with the given value and attributes
     *
     * @param value of the xml tag
     * @param attributes for the xml tag
     * @return xml text tag with value and attributes
     */
    public static String getXMLForTextTag(String value, String... attributes) {
        return getXMLForTag("text", value, attributes);
    }

    /**
     * Generates the xml for a given tag with the given value an attributes.
     * With the value being wrapped in a CDATA tag e.g (<![CDATA[ value ]]>)
     *
     * @param value of the xml tag
     * @param attributes for the xml tag
     * @return xml text tag with value and attributes
     */
    public static String getXMLForCDATATextTag(String value, String... attributes) {
        value = "<![CDATA[" + replaceNewLineWithLineBreak(value) + "]]>";
        return getXMLForTag("text", value, attributes);
    }

    /**
     * Replaces all occurrences of \n with <br>
     *
     * @param value to check for occurrences of \n
     * @return String with replaced \n
     */
    public static String replaceNewLineWithLineBreak(String value) {
        return value.replace("\n", "<br>");
    }

    /**
     * Generates the xml img tag with the given attributes.
     *
     * @param src attribute of the img tag
     * @param alt attribute of the img tag
     * @param otherAttributes attribute of the img tag
     * @return xml img tag with the given attributes
     */
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
