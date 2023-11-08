package lynorg.filemngt.controller;

import lynorg.filemngt.model.FileFolderDto;
import lynorg.filemngt.model.FolderFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestMain {
    public static void main(String[] args) {

//        ArrayList<String> templates1 = findTheAbsolutePathOfFile("Templates", "1910-1910-Approve-F1-MULTIPLE.html");
//        System.out.println(templates1);

//        findAndCopyFile("Templates", "1910-1910-Approve-F1-MULTIPLE.html", "fileDestination", "sample.html");

//        String[] samplePath = {
//                "sampleFile": "/p1/p2/s.xyz",
//                "list"=[{"folder": "/1/n/", "file": "1.2"},{"folder": "/1/n/", "file": "1.2"}]
//        }

        FolderFile file1 = new FolderFile("DestTemplates/pdfTemplates/1920/","cat.html");
        FolderFile file2 = new FolderFile("DestTemplates/pdfTemplates/1930/","cat.html");
        FileFolderDto fileFolderDto = new FileFolderDto("Templates/pdfTemplates/1910/1910-Approve/1910-1910-Approve-F1-MULTIPLE.html",new ArrayList<>(List.of(file1,file2)));

        createFolderFileAndCopyPasteFileContent(fileFolderDto);


    }

    private static ArrayList<String> nameOfAllFiles(String rootFolder) {
        Path folder = Paths.get(rootFolder);
        ArrayList<String> fileList = new ArrayList<>();
        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.walk(folder).filter(Files::isRegularFile).forEach(file -> {
                    fileList.add(String.valueOf(file));
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fileList;
    }

    private static ArrayList<String> findTheAbsolutePathOfFile(String rootFolder, String fileName) {
        Path folder = Paths.get(rootFolder);
        ArrayList<String> fileList = new ArrayList<>();
        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.walk(folder).filter(Files::isRegularFile).forEach(file -> {
                    if (file.getFileName().toString().equalsIgnoreCase(fileName)) {
                        fileList.add(String.valueOf(file));
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return fileList;
    }

    private static void findAndCopyFile(String rootFolder, String fileName, String destinationFolder) {
        Path folder = Paths.get(rootFolder);
        Path destination = Paths.get(destinationFolder);

        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.walk(folder).filter(Files::isRegularFile).forEach(file -> {
                    if (file.getFileName().toString().equalsIgnoreCase(fileName)) {
                        Path targetFile = destination.resolve(file.getFileName());
                        System.out.println("Copy::" + file);
                        System.out.println("Paste::" + targetFile);
                        try {
                            Files.createDirectories(destination);
                            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("File copied successfully!");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Directory not found: " + rootFolder);
        }
    }

    private static void findAndCopyFile(String rootFolder, String fileName, String destinationFolder, String newFileName) {
        Path folder = Paths.get(rootFolder);
        Path destination = Paths.get(destinationFolder);

        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            System.out.println("Directory not found: " + rootFolder);
            return;
        }

        try {
            Files.walk(folder).filter(Files::isRegularFile).forEach(file -> {
                if (file.getFileName().toString().equalsIgnoreCase(fileName)) {
                    Path targetFile = getUniqueTargetPath(destination, newFileName);

                    System.out.println("Copy:: " + file);
                    System.out.println("Paste:: " + targetFile);

                    try {
                        Files.createDirectories(destination);
                        Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File copied successfully!");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static Path getUniqueTargetPath(Path destination, String newFileName) {
        int suffix = 1;
        Path targetFile = destination.resolve(newFileName);

        while (Files.exists(targetFile)) {
            String baseName = newFileName.substring(0, newFileName.lastIndexOf('.'));
            String extension = newFileName.substring(newFileName.lastIndexOf('.'));
            newFileName = baseName + suffix + extension;
            targetFile = destination.resolve(newFileName);
            suffix++;
        }

        return targetFile;
    }

    public void copyPaste(String rootFolder, String searchFileName, String[] folderFile) {
        Path folder = Paths.get(rootFolder);
        ArrayList<String> fileList = new ArrayList<>();
        try {
            Files.walk(folder).filter(Files::isRegularFile).forEach(file -> {
                if (file.getFileName().toString().equalsIgnoreCase(searchFileName)) {
                    fileList.add(String.valueOf(file));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (folderFile.length > 0) {
            Arrays.stream(folderFile).forEach(item -> {
                if (!Files.isRegularFile(Path.of(item))) {
                    try {
                        Files.createDirectories(Paths.get(item));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Files.createFile(Path.of(item));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

    }

    private static void createFolderFile(String[] folderFile) {
        if (folderFile != null && folderFile.length > 0) {
            for (String item : folderFile) {
                Path path = Paths.get(item);
                try {
                    if (!Files.exists(path)) {
                        if (item.endsWith("/")) {
                            // It's a directory
                            Files.createDirectories(path);
                            System.out.println("Created directory: " + path);
                        } else {
                            // It's a file, so create the parent directory if it doesn't exist
                            Path parentDirectory = path.getParent();
                            if (!Files.exists(parentDirectory)) {
                                Files.createDirectories(parentDirectory);
                            }
                            Files.createFile(path);
                            System.out.println("Created file: " + path);
                        }
                    } else {
                        System.out.println("Path already exists: " + path);
                    }
                } catch (IOException e) {
                    // Handle exceptions gracefully, e.g., log the error
                    System.err.println("Error creating " + (Files.isDirectory(path) ? "directory" : "file") + ": " + path);
                    e.printStackTrace();
                }
            }
        }
    }


    private static void createFolderFileAndCopyPasteFileContent(FileFolderDto fileFolderDto) {
        if (!fileFolderDto.getContentFileAbsolutPath().equalsIgnoreCase("") && fileFolderDto.getContentFileAbsolutPath() != null) {
            Path contentPath = Paths.get(fileFolderDto.getContentFileAbsolutPath());
            byte[] contentInBytes = null;
            if (Files.exists(contentPath)) {
                try {
                    contentInBytes = Files.readAllBytes(contentPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            ArrayList<FolderFile> listFolderFile = fileFolderDto.getListFolderFile();
            if (listFolderFile != null) {
                for (FolderFile singleFolderFile : listFolderFile) {
                    if (!singleFolderFile.getFolderPath().equalsIgnoreCase("") && singleFolderFile.getFolderPath() != null) {

                        Path path = Paths.get(singleFolderFile.getFolderPath());
                        if (!Files.exists(path)) {
                            if (singleFolderFile.getFolderPath().endsWith("/")) {
                                // It's a directory
                                try {
                                    Files.createDirectories(path);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("Created directory: " + path);
                            }
                        }else {
                            System.out.println("Path already exists: " + singleFolderFile.getFolderPath());
                        }

                        // paste the content
                        if (!singleFolderFile.getNewFileName().equalsIgnoreCase("" ) && singleFolderFile.getNewFileName() !=null){
                            String newFileName = singleFolderFile.getNewFileName();
                            Path parentDirectory = path.getParent();
                            if (!Files.exists(parentDirectory)) {
                                try {
                                    Files.createDirectories(parentDirectory);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            try {
                                System.out.println("Folder:: "+singleFolderFile.getFolderPath());
                                Path destinationFile =Paths.get( singleFolderFile.getFolderPath() + "/" + singleFolderFile.getNewFileName());
//                                Files.copy(Paths.get(fileFolderDto.getContentFileAbsolutPath()), destinationFile , StandardCopyOption.REPLACE_EXISTING);
                                Files.write(destinationFile, contentInBytes);
                                System.out.println("File content copy paste completed ✅");

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    } else {
                        System.out.println("Destination Folder can't be null");
                    }

                }

            } else {
                throw new RuntimeException("Destination can't be null");
            }
        } else {
            throw new RuntimeException("File Not Found!::" + fileFolderDto.getContentFileAbsolutPath());
        }
    }


}


