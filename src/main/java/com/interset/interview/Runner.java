package com.interset.interview;

import com.interset.interview.service.SummaryData;
import com.interset.interview.util.FileUtils;

import java.io.File;
import java.util.Arrays;

public class Runner {

    private static final String[] supportFiles = {"csv", "json", "csv.gz", "json.gz"};

    /**
     * This is main method which is starting point for this application.
     * It requires 1 arguments to run successfully.
     *
     * @param: args[0] : Path to JSON or CSV file to read.
     * <p>
     * The JSON and CSV files must contain the following fields:
     * name, siblings, favourite_food, birth_timezone, birth_timestamp
     * <p>
     * This application parses the files and prints out the following information:
     * - Average number of siblings (round up)
     * - Top 3 favourite foods
     * - How many people were born in each month of the year (uses the month of each person's respective timezone of birth)
     */
    public static void main(String args[]) {

        if (args.length != 1) {
            System.out.println("We currently only expect 1 argument! A path to a JSON or CSV file to read.");
            System.exit(1);
        }

        // System.out.println("Do cool stuff here!!");
        File file = new File(args[0]);
        try {
            String fileType = FileUtils.getFileType(file);

            // exit when file not exist or not json / csv / json.gz / csv.gz
            if (fileType == null || !Arrays.asList(supportFiles).contains(fileType)) {
                System.out.println("We currently only support csv / json / json.gz / csv.gz file");
                System.exit(1);
            }
            SummaryData summaryData = new SummaryData(fileType, file);
            summaryData.generateReport();

            System.out.println("The End of Application, Thanks!");
        } catch (NullPointerException e) {
            System.out.println("Error: File does not exist, exit application");
            System.exit(1);
        }
    }
}
