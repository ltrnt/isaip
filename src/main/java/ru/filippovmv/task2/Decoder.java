package ru.filippovmv.task2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Decoder {

    public static final String DEFAULT_CONTAINER_PATH = "src/test/resources/task2/container.txt";
    public static final String DEFAULT_SECRET_PATH = "src/test/resources/task2/secretOutput.txt";
    private static final String ENCODING = "KOI8-R";

    public static void decode() throws IOException {
        try {
            File secretFile = new File(DEFAULT_SECRET_PATH);
            File container = new File(DEFAULT_CONTAINER_PATH);
            List<String> containerStrings = Files.readAllLines(container.toPath(), Charset.forName(ENCODING));
            List<String> bitsOfEncodedSecret = new ArrayList<>();

            int zeroCounter = 0;
            int bitsCounter = 0;
            StringBuilder bits = new StringBuilder();

            for (String line : containerStrings) {
                if (line.endsWith(" ")) {
                    bits.append("1");
                    zeroCounter = 0;
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

            System.out.println();

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

//            List<Byte> resultBytes = bytes.stream().map(b1te -> {
//                if (b1te > 128) {
//                    return (Byte) (byte) (b1te - 256);
//                }
//                return (Byte) (byte) b1te.intValue();
//            }).collect(Collectors.toList());




//            int containerPosition = 0;
//            for(int i = 0; i < secretByteArray.length; i++) {
//                int secretByte = secretByteArray[i];
//                int convertedSecretByte = secretByte;
//                if (secretByte < 0) {
//                    convertedSecretByte = (256 + secretByte);
//                }
//
//                StringBuilder secretByteToBitsString = new StringBuilder();
//                int temp = convertedSecretByte;
//                while (temp > 0) {
//                    secretByteToBitsString.append(temp % 2);
//                    temp /= 2;
//                }
//
//                if (secretByteToBitsString.length() < 8) {
//                    secretByteToBitsString.append("0");
//                }
//
//                String resultBitString = secretByteToBitsString.reverse().toString();
//
//                for (int bitPosition = 0; bitPosition < resultBitString.length(); bitPosition++, containerPosition++) {
//                    if (resultBitString.charAt(bitPosition) == '1') {
//                        containerStrings.set(containerPosition, containerStrings.get(containerPosition) + " ");
//                    }
//                }
//                System.out.println(secretByte + " " + convertedSecretByte + " " + secretString.charAt(i) + " " + secretByteToBitsString);
//            }
//
//            Files.write(container.toPath(), containerStrings, Charset.forName(ENCODING));
//            System.out.println("Encoded.");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
