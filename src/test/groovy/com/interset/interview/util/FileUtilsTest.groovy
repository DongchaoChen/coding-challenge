package com.interset.interview.util

import spock.lang.Shared
import spock.lang.Specification

class FileUtilsTest extends Specification {
    @Shared
    File file
    @Shared
    long fileSize
    @Shared
    String fileExtenion
    @Shared
    String fileType

    def "getFileSize returns 0 when file is null"() {
        given:
        file = null;
        when:
        fileSize = FileUtils.getFileSize(file);
        then:
        fileSize == 0
    }

    def "getFileSize returns 0 when file not exist"() {
        given:
        file = new File("src/test/resources/notExist.jpg");
        when:
        fileSize = FileUtils.getFileSize(file);
        then:
        fileSize == 0
    }

    def "getFileSize returns 0 for empty json file"() {
        given:
        file = new File("src/test/resources/empty_sample.json");
        when:
        fileSize = FileUtils.getFileSize(file);
        then:
        fileSize == 0
    }

    def "getFileSize returns 0 for empty csv file"() {
        given:
        file = new File("src/test/resources/empty_sample.csv");
        when:
        fileSize = FileUtils.getFileSize(file);
        then:
        fileSize == 0
    }

    def "getFileSize returns file size in bytes"() {
        given:
        file = new File("src/test/resources/population_sample.json");
        long expectedSize = 162242;
        when:
        fileSize = FileUtils.getFileSize(file);
        then:
        fileSize == expectedSize
    }

    def "getFileExtension returns null when file is null"() {
        given:
        file = null;
        when:
        fileExtenion = FileUtils.getFileExtension(file);
        then:
        fileExtenion == null
    }

    def "getFileExtension returns null when file not exist"() {
        given:
        file = new File("src/test/resources/notExist.jpg");
        when:
        fileExtenion = FileUtils.getFileExtension(file);
        then:
        fileExtenion == null
    }

    def "getFileExtension returns lowercase file extension"() {
        when:
        fileExtenion = FileUtils.getFileExtension(file);
        then:
        fileExtenion == expectedFileExtension
        where:
        file                                     | expectedFileExtension
        new File("src/test/resources/file1.csv") | "csv"
        new File("src/test/resources/file2.CSV") | "csv"
    }

    def "getFileExtension return empty string when file has without extension"() {
        given:
        file = new File("src/test/resources/file3");
        when:
        fileExtenion = FileUtils.getFileExtension(file);
        then:
        fileExtenion == ""
    }

    def "getFileType returns file-test.txt as txt file"() {
        given:
        file = new File("src/test/resources/test.txt");
        when:
        fileType = FileUtils.getFileType(file);
        then:
        fileType == "txt"
    }

    def "getFileType returns population_sample.csv as csv file"() {
        given:
        file = new File("src/test/resources/population_sample.csv");
        when:
        fileType = FileUtils.getFileType(file);
        then:
        fileType == "csv"
    }

    def "getFileType returns population_sample.json as json file"() {
        given:
        file = new File("src/test/resources/population_sample.json");
        when:
        fileType = FileUtils.getFileType(file);
        then:
        fileType == "json"
    }

    def "getFileType returns population_large.json.gz as json.gz file"() {
        given:
        file = new File("src/main/resources/population_large.json.gz");
        when:
        fileType = FileUtils.getFileType(file);
        then:
        fileType == "json.gz"
    }

    def "getFileType returns population_large.csv.gz as csv.gz file"() {
        given:
        file = new File("src/main/resources/population_large.csv.gz");
        when:
        fileType = FileUtils.getFileType(file);
        then:
        fileType == "csv.gz"
    }

    def "decompressGZIP unzip population_large.json.gz success"() {
        File unzippedFile
        given:
        file = new File("src/main/resources/population_large.json.gz");
        when:
        unzippedFile = FileUtils.decompressGZIP(file);
        then:
        unzippedFile.exists()
        unzippedFile.deleteOnExit()
    }
}
