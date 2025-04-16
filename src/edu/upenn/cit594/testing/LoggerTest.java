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
        String testLogFile = "/Users/edwardfu/Documents/School/CIT594/CIT5940-Final-Project/log.txt";
        Files.deleteIfExists(new File(testLogFile).toPath());

        Logger logger = Logger.getInstance();
        logger.setOutput(testLogFile);
        logger.log("TEST_EVENT_1");

        List<String> lines = Files.readAllLines(new File(testLogFile).toPath());
        assertTrue(lines.get(0).contains("TEST_EVENT_1"), "Log should contain the test event");
    }

    @Test
    // I dont think by passing in null will make it fall back to stderr. I created another test below
    // Not sure if theres a way to handle null right now
    public void testDefaultToSystemErr() {
        Logger logger = Logger.getInstance();
        logger.setOutput(null);
        assertDoesNotThrow(() -> logger.log("This should go to stderr"));
    }

    @Test
    public void testDefaultToSystemErr2() {
        Logger logger = Logger.getInstance();
       String invalidFileLocation = "something/that/does/not/exist.txt";
        assertDoesNotThrow(() -> logger.log("This should go to stderr"));
    }
}
