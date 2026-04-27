package com.datascienceacademy.backenddatascienceacademy.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.regex.*;

@Service
public class CvParseService {

    private static final List<String> SA_PROVINCES = List.of(
        "Eastern Cape", "Free State", "Gauteng", "KwaZulu-Natal",
        "Limpopo", "Mpumalanga", "North West", "Northern Cape", "Western Cape"
    );

    private static final List<String> SA_CITIES = List.of(
        "Johannesburg", "Cape Town", "Durban", "Pretoria", "Port Elizabeth",
        "Bloemfontein", "East London", "Nelspruit", "Polokwane", "Kimberley",
        "Rustenburg", "George", "Pietermaritzburg", "Benoni", "Tembisa",
        "Soweto", "Sandton", "Midrand", "Centurion", "Randburg"
    );

    private static final List<String> QUALIFICATION_KEYWORDS = List.of(
        "PhD", "Doctorate", "Master", "MSc", "MBA", "Honours", "Hons",
        "Bachelor", "BSc", "BCom", "BA ", "BEng", "BTech", "BEd",
        "National Diploma", "Diploma", "Certificate", "Matric", "Grade 12"
    );

    private static final List<String> INSTITUTION_KEYWORDS = List.of(
        "University", "Universiteit", "College", "Institute", "Academy",
        "School of", "Polytechnic", "Tshwane", "UNISA", "Wits", "UCT",
        "Stellenbosch", "UJ", "NWU", "UFS", "UKZN", "UWC", "UZ", "VUT",
        "DUT", "CPUT", "MUT", "CUT", "SMU", "Sol Plaatje"
    );

    public Map<String, String> parse(String cvBase64, String fileType) {
        String text = extractText(cvBase64, fileType);
        return parseFields(text);
    }

    private String extractText(String cvBase64, String fileType) {
        try {
            byte[] bytes = Base64.getDecoder().decode(cvBase64);
            if ("pdf".equalsIgnoreCase(fileType)) {
                PDDocument doc = Loader.loadPDF(bytes);
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(doc);
                doc.close();
                return text;
            } else if ("docx".equalsIgnoreCase(fileType)) {
                XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(bytes));
                XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
                return extractor.getText();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private Map<String, String> parseFields(String text) {
        Map<String, String> result = new HashMap<>();

        result.put("email",        extractEmail(text));
        result.put("phoneNumber",  extractPhone(text));
        result.put("githubUrl",    extractUrl(text, "github\\.com/[\\w-]+"));
        result.put("linkedinUrl",  extractUrl(text, "linkedin\\.com/in/[\\w-]+"));
        result.put("idNumber",     extractIdNumber(text));
        result.put("fullName",     extractName(text));
        result.put("province",     extractFromList(text, SA_PROVINCES));
        result.put("city",         extractFromList(text, SA_CITIES));
        result.put("highestQualification", extractQualification(text));
        result.put("institution",  extractInstitution(text));
        result.put("yearCompleted", extractYear(text));
        result.put("currentStatus", extractStatus(text));
        result.put("gender",       extractGender(text));
        result.put("nationality",  extractNationality(text));
        result.put("preferredLocation", result.get("city"));
        result.put("howDidYouHearAboutUs", "");

        return result;
    }

    // --- Extractors ---

    private String extractEmail(String text) {
        Matcher m = Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}").matcher(text);
        return m.find() ? m.group() : "";
    }

    private String extractPhone(String text) {
        // SA mobile/landline: 0XX XXX XXXX, +27 XX XXX XXXX, etc.
        Matcher m = Pattern.compile("(\\+27|0)[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{4}").matcher(text);
        if (m.find()) return m.group().replaceAll("\\s", "");
        // Fallback: any 10-digit number
        Matcher m2 = Pattern.compile("\\b0[0-9]{9}\\b").matcher(text);
        return m2.find() ? m2.group() : "";
    }

    private String extractUrl(String text, String pattern) {
        Matcher m = Pattern.compile("https?://(?:www\\.)?" + pattern, Pattern.CASE_INSENSITIVE).matcher(text);
        if (m.find()) return m.group();
        // Without http
        Matcher m2 = Pattern.compile("(?:www\\.)?" + pattern, Pattern.CASE_INSENSITIVE).matcher(text);
        if (m2.find()) {
            String url = m2.group();
            return url.startsWith("http") ? url : "https://" + url;
        }
        return "";
    }

    private String extractIdNumber(String text) {
        // SA ID: 13 digits
        Matcher m = Pattern.compile("\\b([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{7}\\b").matcher(text);
        return m.find() ? m.group() : "";
    }

    private String extractName(String text) {
        // Try to find a line near top that looks like a name (2-4 words, all caps or title case, no digits)
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            // Skip lines with common non-name keywords
            if (trimmed.toLowerCase().matches(".*(curriculum|vitae|resume|cv|profile|contact|address|email|phone|tel|www|http|@).*"))
                continue;
            // 2-4 words, only letters and spaces
            if (trimmed.matches("[A-Za-z]{2,}([ ][A-Za-z]{2,}){1,3}")) {
                return toTitleCase(trimmed);
            }
        }
        return "";
    }

