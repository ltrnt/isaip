package ru.filippovmv.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.filippovmv.task1.datamodel.FileH;
import ru.filippovmv.task1.datamodel.FolderH;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class HashChecker {

    private static final String TARGET_DEFAULT_CATALOG_PATH = "src/test/resources/task1/targetFolder"; // TODO Fill default path
    private static final String DEFAULT_HASH_PATH = "src/test/resources/task1/hash"; // TODO Fill default path
    private static final String ENCODING = "KOI8-R"; // TODO Fill default path

    public static void calculateHashAndWriteToFile(String targetPath, String hashPath) {
        Set<FileH> files = new HashSet<>();
        recursiveHashCalculation(
                new File(targetPath == null ? TARGET_DEFAULT_CATALOG_PATH : targetPath),
                files
        );

        if (!files.isEmpty()) {
            writeToFile(files, hashPath);
        }
    }

    public static void checkHash(String targetPath, String hashPath) {
        Set<FileH> olderFilesH = readFromFile(hashPath).getFiles();
        Set<FileH> currentFilesH = new HashSet<>();

        recursiveHashCalculation(
                new File(targetPath == null ? TARGET_DEFAULT_CATALOG_PATH : targetPath),
                currentFilesH
        );

        Boolean hasDifference = false;

        for (FileH file : olderFilesH) {
            if (!currentFilesH.contains(file)) {
                currentFilesH.remove(file);
                System.out.println("File " + file + " is different");
                hasDifference = true;
            }
        }

        if (!hasDifference) {
            System.out.println("No differences in folder");
        }
    }

    private static void writeToFile(Set<FileH> files, String hashPath) {
        File hashFile = new File(hashPath == null ? DEFAULT_HASH_PATH : hashPath);

        if (hashFile.exists()) {
            System.out.println("Rewriting existing Hash File");
        }

        try (FileWriter fileWriter = new FileWriter(hashFile, false)) {
            fileWriter.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new FolderH(files)));
            System.out.println("Hash File successfully created (Path: " + hashFile.getPath() + ")");
        } catch (IOException e) {
            System.out.println("Error while writing to File");
            throw new RuntimeException(e);
        }
    }

    private static FolderH readFromFile(String hashPath) {
        File hashFile = new File(hashPath == null ? DEFAULT_HASH_PATH : hashPath);
        String plainString;
        FolderH folderH;


        if (!hashFile.exists()) {
            System.out.println("Hash File not exist");
            throw new RuntimeException();
        }

        try {
            plainString = Files.readString(hashFile.toPath(), Charset.forName(ENCODING));
        } catch (IOException e) {
            System.out.println("Error while reading Hash File");
            throw new RuntimeException(e);
        }

        if (plainString == null || plainString.isEmpty() || plainString.isBlank()) {
            System.out.println("Hash File is empty");
            throw new RuntimeException();
        }

        try {
            folderH = new ObjectMapper().readValue(hashFile, FolderH.class);
        } catch (IOException e) {
            System.out.println("Error while deserializing Hash File");
            throw new RuntimeException(e);
        }

        return folderH;
    }

    private static Integer hashOf(File target) {
        int hash = 0;
        byte[] file = new byte[0];

        try {
            file = Files.readAllBytes(target.toPath());
        } catch (IOException e) {
            System.out.println("Error while reading bytes from File: " + target.getName());
            throw new RuntimeException(e);
        }

        for (int i = 0; i < file.length; i += 2) {
            int local = (file[i] << 8) + (i + 1 < file.length ? file[i + 1] : 0);
            hash ^= local;
        }

        return hash;
    }

    private static void recursiveHashCalculation(File target, Set<FileH> files) {
        File[] innerFiles = target.listFiles();

        if (innerFiles != null) {
            for (File innerFile : innerFiles) {
                if (innerFile.isDirectory()) {
                    recursiveHashCalculation(innerFile, files);
                } else {
                    files.add(new FileH(
                            innerFile.getPath(),
                            hashOf(innerFile)
                    ));
                }
            }
        }
    }

}
