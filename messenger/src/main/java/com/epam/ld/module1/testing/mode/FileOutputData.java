package com.epam.ld.module1.testing.mode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileOutputData implements OutputDestination{

    private File outputFile;

    @Override
    public void printOutputData(String data) throws IOException {
        Files.writeString(outputFile.toPath(),data);
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }
}
