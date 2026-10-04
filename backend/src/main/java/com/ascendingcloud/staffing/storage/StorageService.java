package com.ascendingcloud.staffing.storage;

import java.io.InputStream;

/**
 * File storage abstraction. Only the key (location) and metadata are kept in
 * the database; the bytes live here. LocalFileStorageService is used for
 * development. For production add an S3 or Azure Blob implementation of this
 * interface and select it with app.storage.type.
 */
public interface StorageService {

    /** Saves the bytes under the given key (e.g. "resumes/uuid.pdf"). */
    void store(String key, InputStream data, long size);

    InputStream load(String key);

    void delete(String key);
}
