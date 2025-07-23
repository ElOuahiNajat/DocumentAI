package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");

    public String store(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + getFileExtension(file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new DocumentStorageException("invalid.file.error");
        }

        Path destinationFile = uploadDir
                .resolve(Paths.get(fileName))
                .normalize()
                .toAbsolutePath();

        if (!destinationFile.getParent().equals(uploadDir.toAbsolutePath())) {
            throw new DocumentStorageNotPermitedException();
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile);
        }

        return uploadDir.resolve(fileName).toString();
    }

    public String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex);
    }

    public int getFileSizeByKo(MultipartFile file) {
        return (int) (file.getSize() / 1024);
    }
}
