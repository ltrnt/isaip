package ru.filippovmv.task3;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Decoder {

    public static final String DEFAULT_CONTAINER_SECRET_PATH = "src/test/resources/task3/containerWithSecret.txt";
    public static final String DEFAULT_SECRET_PATH = "src/test/resources/task3/secretOutput.txt";
    private static final String ENCODING = "KOI8-R";

    public static void decode() {
        try {
            File secretFile = new File(DEFAULT_SECRET_PATH);
            File container = new File(DEFAULT_CONTAINER_SECRET_PATH);
            String containerStrings = Files.readString(container.toPath(), Charset.forName(ENCODING));
            List<String> bitsOfEncodedSecret = new ArrayList<>();

            int zeroCounter = 0;
            int bitsCounter = 0;
            StringBuilder bits = new StringBuilder();

            for(int i = 0; i < containerStrings.length(); i++) {
                if (containerStrings.charAt(i) == ' ') {
                    if (containerStrings.length() > i + 1 && containerStrings.charAt(i + 1) == ' ') {
                        bits.append("1");
                        zeroCounter = 0;
                        i++;
                    } else {
                        bits.append('0');
                        zeroCounter++;
                    }

                    bitsCounter++;

                    if (zeroCounter >= 8 && bitsCounter >= 8) {
                        break;
                    }

                    if (bitsCounter >= 8) {
                        bitsOfEncodedSecret.add(bits.toString());
                        bits = new StringBuilder();
                        bitsCounter = 0;
                    }
                }
            }

            System.out.println(bitsOfEncodedSecret);

//            List<Integer> bytes = new ArrayList<>();
            StringBuilder result = new StringBuilder();
            for (String secretBits : bitsOfEncodedSecret) {
                int secretByte = 0;

                for (int i = 7, x = 1; i >= 0; i--, x = 2 * x) {
                    if (secretBits.charAt(i) == '1') {
                        secretByte += x;
                    }
                }

                result.append(new String(new byte[]{(byte)(secretByte >= 128 ? secretByte - 256 : secretByte)}, Charset.forName(ENCODING)));
                System.out.println((secretByte >= 128 ? secretByte - 256 : secretByte));
            }

            System.out.println(result);

            Files.writeString(secretFile.toPath(), result.toString(), Charset.forName(ENCODING));

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
