package ru.filippovmv.task3;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class Encoder {

    public static final String DEFAULT_CONTAINER_PATH = "src/test/resources/task3/container.txt";
    public static final String DEFAULT_CONTAINER_SECRET_PATH = "src/test/resources/task3/containerWithSecret.txt";
    public static final String DEFAULT_SECRET_PATH = "src/test/resources/task3/secret.txt";
    private static final String ENCODING = "KOI8-R";

    public static void encode() {
        try {
            File container = new File(DEFAULT_CONTAINER_PATH);
            StringBuilder containerString = new StringBuilder(Files.readString(container.toPath(), Charset.forName(ENCODING)));

            File secretFile = new File(DEFAULT_SECRET_PATH);
            String secretString = Files.readString(secretFile.toPath(), Charset.forName(ENCODING));

            if (secretString.length() * 8 > containerString.toString().split(" ").length - 1) {
                System.out.println("Not enough space in container");
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
                    while (containerString.toString().charAt(containerPosition) != ' ') {
                        containerPosition++;
                    }
                    if (resultBitString.charAt(bitPosition) == '1') {
                        String temp1 = containerString.substring(0, containerPosition);
                        String temp2 = containerString.substring(containerPosition, containerString.length());
                        containerString = new StringBuilder();
                        containerString.append(temp1);
                        containerString.append(" ");
                        containerString.append(temp2);
                        containerPosition += 1;

                    }
                }
                System.out.println(secretByte + " " + convertedSecretByte + " " + secretString.charAt(i) + " " + secretByteToBitsString);
            }

            Files.writeString(new File(DEFAULT_CONTAINER_SECRET_PATH).toPath(), containerString, Charset.forName(ENCODING));
            System.out.println("Encoded.");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
