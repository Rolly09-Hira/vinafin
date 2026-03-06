package com.spring.spring.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeolocalisationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtenir le pays à partir de l'IP (API gratuite)
     */
    public String getPaysFromIp(String ip) {
        // Éviter de géolocaliser les IP locales
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1") || ip.startsWith("192.168.")) {
            return "Local";
        }

        try {
            // Utilisation d'une API gratuite (ex: ip-api.com)
            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JsonNode json = objectMapper.readTree(content.toString());
            if (json.has("country") && !json.get("country").isNull()) {
                return json.get("country").asText();
            }
        } catch (Exception e) {
            // Log l'erreur mais ne pas bloquer
            System.err.println("Erreur géolocalisation: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtenir la ville à partir de l'IP
     */
    public String getVilleFromIp(String ip) {
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1") || ip.startsWith("192.168.")) {
            return "Local";
        }

        try {
            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JsonNode json = objectMapper.readTree(content.toString());
            if (json.has("city") && !json.get("city").isNull()) {
                return json.get("city").asText();
            }
        } catch (Exception e) {
            System.err.println("Erreur géolocalisation ville: " + e.getMessage());
        }

        return null;
    }
}