package com.epam.ld.module1.testing.mode;

public class FileInputData implements InputSource{

    private String[] lines;
    private int pointer;

    @Override
    public String[] getExpression() {
        String line = lines[pointer++];
        return line.split(",");
    }

    public void readLines(){
        lines = getLines();
        pointer = 0;
    }

    public String[] getLines() {
        return new String[0];
    }
}
