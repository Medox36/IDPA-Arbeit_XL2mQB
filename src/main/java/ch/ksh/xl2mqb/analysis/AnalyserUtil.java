package ch.ksh.xl2mqb.analysis;

import org.apache.poi.xssf.usermodel.XSSFCell;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class AnalyserUtil {
    private static final Pattern numericPattern = Pattern.compile("^\\d+$");
    private static final Pattern decimalPattern = Pattern.compile("^\\d+(\\.\\d+)?$");
    private static final String imageFileRegex = "^(?<Path>(?:[a-zA-Z]:)+\\\\(?:[\\w\\s.]+\\\\)*)(?<FileName>[\\w\\s.]+?)$";
    private static final String imageFileExtensionRegex = "^(?<FileExtension>(\\.png)|(\\.jpg)|(\\.gif)|(\\.svg)|(\\.PNG)|(\\.JPG)|(\\.GIF)|(\\.SVG))$";

    public static void questionName(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        if (cell.getStringCellValue().isBlank()) {
            sb.appendTabbed(rowNum, "hat keinen Namen.");
        }
    }

    public static void points(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        String cellValue = cell.getStringCellValue().trim();
        if (cellValue.isEmpty()) {
            sb.appendTabbed(rowNum, "hat keine Punkte.");
        }
        if (!isDecimal(cellValue)) {
            sb.appendTabbed(rowNum, "hat eine ungültige Punktangabe (Nur Dezimalzahlen)");
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }

        return numericPattern.matcher(strNum).matches();
    }

    public static boolean isDecimal(String strNum) {
        if (strNum == null) {
            return false;
        }

        return decimalPattern.matcher(strNum).matches();
    }

    public static void generalFeedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "allgemeines");
    }

    public static void correctFeedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "korrektes");
    }

    public static void partiallyCorrect(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "teilweise korrektes");
    }

    public static void incorrectFeedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        feedback(sb, cell, rowNum, "inkorrektes");
    }

    private static void feedback(TabbedStringBuilder sb, XSSFCell cell, int rowNum, String feedbackType) {
        if (cell.getStringCellValue().isBlank()) {
            sb.appendTabbed(rowNum, "hat kein " + feedbackType + " Feedback.");
        }
    }

    public static void picture(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        ImageResult result = AnalyserUtil.pictureExistence(cell.getStringCellValue().trim());
        switch (result) {
            case NotExistRemote -> sb.appendTabbed(rowNum, "hat ein Bild das online nicht gefunden werden kann oder es besteht gerade keine Internetverbindung.");
            case NotExistLocal -> sb.appendTabbed(rowNum, "hat ein Bild das lokal nicht gefunden werden kann/nicht existiert.");
            case WrongFileFormat -> sb.appendTabbed(rowNum, "hat ein Bildformat das nicht von Moodle unterstützt wird (Nur .png .jpg .gif und .svg werden unterstützt).");
            case NonExistent -> sb.appendTabbed(rowNum, "hat ein Bild das weder lokal noch online verfügbar ist oder falsch angegeben wurde.");
        }
    }

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

    public static void hintAndPenalty(TabbedStringBuilder sb, XSSFCell hintCell, XSSFCell penaltyCell, int rowNum) {
        String hint = hintCell.getStringCellValue().trim();
        String penalty = penaltyCell.getStringCellValue().trim();

        if (hint.isBlank() && penalty.isBlank()) {
            sb.appendTabbed(rowNum, "hat keinen Hinweis.");
        } else if (hint.isBlank() && !penalty.isBlank()) {
            sb.appendTabbed(rowNum, "hat einen Hinweis ohne angegebenen Abzug.");
        } else if (!hint.isBlank() && penalty.isBlank()) {
            sb.appendTabbed(rowNum, "hat einen Abzug ohne angegebenen Hinweis.");
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
                sb.appendTabbed(rowNum, "hat einen ungültigen Abzug (Nur Dezimalzahlen).");
            }
        }
    }

    public static void questionText(TabbedStringBuilder sb, XSSFCell cell, int rowNum) {
        if (cell.getStringCellValue().isBlank()) {
            sb.appendTabbed(rowNum, "hat keine Fragestellung.");
        }
    }

    public static void questionAnswer(TabbedStringBuilder sb, XSSFCell answerCell, XSSFCell pointsCell, XSSFCell feedbackCell, int rowNum, int answerNum) {
        String answer = answerCell.getStringCellValue().trim();
        String points = pointsCell.getStringCellValue().trim();
        String feedback = feedbackCell.getStringCellValue().trim();

        if (!answer.isEmpty() & !points.isEmpty() & feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " kein Feedback.");
        } else if (!answer.isEmpty() & points.isEmpty() & !feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " keine Punktzahl.");
        } else if (answer.isEmpty() & !points.isEmpty() & !feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " eine leere Antwort.");
        } else if (answer.isEmpty() & points.isEmpty() & !feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum +  " eine leere Antwort und keine Punktzahl.");
        } else if (answer.isEmpty() & !points.isEmpty() & feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " eine leere Antwort und kein Feedback.");
        } else if (!answer.isEmpty() & points.isEmpty() & feedback.isEmpty()) {
            sb.appendTabbed(rowNum, "hat für Antwort " + answerNum + " keine Punktzahl und kein Feedback.");
        }
    }
}
