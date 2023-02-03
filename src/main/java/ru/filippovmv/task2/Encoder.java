package ru.filippovmv.task2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class Encoder {

    public static final String DEFAULT_CONTAINER_EMPTY_PATH = "src/test/resources/task2/container.txt";
    public static final String DEFAULT_CONTAINER_SECRET_PATH = "src/test/resources/task2/containerWithSecret.txt";
    public static final String DEFAULT_SECRET_PATH = "src/test/resources/task2/secret.txt";
    private static final String ENCODING = "KOI8-R";

    public static void encode() throws IOException {
        try {
            File container = new File(DEFAULT_CONTAINER_EMPTY_PATH);
            List<String> containerStrings = Files.readAllLines(container.toPath(), Charset.forName(ENCODING));

            File secretFile = new File(DEFAULT_SECRET_PATH);
            String secretString = Files.readString(secretFile.toPath(), Charset.forName(ENCODING));

            if (secretString.length() * 8 > containerStrings.size()) {
                System.out.println("Not enough lines in container");
                return;
            }

            byte[] secretByteArray = secretString.getBytes(ENCODING);

            int containerPosition = 0;
            for(int i = 0; i < secretByteArray.length; i++) {
                int secretByte = secretByteArray[i];
                int convertedSecretByte = secretByte;
                if (secretByte < 0) {
                    convertedSecretByte = (256 + secretByte);
                }

                StringBuilder secretByteToBitsString = new StringBuilder();
                int temp = convertedSecretByte;
                while (temp > 0) {
                    secretByteToBitsString.append(temp % 2);
                    temp /= 2;
                }

                if (secretByteToBitsString.length() < 8) {
                    for (int k = 0; k <= (8 - secretByteToBitsString.length()); k++) {
                        secretByteToBitsString.append("0");
                    }
                }

                String resultBitString = secretByteToBitsString.reverse().toString();

                for (int bitPosition = 0; bitPosition < resultBitString.length(); bitPosition++, containerPosition++) {
                    if (resultBitString.charAt(bitPosition) == '1') {
                        containerStrings.set(containerPosition, containerStrings.get(containerPosition) + " ");
                    }
                }
                System.out.println(secretByte + " " + convertedSecretByte + " " + secretString.charAt(i) + " " + secretByteToBitsString);
            }

            Files.write(new File(DEFAULT_CONTAINER_SECRET_PATH).toPath(), containerStrings, Charset.forName(ENCODING));
            System.out.println("Encoded.");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
