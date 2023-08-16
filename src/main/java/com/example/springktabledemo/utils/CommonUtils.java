package com.example.springktabledemo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    public static String getCurrentTime(){
        // Capture the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define a custom date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Convert LocalDateTime to string
        String formattedDateTime = now.format(formatter);

        System.out.println(formattedDateTime);
        return formattedDateTime;
    }
}
