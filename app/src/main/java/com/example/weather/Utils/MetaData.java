package com.example.weather.Utils;

import java.util.Date;
import java.text.SimpleDateFormat;

public class MetaData {

    private static final String apiKey = "ed3b98d2b9857c5237aed4dcf98ec29e";
    private static final String apiUrl = "https://api.openweathermap.org/data/2.5/weather"; //?q=London,uk&appid=

    public static String whatIsTheDate() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
        return format.format(curDate);
    }

    public static String formatRequestURL(String cityName) {
        StringBuilder url = new StringBuilder(apiUrl);
        url.append(String.format("?q=%s&appid=%s&units=metric", cityName, apiKey));
        return url.toString();
    }

    public static String formatIconURL(String iconID) {
        StringBuilder url = new StringBuilder("https://openweathermap.org/img/w/");
        url.append(iconID);
        url.append(".png");
        return url.toString();
    }

}
