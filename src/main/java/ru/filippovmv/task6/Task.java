package ru.filippovmv.task6;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Task {

    public static final String DEFAULT_TARGET_PATH = "src/test/resources/task6/secret.txt";
    public static final String DEFAULT_FOLDER_PATH = "src/test/resources/task6/targetFolder";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Зашифровать (1) или расшифровать (2)? (1/2): ");
        int answer = scanner.nextInt();
        if (answer == 1) {
            EndecoderFolder.encodeFolder(new File(DEFAULT_FOLDER_PATH).toPath(), "secret", new File(DEFAULT_TARGET_PATH).toPath());
        } else {
            EndecoderFolder.decodeFolder(new File(DEFAULT_TARGET_PATH).toPath(), "secret");
        }

    }

}
