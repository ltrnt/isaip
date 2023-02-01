package ru.filippovmv.task6;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.filippovmv.task5.Endecoder;
import ru.filippovmv.task6.datamodel.FileH;
import ru.filippovmv.task6.datamodel.Folder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class EndecoderFolder {

    public static void encodeFolder(Path folder, String secret, Path target) throws IOException {
        Set<String> folderPaths = new HashSet<>();
        List<FileH> files = new ArrayList<>();

        Files.walk(folder)
                .filter(Files::isRegularFile)
                .peek(el -> {
                    try {
                        files.add(new FileH(el.toAbsolutePath().toString(), Files.readString(el)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        Files.walk(folder)
                .filter(Files::isDirectory)
                .peek(el -> folderPaths.add(el.toAbsolutePath().toString()))
                .sorted((a1, a2) -> -a1.toString().compareTo(a2.toString()))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                        System.out.println("Error while deleting");
                    }
                });


        String s = new ObjectMapper().writeValueAsString(new Folder(folderPaths, files));
        Files.writeString(target, Endecoder.encode(s, secret), StandardCharsets.UTF_8);
        Files.deleteIfExists(folder);
    }

    public static void decodeFolder(Path container, String secret) throws IOException {
        String containerString = Files.readString(container, StandardCharsets.UTF_8);

        String decode = Endecoder.decode(containerString, secret);

        Folder folder = new ObjectMapper().readValue(decode, Folder.class);

        folder.getFolders().stream().sorted(Comparator.comparing(String::toString))
                .forEach(el -> {
                    try {
                        Files.createDirectory(Path.of(el));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        for (FileH file : folder.getFiles()) {
            Files.writeString(Path.of(file.getPath()), file.getValue());
        }

        Files.deleteIfExists(container);
    }
}
