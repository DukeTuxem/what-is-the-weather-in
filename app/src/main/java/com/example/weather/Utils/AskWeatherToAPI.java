package com.example.weather.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AskWeatherToAPI {

    public static String callOpenWeather(String url) {
        String answer = null;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection socket = (HttpURLConnection) urlObj.openConnection();

            if (socket.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null)
                    sb.append(line);
                answer = sb.toString();
            }
            socket.disconnect();
        }
        catch (MalformedURLException e) {
            System.err.println("Malformed URL Exception : " + e.getStackTrace());
        }
        catch (IOException e) {
            System.err.println("IOException : " + e.getStackTrace());
        }
        return answer;
    }
}
