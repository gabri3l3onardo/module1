package com.epam.ld.module1.testing.mode;

import java.io.IOException;

public class ConsoleOutputData implements OutputDestination{

    @Override
    public void printOutputData(String data) throws IOException {
        // Not defined yet
    }

    /**
     * Prints all the lines in console (one by one)
     *
     * @param lines         Lines to print in console
     * @throws IOException  Exception if it's not possible to print
     */
    public void printOutputLines(String[] lines) throws IOException {
        for (String line:lines) {
            printOutputData(line);
        }
    }
}
