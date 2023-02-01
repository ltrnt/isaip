package ru.filippovmv.task4;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decoder {

    public static final Map<Character, Character> analogs = new HashMap<>();
    public static final String DEFAULT_CONTAINER_PATH = "src/test/resources/task4/container.txt";
    public static final String DEFAULT_SECRET_PATH = "src/test/resources/task4/secretOutput.txt";
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

    public static void decode() {
        try {
            File secretFile = new File(DEFAULT_SECRET_PATH);
            File container = new File(DEFAULT_CONTAINER_PATH);
            String containerString = Files.readString(container.toPath(), Charset.forName(ENCODING));
            List<String> bitsOfEncodedSecret = new ArrayList<>();

            int zeroCounter = 0;
            int bitsCounter = 0;
            StringBuilder bits = new StringBuilder();

            for(int i = 0; i < containerString.length(); i++) {
                if (analogs.containsKey(containerString.charAt(i)) || analogs.containsValue(containerString.charAt(i))) {
                    if (analogs.get(containerString.charAt(i)) == null) {
                        System.out.println(i + " 1 "+ analogs.get(containerString.charAt(i)));
                        bits.append("1");
                        zeroCounter = 0;
                    } else {
                        System.out.println(i + " 0 " + analogs.get(containerString.charAt(i)));
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
