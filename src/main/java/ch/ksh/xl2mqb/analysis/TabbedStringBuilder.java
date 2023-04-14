package ch.ksh.xl2mqb.analysis;

public class TabbedStringBuilder {
    private final StringBuilder sb;

    public TabbedStringBuilder() {
        sb = new StringBuilder();
    }

    public TabbedStringBuilder(String str) {
        sb = new StringBuilder(str);
    }

    public void appendTabbed(int rowNum, String str) {
        sb.append("\t").append("Frage auf Zeile ").append(rowNum).append(" ").append(str).append("\n");
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
