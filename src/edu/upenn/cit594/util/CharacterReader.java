package edu.upenn.cit594.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CharacterReader implements AutoCloseable {
    private BufferedReader reader;

    public CharacterReader(String filename) throws IOException {
        reader = new BufferedReader(new FileReader(filename));
    }

    public int read() throws IOException {
        return reader.read();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
