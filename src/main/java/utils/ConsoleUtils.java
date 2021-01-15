package utils;

import java.util.List;
import java.util.Scanner;

public class ConsoleUtils {

    public static <T> int indexedSelection(String message, List<T> list) {
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
            System.out.println(builder.toString());
            choice = new Scanner(System.in).nextInt();
        } while (choice < 0 || choice >= list.size());

        return choice;
    }
}
