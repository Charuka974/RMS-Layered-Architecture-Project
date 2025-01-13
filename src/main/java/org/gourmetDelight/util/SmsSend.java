package org.gourmetDelight.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsSend {
    public static void sendSms(String phoneNumber, String message) {
        String smsApiUrl = System.getenv("smsAPI");
        if (smsApiUrl == null) {
            System.err.println("smsAPI environment variable is not set");
            return;
        }

        try {
            // Construct the full URL including the phone number and message
            String apiUrl = smsApiUrl + phoneNumber + "&message=Your secure verification code for Gourmet Delight is " + message;

            System.out.println("Request URL: " + apiUrl);

            // Create the URL object with the full URL
            URL url = new URL(apiUrl);

            // Open connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            con.setRequestMethod("GET");

            // Optional: Set request headers
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Get the response code
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Print the response
                System.out.println("Response: " + response.toString());
            }

        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }


}