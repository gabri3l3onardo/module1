package com.epam.ld.module1.testing.mode;

import java.io.IOException;

public class ConsoleOutputData implements OutputDestination{

    @Override
    public void printOutputData(String data) throws IOException {
        // Not defined yet
    }

    public void printOutputLines(String[] lines) throws IOException {
        for (String line:lines) {
            printOutputData(line);
        }
    }
}
