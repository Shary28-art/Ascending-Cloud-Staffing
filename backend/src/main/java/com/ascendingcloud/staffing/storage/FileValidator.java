package com.ascendingcloud.staffing.storage;

import com.ascendingcloud.staffing.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Validates uploads against the requirements document:
 * resumes - PDF/DOC/DOCX up to 5 MB; logos - PNG/JPG/JPEG/WebP up to 2 MB.
 * The file's real bytes (magic numbers) are checked, not just its name.
 */
public final class FileValidator {

    public static final long MAX_RESUME_BYTES = 5L * 1024 * 1024;
    public static final long MAX_LOGO_BYTES = 2L * 1024 * 1024;

    /** extension -> content type */
    private static final Map<String, String> RESUME_TYPES = Map.of(
            "pdf", "application/pdf",
            "doc", "application/msword",
            "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private static final Map<String, String> LOGO_TYPES = Map.of(
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "webp", "image/webp");

    private FileValidator() {
    }

    public static String extensionOf(MultipartFile file) {
        String name = file.getOriginalFilename();

        if (name == null || !name.contains(".")) {
            throw new BadRequestException("File must have an extension");
        }

        return name.substring(name.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    /** Returns the validated extension. */
    public static String validateResume(MultipartFile file) {
        requireNotEmpty(file, MAX_RESUME_BYTES, "Resume", "5 MB");

        String ext = extensionOf(file);

        if (!RESUME_TYPES.containsKey(ext)) {
            throw new BadRequestException("Resume must be a PDF, DOC or DOCX file");
        }

        byte[] head = readHead(file, 8);

        boolean ok = switch (ext) {
            case "pdf" -> startsWith(head, '%', 'P', 'D', 'F');
            case "docx" -> startsWith(head, 'P', 'K');
            case "doc" -> head.length >= 4
                    && (head[0] & 0xFF) == 0xD0 && (head[1] & 0xFF) == 0xCF
                    && (head[2] & 0xFF) == 0x11 && (head[3] & 0xFF) == 0xE0;
            default -> false;
        };

        if (!ok) {
            throw new BadRequestException(
                    "File content does not match its extension");
        }

        return ext;
    }

    public static String validateLogo(MultipartFile file) {
        requireNotEmpty(file, MAX_LOGO_BYTES, "Logo", "2 MB");

        String ext = extensionOf(file);

        if (!LOGO_TYPES.containsKey(ext)) {
            throw new BadRequestException("Logo must be a PNG, JPG, JPEG or WebP image");
        }

        byte[] head = readHead(file, 12);

        boolean ok = switch (ext) {
            case "png" -> head.length >= 4
                    && (head[0] & 0xFF) == 0x89 && head[1] == 'P'
                    && head[2] == 'N' && head[3] == 'G';
            case "jpg", "jpeg" -> head.length >= 3
                    && (head[0] & 0xFF) == 0xFF && (head[1] & 0xFF) == 0xD8
                    && (head[2] & 0xFF) == 0xFF;
            case "webp" -> head.length >= 12
                    && startsWith(head, 'R', 'I', 'F', 'F')
                    && head[8] == 'W' && head[9] == 'E'
                    && head[10] == 'B' && head[11] == 'P';
            default -> false;
        };

        if (!ok) {
            throw new BadRequestException(
                    "File content does not match its extension");
        }

        return ext;
    }

    public static String resumeContentType(String ext) {
        return RESUME_TYPES.get(ext);
    }

    public static String logoContentType(String ext) {
        return LOGO_TYPES.get(ext);
    }

    /** Server-generated name; the client's file name is never used on disk. */
    public static String secureKey(String folder, String ext) {
        return folder + "/" + UUID.randomUUID() + "." + ext;
    }

    private static void requireNotEmpty(
            MultipartFile file, long max, String label, String maxLabel) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException(label + " file is required");
        }

        if (file.getSize() > max) {
            throw new BadRequestException(label + " must be " + maxLabel + " or smaller");
        }
    }

    private static byte[] readHead(MultipartFile file, int n) {
        try (var in = file.getInputStream()) {
            return in.readNBytes(n);
        } catch (IOException e) {
            throw new BadRequestException("Could not read uploaded file");
        }
    }

    private static boolean startsWith(byte[] data, char... expected) {
        if (data.length < expected.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (data[i] != (byte) expected[i]) {
                return false;
            }
        }

        return true;
    }
}
