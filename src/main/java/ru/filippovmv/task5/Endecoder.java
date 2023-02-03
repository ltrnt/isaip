package ru.filippovmv.task5;

import java.nio.charset.Charset;

public class Endecoder {

    public static final int N = 2000;

    public static String encode(String container, String secret) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < container.length(); i++) {
            int code = (container.charAt(i) + secret.charAt(i % secret.length())) % N;
            result.append((char) code);
        }

        return result.toString();
    }

    public static String decode(String container, String secret) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < container.length(); i++) {
            int code = (container.charAt(i) - secret.charAt(i % secret.length()) + N) % N;
            result.append((char) code);
        }

        return result.toString();
    }

}
