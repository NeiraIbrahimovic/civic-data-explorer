
package edu.upenn.cit594.testing;

import edu.upenn.cit594.Main;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MainMenuTest {

    @Test
    public void testFullProgramMenuFlow() throws IOException {
        String input = String.join(System.lineSeparator(),
            "1",  //show available actions
            "2",  //total population
            "3", "partial", "2021-03-01",  //partial vacc per capita
            "4", "19104",  //avg market value
            "5", "19104",  //avg livable area
            "6", "19104",  //total market value per capita
            "7",          //additional feature
            "0"           //exit
        );

        //Redirect input and output
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream testInput = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        System.setIn(testInput);
        System.setOut(new PrintStream(testOutput));

        String[] args = {
        		//Update with your own file paths
            "--covid=../CIT5940_Final_Project_testFiles/src/edu/upenn/cit594/testing/covid_data.csv",
            "--population=../CIT5940_Final_Project_testFiles/src/edu/upenn/cit594/testing/population.csv",
            "--properties=../CIT5940_Final_Project_testFiles/src/edu/upenn/cit594/testing/properties.csv",
            "--log=../CIT5940_Final_Project_testFiles/src/edu/upenn/cit594/testing/test_log.txt"
        };

        //Run main
        Main.main(args);

        //Restore I/O
        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = testOutput.toString();

        //Basic output checks
        assertTrue(output.contains("BEGIN OUTPUT"), "Expected output section not found");
        assertTrue(output.contains("END OUTPUT"), "Expected output end not found");
        assertTrue(output.contains("19104"), "Expected ZIP code output");
    }
}
