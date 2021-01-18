package utils;

import java.util.List;
import java.util.Scanner;

public class ConsoleUtils {

    public static void show(String message) {
        System.out.println(message);
    }

    public static <T> void showList(String message, List<T> list) {
        System.out.println(message);

        StringBuilder builder = new StringBuilder();

        for (T t : list) {
            builder.append(t.toString())
                    .append("\n");
        }

        System.out.println(builder);
    }

    public static String promptString(String message) {
        System.out.println(message);

        return new Scanner(System.in).next();
    }

    public static <T> T promptIndexedSelection(String message, List<T> list) {
        System.out.println(message);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append("[")
                    .append(i)
                    .append("] ")
                    .append(list.get(i).toString())
                    .append("\n");
        }

        int choice;
        do {
            System.out.println(builder);
            choice = new Scanner(System.in).nextInt();
            if (choice < 0 || choice >= list.size()) System.out.println("Please choose a valid element.");
        } while (choice < 0 || choice >= list.size());

        return list.get(choice);
    }
}
