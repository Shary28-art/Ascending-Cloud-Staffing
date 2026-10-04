package com.ascendingcloud.staffing.storage;

import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Stores files on local disk under app.storage.local-root.
 * Use a different root per environment (dev / staging / prod).
 */
@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorageService implements StorageService {

    private final Path root;

    public LocalFileStorageService(
            @Value("${app.storage.local-root:./uploads}") String root) {
        this.root = Path.of(root).toAbsolutePath().normalize();
    }

    @PostConstruct
    void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot create storage directory " + root, e);
        }
    }

    private Path resolve(String key) {
        Path target = root.resolve(key).normalize();

        // Block path traversal such as "../../etc/passwd"
        if (!target.startsWith(root)) {
            throw new BadRequestException("Invalid storage key");
        }

        return target;
    }

    @Override
    public void store(String key, InputStream data, long size) {
        Path target = resolve(key);

        try {
            Files.createDirectories(target.getParent());
            Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not store file", e);
        }
    }

    @Override
    public InputStream load(String key) {
        try {
            return Files.newInputStream(resolve(key));
        } catch (NoSuchFileException e) {
            throw new ResourceNotFoundException("File not found");
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read file", e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(resolve(key));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not delete file", e);
        }
    }
}
