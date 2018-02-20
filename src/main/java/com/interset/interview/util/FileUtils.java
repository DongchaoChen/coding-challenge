package com.interset.interview.util;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File utilities
 *
 * @author Dongchao Chen
 */
public class FileUtils {

    // Used to save temp files
    private static final String tempDir = System.getProperty("java.io.tmpdir");

    /**
     * private constructor
     */
    private FileUtils() {}

    /**
     * @param file abstract file pathname
     * @return file size in bytes
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long length = file.length();
        return length;
    }

    /**
     * @param file abstract file pathname
     * @return file extension in lower case
     */
    public static String getFileExtension(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex != -1 && lastIndex != 0)
            return fileName.substring(lastIndex + 1).toLowerCase();
        else return "";
    }

    /**
     * @param file abstract file pathname
     * @return fileType
     */
    public static String getFileType(File file) {
        String fileExtension = getFileExtension(file);
        if (fileExtension.equals("gz")) {
            String fileName = file.getName();
            if (fileName.endsWith("csv.gz")) {
                return "csv.gz";
            }
            if (fileName.endsWith("json.gz")) {
                return "json.gz";
            }
        }
        return fileExtension;
    }

    /**
     * @param file gzip file
     * @return return unzipped file
     */
    public static File decompressGZIP(File file) {
        String fileName = file.getName();
        String decompreddFileName = fileName.substring(0, fileName.lastIndexOf("."));
        File decompressedFile = new File(tempDir + decompreddFileName);

        try (GzipCompressorInputStream in = new GzipCompressorInputStream(new FileInputStream(file))) {
            IOUtils.copy(in, new FileOutputStream(decompressedFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return decompressedFile;
    }
}
