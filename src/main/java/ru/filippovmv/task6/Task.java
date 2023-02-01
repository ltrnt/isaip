package ru.filippovmv.task6;

import java.io.File;
import java.io.IOException;

public class Task {

    public static final String DEFAULT_TARGET_PATH = "src/test/resources/task6/secret.txt";
    public static final String DEFAULT_FOLDER_PATH = "src/test/resources/task6/targetFolder";

    public static void main(String[] args) throws IOException {
//        EndecoderFolder.encodeFolder(new File(DEFAULT_FOLDER_PATH).toPath(), "secret", new File(DEFAULT_TARGET_PATH).toPath());
        EndecoderFolder.decodeFolder(new File(DEFAULT_TARGET_PATH).toPath(), "secret");
    }

}
