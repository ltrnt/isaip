package ru.filippovmv.task4;

import java.util.Scanner;

public class Task {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Зашифровать (1) или расшифровать (2) секрет? (1/2): ");
        int answer = scanner.nextInt();
        if (answer == 1) {
            Encoder.encode();
        } else {
            Decoder.decode();
        }
    }

}
