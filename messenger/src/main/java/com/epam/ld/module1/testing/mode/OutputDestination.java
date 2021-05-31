package com.epam.ld.module1.testing.mode;

import java.io.IOException;

public interface OutputDestination {
    void printOutputData(String data) throws IOException;
}
