package lynorg.filemngt.service;

import lynorg.filemngt.model.FileFolderDto;
import lynorg.filemngt.model.FolderFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class FileIO {
    public void stringReplaceFromFile(String folderPath, String originalString, String replaceString) {
        Path folder = Paths.get(folderPath);

        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.walk(folder)
                        .filter(Files::isRegularFile)
                        .forEach(filePath -> processFile(filePath, originalString, replaceString));
            } catch (IOException e) {
                throw new RuntimeException("Error processing files in the directory: " + folderPath, e);
            }
        } else {
            throw new IllegalArgumentException("The specified folder does not exist or is not a directory: " + folderPath);
        }
    }

    public void stringReplaceFromFile(String folderPath, String replaceString) {
        Path folder = Paths.get(folderPath);

        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.walk(folder)
                        .filter(Files::isRegularFile)
                        .forEach(filePath -> processFile(filePath, replaceString));
            } catch (IOException e) {
                throw new RuntimeException("Error processing files in the directory: " + folderPath, e);
            }
        } else {
            throw new IllegalArgumentException("The specified folder does not exist or is not a directory: " + folderPath);
        }
    }

    private void processFile(Path file, String originalString, String replaceString) {
        try {
            byte[] content = Files.readAllBytes(file);
            String contentStr = new String(content);
            if (contentStr.contains(originalString)) {
                contentStr = contentStr.replace(originalString, replaceString);
                Files.write(file, contentStr.getBytes());
                System.out.println("Replaced in file: " + file.getFileName());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + file.getFileName(), e);
        }
    }

    private void processFile(Path file, String replaceString) {
        try {
            byte[] content = Files.readAllBytes(file);
            String contentStr = new String(content);
            if (contentStr.contains("<!-- [|#|replacePattern|#|] START -->")) {
                String[] parts = contentStr.split("<!-- \\[\\|#\\|replacePattern\\|#\\|] START -->");
                String patternBeforePart = parts[0];
                String[] secondSplit = parts[1].split("<!-- \\[\\|#\\|replacePattern\\|#\\|] END -->");
                String patternAfterPart = secondSplit[1];

                String finalString = patternBeforePart + replaceString + patternAfterPart;
                Files.write(file, finalString.getBytes());
                System.out.println("Replaced in file: " + file.getFileName());
            } else {
                System.out.println("No pattern found");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + file.getFileName(), e);
        }
    }

    public void createFolderFileAndCopyPasteFileContent(FileFolderDto fileFolderDto) {
        String contentFileAbsolutPath = fileFolderDto.getContentFileAbsolutPath();

        if (StringUtils.isEmpty(contentFileAbsolutPath)) {
            throw new IllegalArgumentException("contentFileAbsolutPath cannot be empty or null");
        }

        Path contentPath = Paths.get(contentFileAbsolutPath);

        if (!Files.exists(contentPath)) {
            throw new IllegalArgumentException("File not found: " + contentFileAbsolutPath);
        }

        byte[] contentInBytes;
        try {
            contentInBytes = Files.readAllBytes(contentPath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file content", e);
        }

        List<FolderFile> listFolderFile = fileFolderDto.getListFolderFile();

        if (listFolderFile == null || listFolderFile.isEmpty()) {
            throw new IllegalArgumentException("Destination list cannot be null or empty");
        }

        for (FolderFile singleFolderFile : listFolderFile) {
            String destinationFolderPath = singleFolderFile.getFolderPath();

            if (StringUtils.isEmpty(destinationFolderPath)) {
                throw new IllegalArgumentException("Destination folder path cannot be empty or null");
            }

            Path destinationPath = Paths.get(destinationFolderPath);

            if (!Files.exists(destinationPath)) {
                try {
                    Files.createDirectories(destinationPath);
                } catch (IOException e) {
                    throw new RuntimeException("Error creating destination directory: " + destinationFolderPath, e);
                }
            }

            if (!StringUtils.isEmpty(singleFolderFile.getNewFileName())) {
                Path destinationFile = destinationPath.resolve(singleFolderFile.getNewFileName());

                try {
                    Files.write(destinationFile, contentInBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Error copying and pasting file content to: " + destinationFile, e);
                }
            }
        }
    }

    public List<String> findTheAbsolutePathOfFile(String rootFolder, String fileName) {
        if (!rootFolder.isEmpty()) {
            Path folder = Paths.get(rootFolder);
            List<String> fileList = new ArrayList<>();

            if (Files.exists(folder) && Files.isDirectory(folder)) {
                try {
                    Files.walk(folder)
                            .filter(Files::isRegularFile)
                            .filter(file -> file.getFileName().toString().equalsIgnoreCase(fileName))
                            .forEach(file -> fileList.add(String.valueOf(file.toAbsolutePath())));
                } catch (IOException e) {
                    throw new RuntimeException("Error searching for files", e);
                }
            }

            return fileList;
        }
        return new ArrayList<>();
    }
}
