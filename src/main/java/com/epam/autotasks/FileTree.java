package com.epam.autotasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class FileTree {


    public Optional<String> tree(final Path path) {
        if (path == null || !Files.exists(path)) {
            return Optional.empty();
        }
        if (Files.isRegularFile(path)) {
            try {
                long sizeFile = Files.size(path);
                return Optional.of(path.getFileName() + " " + sizeFile + " bytes");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Files.isDirectory(path)) {
            try {
                long dirSize = calculateDirectorySize(path);
                String header = path.getFileName() + " " + dirSize + " bytes";
                String tree = buildTree(path, "");
                return Optional.of(header + "\n" + tree);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    private long calculateDirectorySize(Path path) throws IOException {
        long size = 0;
        try (var stream = Files.list(path)) {
            for (Path child : stream.toList()) {
                if (Files.isRegularFile(child)) {
                    size += Files.size(child);
                } else if (Files.isDirectory(child)) {
                    size += calculateDirectorySize(child);
                }
            }
        }
        return size;
    }

    private String buildTree(Path path, String currentPrefix) throws IOException {
        List<Path> paths;
        try (var stream = Files.list(path)) {
            paths = stream.toList();
        }
        List<Path> directories = new ArrayList<>();
        List<Path> subFiles = new ArrayList<>();

        for (Path p : paths) {
            if (Files.isDirectory(p)) {
                directories.add(p);
            } else {
                subFiles.add(p);
            }
        }

        directories.sort(Comparator.comparing(p -> p.getFileName().toString(), String::compareToIgnoreCase));
        subFiles.sort(Comparator.comparing(p -> p.getFileName().toString(), String::compareToIgnoreCase));

        List<Path> allElements = new ArrayList<>();
        allElements.addAll(directories);
        allElements.addAll(subFiles);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < allElements.size(); i++) {
            Path element = allElements.get(i);
            boolean isLast = (i == allElements.size() - 1);
            String elementPrefix = isLast ? "└─ " : "├─ ";
            String childPrefix = isLast ? "   " : "│  ";

            if (Files.isDirectory(element)) {
                long dirSize = calculateDirectorySize(element);
                sb.append(currentPrefix).append(elementPrefix).append(element.getFileName())
                        .append(" ").append(dirSize).append(" bytes").append("\n");
                sb.append(buildTree(element, currentPrefix + childPrefix));
            } else {
                long sizeFile = Files.size(element);
                sb.append(currentPrefix).append(elementPrefix).append(element.getFileName())
                        .append(" ").append(sizeFile).append(" bytes").append("\n");
            }
        }

        return sb.toString();
    }
}
