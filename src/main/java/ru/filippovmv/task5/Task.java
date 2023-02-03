package ru.filippovmv.task5;

public class Task {
    public static void main(String[] args) {
        String encoded = Endecoder.encode("привет", "ключ");
        System.out.println("Encoded: " + encoded);
        System.out.println("Decoded: " + Endecoder.decode(encoded, "ключ"));
    }
}
