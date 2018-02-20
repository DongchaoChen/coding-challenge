package com.interset.interview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.interset.interview.model.People;
import com.interset.interview.util.FileUtils;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryData {

    private String fileType;
    private File file;
    private int records;
    private int numOfSiblings;
    private Map<String, Integer> monthsBirthMap;
    private Map<String, Integer> foodMap;

    /**
     * @param fileType the file type e.g. csv, json, csv.gz, json.gz
     * @param file     the file for process
     */
    public SummaryData(String fileType, File file) {
        this.fileType = fileType;
        this.file = file;
    }

    /**
     * reset all private parameters before process files
     */
    private void reset() {
        records = 0;
        numOfSiblings = 0;
        monthsBirthMap = new HashMap<>();
        foodMap = new HashMap<>();
    }

    /**
     * the general method to process file based on type
     */
    public void generateReport() {
        reset(); // reset all parameters

        switch (fileType) {
            case "csv":
                processCSVFile(file);
                break;
            case "json":
                processJSONFile(file);
                break;
            case "csv.gz":
                processCSVGZFile(file);
                break;
            case "json.gz":
                processJSONGZFile(file);
                break;
            default:
                System.out.println("Unsupported file");
                break;
        }

        // Sample: Average siblings: 2
        System.out.println("Average siblings: " + getAverageSiblings(numOfSiblings, records));

        // Sample: Three favourite foods: pizza (74), Meatballs (36), Ice Cream (33)
        System.out.print("Three favourite foods: ");
        foodMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .forEach(entry -> System.out.print(entry.getKey() + "(" + entry.getValue() + ") "))
        ;
        // Sample: Birth Months: January (654), February (45), March (38), April (28), May (11), June (16), July (13), August (7), September (32), October (5), November (30), December (31)
        System.out.println("\nBirth Months: " + "January (" + monthsBirthMap.get("Jan") + "), "
                + "February (" + monthsBirthMap.get("Feb") + "), "
                + "March (" + monthsBirthMap.get("Mar") + "), "
                + "April (" + monthsBirthMap.get("Apr") + "), "
                + "May (" + monthsBirthMap.get("May") + "), "
                + "June (" + monthsBirthMap.get("Jun") + "), "
                + "July (" + monthsBirthMap.get("Jul") + "), "
                + "August (" + monthsBirthMap.get("Aug") + "), "
                + "September (" + monthsBirthMap.get("Sep") + "), "
                + "October (" + monthsBirthMap.get("Oct") + "), "
                + "November (" + monthsBirthMap.get("Nov") + "), "
                + "December (" + monthsBirthMap.get("Dec") + ")");
    }

    /**
     * @param sumOfSiblings sum of siblings
     * @param total         total people
     * @return round up value for average siblings
     */
    private int getAverageSiblings(int sumOfSiblings, int total) {
        return (int) Math.round(((double) sumOfSiblings) / (total));
    }

    /**
     * @param birth_timezone  birth timezone
     * @param birth_timestamp birth timestamp
     */
    private void updateBirthMonths(String birth_timezone, String birth_timestamp) {
        // If the zone ID starts with '+' or '-', the ID is parsed as a ZoneOffset using ZoneOffset.of(String).
        // See https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html
        ZoneId zoneId = ZoneId.of(birth_timezone);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(birth_timestamp)), zoneId);
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        // Directly output month so that we can use month as Key in the Map
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");
        String month = localDateTime.format(formatter);
        if (monthsBirthMap.containsKey(month)) {
            monthsBirthMap.put(month, monthsBirthMap.get(month) + 1);
        } else {
            monthsBirthMap.put(month, 1);
        }
    }

    /**
     * update food preferred in Map
     *
     * @param foodName the food name
     */
    private void updateFoodMap(String foodName) {
        if (foodMap.containsKey(foodName)) {
            foodMap.put(foodName, foodMap.get(foodName) + 1);
        } else {
            foodMap.put(foodName, 1);
        }
    }

    /**
     * process CSV file
     *
     * @param file csv file
     */
    private void processCSVFile(File file) {
        System.out.println("processing csv file " + file.getName());

        // Read full CSV file with header at once (RFC standard format, UTF-8 encoded)
        // Reference https://github.com/osiegmar/FastCSV
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        try {
            CsvContainer csv = csvReader.read(file, StandardCharsets.UTF_8);
            for (CsvRow row : csv.getRows()) {
                numOfSiblings += Integer.parseInt(row.getField("siblings"));
                updateBirthMonths(row.getField("birth_timezone"), row.getField("birth_timestamp"));
                updateFoodMap(row.getField("favourite_food"));
            }
            records = csv.getRows().size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * process JSON file
     *
     * @param file json file
     */
    private void processJSONFile(File file) {
        System.out.println("processing json file " + file.getName());

        // Using jackson
        processJsonUsingJackson(file);

        // Using gson stream
        // processJsonUsingGson(file);
    }

    /**
     * process JSON file using Jackson ObjectMapper, read in-memory
     *
     * @param file json file
     */
    private void processJsonUsingJackson(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<People> peopleList = objectMapper.readValue(
                    file,
                    objectMapper.getTypeFactory().constructCollectionType(
                            List.class, People.class));

            peopleList.stream().forEach((people -> {
                numOfSiblings += people.getSiblings();
                updateBirthMonths(people.getBirth_timezone(), people.getBirth_timestamp());
                updateFoodMap(people.getFavourite_food());
            }
            ));
            records = peopleList.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * process JSON file using Gson Stream mode
     *
     * @param file json file
     */
    private void processJsonUsingGson(File file) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            Gson gson = new GsonBuilder().create();

            // Read file in stream mode
            reader.beginArray();
            while (reader.hasNext()) {
                // Read data into object model
                People people = gson.fromJson(reader, People.class);
                numOfSiblings += people.getSiblings();
                updateBirthMonths(people.getBirth_timezone(), people.getBirth_timestamp());
                updateFoodMap(people.getFavourite_food());
                records++;
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * process CSV gzip file
     *
     * @param file csv gzip file
     */
    private void processCSVGZFile(File file) {
        System.out.println("processing csv.gz file " + file.getName());
        File csvFile = FileUtils.decompressGZIP(file);
        processCSVFile(csvFile);
        if (csvFile.delete()) {
            System.out.println("Deleted file " + csvFile.getName() + " after processing. ");
        } else {
            System.out.println("Failed to delete the file " + csvFile.getName());
        }
    }

    /**
     * process JSON gzip file
     *
     * @param file json gzip file
     */
    private void processJSONGZFile(File file) {
        System.out.println("processing json.gz file " + file.getName());
        File jsonFile = FileUtils.decompressGZIP(file);
        processJSONFile(jsonFile);
        if (jsonFile.delete()) {
            System.out.println("Deleted file " + jsonFile.getName() + " after processing. ");
        } else {
            System.out.println("Failed to delete the file " + jsonFile.getName());
        }
    }
}
