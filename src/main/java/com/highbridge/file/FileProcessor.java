package com.highbridge.file;

import com.highbridge.file.domain.Row;
import com.highbridge.file.util.UUIDGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 General file read/write utilities for files following schema as of @Row objects.
 Methods  are provided in the following areas:
    writing to a file
    reading from a file
 * @author vksatish
 *
 */
public class FileProcessor {

    private static final Logger LOGGER = Logger.getLogger(FileProcessor.class.getName());
    private UUIDGenerator uuidGenerator;
    private static final String FILE_PREFIX = "src/test/resources/output_";
    private static final String FILE_EXTENSION = ".txt";
    private static final String DELIMITER = ",";

    public FileProcessor(UUIDGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    /**
     * Returns a list of row object for all the valid data rows read from input file.
     * @param fileName input fileName with file location
     * @return list of row object
     * @throws IOException
     */
    public List<Row> readFile(final String fileName) throws IOException {
        List<Row> resultList = new ArrayList<>();
        int lineCount = 0;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                convertStringToObject(line).ifPresent(row -> resultList.add(row));
                lineCount++;
            }
        } catch (FileNotFoundException fileNotFoundException) {
            LOGGER.log(Level.SEVERE, "Error accessing file!", fileNotFoundException);
            throw fileNotFoundException;
        }
        LOGGER.info("Finished reading file - " + fileName);
        LOGGER.info("Total Rows read - " + lineCount);
        LOGGER.info("Valid Rows - " + resultList.size());
        LOGGER.info("Invalid Rows - " + (lineCount - resultList.size()));
        return resultList;
    }

    private Optional<Row> convertStringToObject(String line) {
        List<String> subStrings = Arrays.stream(line.split(DELIMITER)).toList();
        int firstNumber, secondNumber;
        if (subStrings.size() != 2) {   // define constant
            LOGGER.log(Level.WARNING, String.format("Skipping file row=[%s], invalid row.", line));
            return Optional.empty();
        }
        try {
            firstNumber = Integer.parseInt(subStrings.get(0));
            secondNumber = Integer.parseInt(subStrings.get(1));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, String.format("Skipping file row=[%s],  error converting string to integer", line), e);// rename the exception variable
            return Optional.empty();
        }
        return Optional.of(new Row(firstNumber, secondNumber));/// try using a mapper
    }

    /**
     * Write list of objects to file in order as below -
     *  1. Last element in the list is written first to file
     *  2. On each row secondNumber of row object is followed by firstNumber
     * @param rowList  list of row objects to be written to file
     * @throws IOException
     */
    public void writeFile(final List<Row> rowList) throws IOException {
        final String uuid = uuidGenerator.getUUID();
        final String fileName = FILE_PREFIX + uuidGenerator.getUUID() + FILE_EXTENSION;
        File file = new File(fileName);
        try (FileWriter fw = new FileWriter(file)) {
            for (int i = rowList.size() - 1; i >= 0; i--) {
                fw.write(formatString(rowList.get(i)));
            }
            fw.flush();
        } catch (IOException ioException) {
            throw ioException;
        }
        LOGGER.info("Records successfully written to file - " + fileName);
    }

    private String formatString(Row row) {
        return String.format("%s,%s\n", Integer.toString(row.getSecondNumber()), Integer.toString(row.getFirstNumber())); // there are different new line  charachet
    }
}
