package ru.filippovmv.task1;

import java.util.Scanner;

public class Task {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Расчитать хэш(1) или проверить(2) файлы? (1/2): ");
        int answer = scanner.nextInt();
        if (answer == 1) {
            HashChecker.calculateHashAndWriteToFile(null, null); // Calculate
        } else {
            HashChecker.checkHash(null, null); // Check
        }
    }
}
