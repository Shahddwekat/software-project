//Ai code
package edu.najah.software.ai;

import edu.najah.software.domain.Appointment;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

// Uses the Groq AI API to generate a smart summary and analysis of the current appointment schedule
public class AISummaryService {

    // Groq API key
	private static final String API_KEY;

	static {
	    java.util.Properties config = new java.util.Properties();
	    try (java.io.InputStream in = new java.io.FileInputStream("groq.properties")) {
	        config.load(in);
	    } catch (Exception e) {
	        System.err.println("groq.properties not found — AI summary disabled");
	    }
	    API_KEY = config.getProperty("groq.api.key", "");
	}
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";
    
    /**
     * Sends the current list of appointments to Groq AI and gets back a professional summary and analysis report.
     * @param appointments the full list of appointments to summarize
     * @return a formatted AI-generated report as a string
     */
    
    public String generateSummary(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            return "No appointments found. Book some appointments first, then generate a summary.";
        }

        String appointmentData = buildAppointmentData(appointments);
        String prompt = buildPrompt(appointmentData, appointments.size());

        try {
            return callGroqAPI(prompt);
        } catch (Exception e) {
            return "Could not connect to AI service: " + e.getMessage();
        }
    }


    private String buildAppointmentData(List<Appointment> appointments) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        for (Appointment a : appointments) {
            sb.append("- ID: ").append(a.getId())
              .append(", Type: ").append(a.getType() != null ? a.getType() : "General")
              .append(", DateTime: ").append(a.getDateTime().format(fmt))
              .append(", Duration: ").append(a.getDuration()).append(" min")
              .append(", Participants: ").append(a.getParticipants())
              .append(", Status: ").append(a.getStatus())
              .append("\n");
        }
        return sb.toString();
    }

    private String buildPrompt(String data, int count) {
        return "You are an AI assistant for an Appointment Scheduling System. "
            + "Analyze the following " + count + " appointment(s) and generate a concise professional report. "
            + "Include: a short overview, breakdown by type, any patterns or observations, "
            + "and 2-3 practical recommendations for scheduling improvement. "
            + "Keep the tone professional but friendly. Use plain text, no markdown.\n\n"
            + "APPOINTMENTS:\n" + data;
    }

    /**
     * Makes the HTTP POST request to the Groq API and returns the response text.
     * Groq uses the OpenAI compatible API format.
     */
    private String callGroqAPI(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);

        String jsonBody = "{"
            + "\"model\": \"" + MODEL + "\","
            + "\"messages\": ["
            + "  {\"role\": \"system\", \"content\": \"You are a helpful scheduling assistant.\"},"
            + "  {\"role\": \"user\", \"content\": \"" + escapeJson(prompt) + "\"}"
            + "],"
            + "\"max_tokens\": 1024,"
            + "\"temperature\": 0.7"
            + "}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        java.io.InputStream stream = responseCode == 200
            ? conn.getInputStream()
            : conn.getErrorStream();

        Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        if (responseCode != 200) {
            throw new Exception("API returned " + responseCode + ": " + response);
        }

        return parseTextFromResponse(response.toString());
    }

    /**
     * Pulls the text content out of Groq's JSON response
     * @param json the raw JSON response from the API
     * @return the extracted text content
     */
    private String parseTextFromResponse(String json) {
        String marker = "\"content\":\"";
        int idx = json.indexOf(marker);
        if (idx == -1) {
            marker = "\"content\": \"";
            idx = json.indexOf(marker);
        }
        if (idx == -1) return "Could not parse AI response.";

        int start = idx + marker.length();
        int end = findClosingQuote(json, start);
        if (end == -1) return "Could not parse AI response.";

        return unescape(json.substring(start, end));
    }

    /**
     * Finds the closing quote of a JSON string, skipping escaped quotes
     * @param json the full JSON string
     * @param start index right after the opening quote
     * @return index of the closing quote, or -1 if not found
     */
    private int findClosingQuote(String json, int start) {
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == '"' && json.charAt(i - 1) != '\\') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Converts JSON escape sequences back to normal characters
     * @param text the escaped JSON string content
     * @return the unescaped plain text
     */
    private String unescape(String text) {
        return text.replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t")
                   .replace("\\\"", "\"")
                   .replace("\\'", "'")
                   .replace("\\\\", "\\");
    }

  
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}