    private String extractQualification(String text) {
        String upper = text.toUpperCase();
        for (String kw : QUALIFICATION_KEYWORDS) {
            int idx = upper.indexOf(kw.toUpperCase());
            if (idx >= 0) {
                // Extract the line containing this keyword
                int start = Math.max(0, text.lastIndexOf('\n', idx) + 1);
                int end = text.indexOf('\n', idx);
                if (end < 0) end = Math.min(text.length(), idx + 80);
                String line = text.substring(start, end).trim();
                if (line.length() > 3 && line.length() < 120) return line;
            }
        }
        return "";
    }

    private String extractInstitution(String text) {
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            for (String kw : INSTITUTION_KEYWORDS) {
                if (trimmed.toLowerCase().contains(kw.toLowerCase())) {
                    if (trimmed.length() > 3 && trimmed.length() < 100)
                        return trimmed;
                }
            }
        }
        return "";
    }

    private String extractYear(String text) {
        // Look for graduation/completion year: 4-digit year between 1990-2030
        Matcher m = Pattern.compile("(?:graduated|completed|obtained|awarded|passed|matric(?:ulated)?)[^\\n]{0,40}(20[0-2][0-9]|199[0-9])", Pattern.CASE_INSENSITIVE).matcher(text);
        if (m.find()) return m.group(1);
        // Fallback: last 4-digit year in range
        Matcher m2 = Pattern.compile("\\b(20[0-2][0-9]|199[0-9])\\b").matcher(text);
        String last = "";
        while (m2.find()) last = m2.group(1);
        return last;
    }

    private String extractStatus(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("currently employed") || lower.contains("full-time") || lower.contains("full time"))
            return "Employed";
        if (lower.contains("student") || lower.contains("currently studying") || lower.contains("enrolled"))
            return "Student";
        if (lower.contains("unemployed") || lower.contains("seeking employment") || lower.contains("looking for"))
            return "Unemployed";
        return "";
    }

    private String extractGender(String text) {
        String lower = text.toLowerCase();
        if (Pattern.compile("\\bgender[:\\s]+male\\b|\\bmale\\b").matcher(lower).find()) return "Male";
        if (Pattern.compile("\\bgender[:\\s]+female\\b|\\bfemale\\b").matcher(lower).find()) return "Female";
        return "";
    }

    private String extractNationality(String text) {
        Matcher m = Pattern.compile("(?:nationality|citizen(?:ship)?)[:\\s]+([A-Za-z ]{3,30})", Pattern.CASE_INSENSITIVE).matcher(text);
        if (m.find()) return m.group(1).trim();
        if (text.toLowerCase().contains("south african")) return "South African";
        return "";
    }

    private String extractFromList(String text, List<String> options) {
        for (String option : options) {
            if (Pattern.compile("\\b" + Pattern.quote(option) + "\\b", Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                return option;
            }
        }
        return "";
    }

    private String toTitleCase(String text) {
        String[] words = text.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
