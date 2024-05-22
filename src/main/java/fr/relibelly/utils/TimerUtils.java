package fr.relibelly.utils;

public class TimerUtils {

    public static String formatTimer(int toSecond) {
        if (toSecond < 0) {
            System.out.println("La durée ne peut pas être négative");
            return null;
        }

        int hour = toSecond / 3600;
        int minutes = (toSecond % 3600) / 60;
        int secondes = toSecond % 60;

        if (hour > 0) {
            return hour + "h " + minutes + "m " + secondes + "s";
        } else if (minutes > 0) {
            return minutes + "m " + secondes + "s";
        } else {
            return secondes + "s";
        }
    }
}
