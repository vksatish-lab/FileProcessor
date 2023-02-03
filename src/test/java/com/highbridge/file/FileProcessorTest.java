package com.highbridge.file;

import com.highbridge.file.domain.Row;
import com.highbridge.file.util.UUIDGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileProcessorTest {

    private UUIDGenerator uuidGenerator = Mockito.mock(UUIDGenerator.class);
    private FileProcessor fileProcessor = new FileProcessor(uuidGenerator);

    @BeforeAll
    static void setup() throws IOException {
        File output = new File("src/test/resources/output_UUID.txt");
        Files.deleteIfExists(output.toPath());
    }

    // Java doc
    @Test
    public void testReadFile_whenAllLinesAreGood() throws IOException {
        List<Row> expectedList = new ArrayList<>();
        expectedList.add(new Row(1, 2));
        expectedList.add(new Row(2, 3));
        expectedList.add(new Row(4, 5));
        List<Row> resultList = fileProcessor.readFile("src/test/resources/input.txt");
        Assertions.assertIterableEquals(resultList, expectedList);
    }

    @Test
    public void testReadFile_withInvalidRow_1() throws IOException {
        List<Row> expectedList = new ArrayList<>();
        expectedList.add(new Row(2, 3));
        expectedList.add(new Row(4, 5));
        List<Row> resultList = fileProcessor.readFile("src/test/resources/input_warning_1.txt");
        Assertions.assertIterableEquals(resultList, expectedList);
    }

    @Test
    public void testReadFile_withInvalidRow_2() throws IOException {
        List<Row> expectedList = new ArrayList<>();
        expectedList.add(new Row(4, 5));
        expectedList.add(new Row(6, 7));
        List<Row> resultList = fileProcessor.readFile("src/test/resources/input_warning_2.txt");
        Assertions.assertIterableEquals(resultList, expectedList);
    }

    @Test
    public void testReadFile_withNegativeFields() throws IOException {
        List<Row> expectedList = new ArrayList<>();
        expectedList.add(new Row(-1, 2));
        expectedList.add(new Row(2, -33));
        expectedList.add(new Row(4, 5));
        List<Row> resultList = fileProcessor.readFile("src/test/resources/input_with_negatives.txt");
        Assertions.assertIterableEquals(resultList, expectedList);
    }

    @Test
    public void testReadFile_withInvalidNumberInRow_1() throws IOException {
        List<Row> expectedList = new ArrayList<>();  // extract out data setup.
        expectedList.add(new Row(1, 2));
        expectedList.add(new Row(2, 4));
        expectedList.add(new Row(3, 6));
        expectedList.add(new Row(4, 8));
        expectedList.add(new Row(5, 2));
        expectedList.add(new Row(6, 4));
        expectedList.add(new Row(7, 6));
        expectedList.add(new Row(8, 8));
        expectedList.add(new Row(9, 2));
        expectedList.add(new Row(10, 4));
        List<Row> resultList = fileProcessor.readFile("src/test/resources/input_warning_number_format_error.txt");  // extract out the variable
        Assertions.assertIterableEquals(resultList, expectedList);
    }

    @Test
    public void testReadFile_FileNotFoundException() throws IOException {
        Assertions.assertThrows(FileNotFoundException.class, () -> fileProcessor.readFile("src/test/resources/input2.txt"));
    }

    @Test
    public void testFileCreation() throws IOException {
        Mockito.when(uuidGenerator.getUUID()).thenReturn("test");
        List<Row> expectedList = new ArrayList<>();
        expectedList.add(new Row(1, 2));
        expectedList.add(new Row(6, 7));
        expectedList.add(new Row(6, 7));
        expectedList.add(new Row(6, 7));
        fileProcessor.writeFile(expectedList);
        File fileExpected = new File("src/test/resources/expected_result.txt");
        File output = new File("src/test/resources/output_test.txt");
        assertTrue(FileUtils.contentEquals(fileExpected, output));
    }

    /**
     * This is using test data provided from the exercise.
     */
    @Test
    public void testReadFollowedByWrite() throws IOException {
        Mockito.when(uuidGenerator.getUUID()).thenReturn("UUID");
        // Read the file
        List<Row> resultList = fileProcessor.readFile("src/test/resources/exercise_test_data.txt");
        // Now write to o/p file
        fileProcessor.writeFile(resultList);
        // verify generated is same as expected
        File fileExpected = new File("src/test/resources/expected_output_UUID.txt");
        File output = new File("src/test/resources/output_UUID.txt");
        assertTrue(FileUtils.contentEquals(fileExpected, output));
    }

}