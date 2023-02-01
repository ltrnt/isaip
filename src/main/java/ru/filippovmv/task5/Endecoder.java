package ru.filippovmv.task5;

public class Endecoder {

    public static String encode(String container, String secret) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < container.length(); i++) {
            int code = (container.charAt(i) + secret.charAt(i % secret.length())) % 2000;
            result.append((char) code);
        }

        return result.toString();
    }

    public static String decode(String container, String secret) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < container.length(); i++) {
            int code = (container.charAt(i) - secret.charAt(i % secret.length())+2000) % 2000;
            result.append((char) code);
        }

        return result.toString();
    }

}
