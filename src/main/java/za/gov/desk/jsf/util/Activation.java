/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.desk.jsf.util;

/**
 *
 * @author kelvinashu
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activation {

    private static final String REST_URI
            = "http://158.69.199.58:8080/activation/rest/xml/client/query?companyName=Ekurhuleni%20Metropolitan%20Municipality";

    public static Boolean isActive() {
        String status = "";
        try {

            URL url = new URL(REST_URI);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
                status = output.substring(output.lastIndexOf(":") + 1);
            }

            conn.disconnect();

        } catch (IOException | RuntimeException e) {

            System.out.println("Exception occured " + e);

        }
        return status.contains("true");
    }

}
