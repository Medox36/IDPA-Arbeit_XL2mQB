package ch.ksh.xl2mqb.analysis;

import ch.ksh.xl2mqb.excel.CellExtractor;

import org.apache.poi.xssf.usermodel.XSSFCell;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * This Util class contains all methods for analysing an Excel File and prompt the user with all the mistakes
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public class AnalyserUtil {
    private static final Pattern numericPattern = Pattern.compile("^\\d+$");
    private static final Pattern decimalPattern = Pattern.compile("^\\d+(\\.\\d+)?$");
    private static final String imageFileRegex = "^(?<Path>(?:[a-zA-Z]:)+\\\\(?:[\\w\\s.]+\\\\)*)(?<FileName>[\\w\\s.]+?)$";
    private static final String imageFileExtensionRegex = "^(?<FileExtension>(png)|(jpg)|(gif)|(svg)|(PNG)|(JPG)|(GIF)|(SVG))$";

    /**
     * Checks if the cell containing the name of a question is empty.
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void questionName(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        if (CellExtractor.getCellValueSafe(cell).isBlank()) {
            sb.appendTabbed(rowNum, "hat keinen Namen.");
        }
    }

    /**
     * Checks if the cell containing the points for a question is empty and if it isn't empty if it is a decimal number
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void points(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        String cellValue = CellExtractor.getCellValueSafe(cell);
        if (cellValue.isEmpty()) {
            sb.appendTabbed(rowNum, "hat keine Punkte.");
        }
        if (!isDecimal(cellValue)) {
            sb.appendTabbed(rowNum, "hat eine ungültige Punktangabe (Nur Dezimalzahlen)");
        }
    }

    /**
     * Checks if the given String represents a numeric value
     *
     * @param strNum to check
     * @return true if the given String represents a numeric value, otherwise false
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }

        strNum = removeTailingDecimalZeros(strNum);
        return numericPattern.matcher(strNum).matches();
    }

    /**
     * Removes all tailing zeros of a String which represents a numeric or decimal value.
     * If the String represents a decimal value and there are only zeroes after the decimal point.
     * The decimal point will be removed as well.
     *
     * @param strNum to check
     * @return String representing a numeric or decimal value without tailing zeros.
     */
    public static String removeTailingDecimalZeros(String strNum) {
        if (!strNum.contains(".")) {
            return strNum;
        }

        String[] arr = strNum.split("\\.");
        while (arr[1].endsWith("0")) {
            arr[1] = arr[1].substring(0, arr[1].length() - 1);
        }

        String newNum = arr[0] + arr[1];
        if (newNum.endsWith(".")) {
            newNum = newNum.substring(0, newNum.length() - 1);
        }
        return newNum;
    }

    /**
     * Checks if the given String represents a decimal value
     *
     * @param strNum to check
     * @return true if the given String represents a decimal value, otherwise false
     */
    public static boolean isDecimal(String strNum) {
        if (strNum == null) {
            return false;
        }
        return decimalPattern.matcher(strNum).matches();
    }

    /**
     * Checks if the cell containing the general feedback is empty
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void generalFeedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "allgemeines");
    }

    /**
     *
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void correctFeedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "korrektes");
    }

    /**
     *
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void partiallyCorrect(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "teilweise korrektes");
    }

    /**
     *
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void incorrectFeedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "inkorrektes");
    }

    /**
     *
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     * @param feedbackType of feedback to be checked
     */
    private static void feedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum, String feedbackType) {
        if (CellExtractor.getCellValueSafe(cell).isBlank()) {
            sb.appendTabbed(rowNum, "hat kein " + feedbackType + " Feedback.");
        }
    }

    /**
     *
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void picture(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        ImageResult result = AnalyserUtil.pictureExistence(CellExtractor.getCellValueSafe(cell));
        switch (result) {
            case NotExistRemote -> sb.appendTabbed(rowNum, "hat ein Bild das online nicht gefunden werden kann " +
                                                           "oder es besteht gerade keine Internetverbindung.");
            case NotExistLocal -> sb.appendTabbed(rowNum, "hat ein Bild das lokal nicht gefunden werden " +
                                                          "kann/nicht existiert.");
            case WrongFileFormat -> sb.appendTabbed(rowNum, "hat ein Bildformat das nicht von Moodle unterstützt " +
                                                            "wird (Nur .png .jpg .gif und .svg werden unterstützt).");
            case NonExistent -> sb.appendTabbed(rowNum, "hat ein Bild das weder lokal noch online verfügbar " +
                                                        "ist oder falsch angegeben wurde.");
        }
    }

    /**
     * Checks the availability of a given image by its path.
     *
     * @param imagePath of the image to check
     * @return The matching enum constant of the enum class ImageResult
     * @see ImageResult
     */
    public static ImageResult pictureExistence(String imagePath) {
        if (imagePath.startsWith("http")) {
            if (imageExists(imagePath)) {
                return ImageResult.ExistRemote;
            } else {
                return ImageResult.NotExistRemote;
            }
        } else if (imagePath.matches(imageFileRegex)) {
            if (Files.notExists(Path.of(imagePath))) {
                return ImageResult.NotExistLocal;
            } else {
                String[] arr = imagePath.split("\\.");
                String fileExtension = arr[arr.length - 1];
                if (!fileExtension.matches(imageFileExtensionRegex)) {
                    return ImageResult.WrongFileFormat;
                } else {
                    return ImageResult.ExistLocal;
                }
            }
        } else {
            return ImageResult.NonExistent;
        }
    }

    /**
     * Checks if a remote image exists by the url of that image
     *
     * @param url of remote image to check
     * @return true url could bre resolved and remote image is found, otherwise false
     */
    private static boolean imageExists(String url) {
        try {
            int responseCode;
            if (url.startsWith("https")) {
                HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                con.setRequestMethod("GET");
                ImageIO.read(con.getInputStream());
                responseCode = con.getResponseCode();
                con.disconnect();
            } else {
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod("GET");
                ImageIO.read(con.getInputStream());
                responseCode = con.getResponseCode();
                con.disconnect();
            }
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the cell containing the hint and penalty of a question are empty or
     * only one of the two isn't empty and the other one is.
     * Also checks if the penalty is valid.(between 0 and 100).
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param hintCell to analyse
     * @param penaltyCell to analyse
     * @param rowNum row number of the given cell
     */
    public static void hintAndPenalty(TabbedStringBuilder sb, XSSFCell hintCell, XSSFCell penaltyCell, int rowNum) {
        String hint = CellExtractor.getCellValueSafe(hintCell);
        String penalty = CellExtractor.getCellValueSafe(penaltyCell);

        if (hint.isBlank() && penalty.isBlank()) {
            sb.appendTabbed(rowNum, "hat keinen Hinweis.");
        } else if (hint.isBlank() && !penalty.isBlank()) {
            sb.appendTabbed(rowNum, "hat einen Abzug ohne angegebenen Hinweis.");
        } else if (!hint.isBlank() && penalty.isBlank()) {
            sb.appendTabbed(rowNum, "hat einen Hinweis ohne angegebenen Abzug.");
        }

        if (!penalty.isBlank()) {
            if (isDecimal(penalty)) {
                try {
                    double penaltyValue = Double.parseDouble(penalty);
                    if (penaltyValue < 0 || penaltyValue > 100) {
                        sb.appendTabbed(rowNum, "hat einen ungültigen Abzug (Nur werte zwischen 0 und 100).");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    sb.appendTabbed(rowNum, "hat einen ungültigen Abzug, den das Programm nicht versteht.");
                }
            } else {
                if (penalty.startsWith("-")) {
                    sb.appendTabbed(rowNum, "hat einen ungültigen Abzug (keine Negativen Zahlen).");
                } else {
                    sb.appendTabbed(rowNum, "hat einen ungültigen Abzug (Nur Dezimalzahlen).");
                }
            }
        }
    }

    /**
     *
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param cell to analyse
     * @param rowNum row number of the given cell
     */
    public static void questionText(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        if (CellExtractor.getCellValueSafe(cell).isBlank()) {
            sb.appendTabbed(rowNum, "hat keine Fragestellung.");
        }
    }

    /**
     *
     * It also appends the error message to the given TabbedStringBuilder.
     *
     * @param sb TabbedStringBuilder to append to
     * @param answerCell to analyse
     * @param pointsCell to analyse
     * @param feedbackCell to analyse
     * @param rowNum row number of the given cell
     * @param answerNum answer number of given cells
     */
    public static void questionAnswer(
            TabbedStringBuilder sb,
            XSSFCell answerCell,
            XSSFCell pointsCell,
            XSSFCell feedbackCell,
            int rowNum,
            int answerNum
    ) {
        String answer = CellExtractor.getCellValueSafe(answerCell);
        String points = CellExtractor.getCellValueSafe(pointsCell);
        String feedback = CellExtractor.getCellValueSafe(feedbackCell);

        if (!answer.isEmpty() & !points.isEmpty() & feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " kein Feedback.");
        } else if (!answer.isEmpty() & points.isEmpty() & !feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " keine Punktzahl.");
        } else if (answer.isEmpty() & !points.isEmpty() & !feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " eine leere Antwort.");
        } else if (answer.isEmpty() & points.isEmpty() & !feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " eine leere Antwort und keine Punktzahl.");
        } else if (answer.isEmpty() & !points.isEmpty() & feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " eine leere Antwort und kein Feedback.");
        } else if (!answer.isEmpty() & points.isEmpty() & feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " keine Punktzahl und kein Feedback.");
        }
    }
}
