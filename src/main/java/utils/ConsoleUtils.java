package utils;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtils {

    public static void show(String message) {
        System.out.println(message);
    }

    public static <T> void showList(String message, String errorMessage, List<T> list) {
        if (list.isEmpty()) show(errorMessage);
        else showList(message, list);
    }

    public static <T> void showList(String message, List<T> list) {
        if (list.isEmpty()) {
            show("THE LIST IS EMPTY");
        }

        show(message);

        StringBuilder builder = new StringBuilder();

        for (T t : list) {
            builder.append(t.toString())
                    .append("\n");
        }

        show(builder.toString());
    }

    public static String promptString(String message) {
        System.out.println(message);
        return new Scanner(System.in).nextLine();
    }

    public static String promptDate(String message) {
        try {
            String string = promptString(message);
            Date.valueOf(string);
            return string;
        } catch (IllegalArgumentException e) {
            show("THIS IS NOT A VALID DATE");
            return promptDate(message);
        }
    }

    public static Integer promptInteger(String message) {
        System.out.println(message);
        return new Scanner(System.in).nextInt();
    }

    public static <T> T promptIndexedSelection(String message, String errorMessage, List<T> list) {
        if (list.isEmpty()) {
            show(errorMessage);
            return null;
        } else {
            return promptIndexedSelection(message, list);
        }
    }

    public static <T> T promptIndexedSelection(String message, List<T> list) {
        if (list.isEmpty()) {
            show("THE LIST IS EMPTY");
            return null;
        }

        show(message);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append("[")
                    .append(i + 1)
                    .append("] ")
                    .append(list.get(i).toString())
                    .append("\n");
        }

        int choice;
        do {
            show(builder.toString());
            choice = promptInteger("");

            if (choice < 1 || choice > list.size()) show("Please choose a valid element.");
        } while (choice < 1 || choice > list.size());

        return list.get(choice - 1);
    }
}
