package com.epam.ld.module1.testing.mode;

public class ConsoleInputData implements InputSource{
    @Override
    public String[] getExpression() {
        return new String[]{"#{placeholder}","value"};
    }
}
