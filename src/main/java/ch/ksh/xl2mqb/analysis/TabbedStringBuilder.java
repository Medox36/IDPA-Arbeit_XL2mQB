package ch.ksh.xl2mqb.analysis;

/**
 * This custom String Builder is used to add appended text to the underlying StringBuilder
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 * @see StringBuilder
 */
public class TabbedStringBuilder {
    private final StringBuilder sb;

    public TabbedStringBuilder() {
        sb = new StringBuilder();
    }

    public TabbedStringBuilder(String str) {
        sb = new StringBuilder(str);
    }

    /**
     * appends a line to the underlying StringBuilder, but appended (with \t at the front)
     *
     * @param rowNum row Number for the question
     * @param str to append
     */
    public void appendTabbed(int rowNum, String str) {
        sb.append("\t").append("Frage auf Zeile ").append((rowNum + 1)).append(" ").append(str).append("\n");
    }

    /**
     * @return the String in the underlying StringBuilder
     */
    @Override
    public String toString() {
        return sb.toString();
    }
}
