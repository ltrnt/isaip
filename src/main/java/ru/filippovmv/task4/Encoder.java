package ru.filippovmv.task4;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Encoder {

    public static final Map<Character, Character> analogs = new HashMap<>();
    public static final String DEFAULT_CONTAINER_PATH = "src/test/resources/task4/container.txt";
    public static final String DEFAULT_SECRET_PATH = "src/test/resources/task4/secret.txt";
    private static final String ENCODING = "KOI8-R";

    static {
        analogs.put('у', 'y');
        analogs.put('е', 'e');
        analogs.put('х', 'x');
        analogs.put('а', 'a');
        analogs.put('р', 'p');
        analogs.put('о', 'o');
        analogs.put('с', 'c');
        analogs.put('К', 'K');
        analogs.put('Е', 'E');
        analogs.put('Н', 'H');
        analogs.put('Х', 'X');
        analogs.put('В', 'B');
        analogs.put('А', 'A');
        analogs.put('Р', 'P');
        analogs.put('О', 'O');
        analogs.put('С', 'C');
        analogs.put('М', 'M');
        analogs.put('Т', 'T');
    }

    public static void encode(){
        try {
            File container = new File(DEFAULT_CONTAINER_PATH);
            StringBuilder containerString = new StringBuilder(Files.readString(container.toPath(), Charset.forName(ENCODING)));

            File secretFile = new File(DEFAULT_SECRET_PATH);
            String secretString = Files.readString(secretFile.toPath(), Charset.forName(ENCODING));

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

                for (int bitPosition = 0; bitPosition < resultBitString.length(); bitPosition++) {
                    while (analogs.get(containerString.charAt(containerPosition)) == null) {
                        containerPosition++;
                    }
                    if (resultBitString.charAt(bitPosition) == '1') {
                        containerString.setCharAt(containerPosition, analogs.get(containerString.charAt(containerPosition)));
                    }
                    containerPosition++;
                }
                System.out.println(secretByte + " " + convertedSecretByte + " " + secretString.charAt(i) + " " + secretByteToBitsString);
            }

            Files.writeString(container.toPath(), containerString.toString(), Charset.forName(ENCODING));
            System.out.println("Encoded.");

        } catch (NullPointerException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
