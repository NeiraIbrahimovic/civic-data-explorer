package edu.upenn.cit594.testing;

import edu.upenn.cit594.logging.Logger;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

    @Test
    public void testSingletonInstance() {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        assertSame(logger1, logger2, "Logger should follow Singleton pattern");
    }

    @Test
    public void testLogFileWriting() throws IOException {
    	//Update with your own file path
        String testLogFile = "../CIT5940_Final_Project_testFiles/src/edu/upenn/cit594/testing/test_log.txt";
        Files.deleteIfExists(new File(testLogFile).toPath());

        Logger logger = Logger.getInstance();
        logger.setOutput(testLogFile);
        logger.log("TEST_EVENT_1");

        List<String> lines = Files.readAllLines(new File(testLogFile).toPath());
        assertTrue(lines.get(0).contains("TEST_EVENT_1"), "Log should contain the test event");
    }

    @Test
    public void testDefaultToSystemErr() {
        Logger logger = Logger.getInstance();
        logger.setOutput(null);
        assertDoesNotThrow(() -> logger.log("This should go to stderr"));
    }
}
