package fr.neolithic.utilities.utils;

import java.util.Scanner;

public class IntegerUtils {
    public static boolean isInteger(String str) {
        Scanner scanner = new Scanner(str.trim());
        if (!scanner.hasNextInt()) return false;
        scanner.nextInt();
        return !scanner.hasNext();
    }
}