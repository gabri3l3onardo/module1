package com.epam.ld.module1.testing;

import com.epam.ld.module1.testing.mode.ConsoleInputData;
import com.epam.ld.module1.testing.mode.ConsoleOutputData;
import com.epam.ld.module1.testing.mode.FileInputData;
import com.epam.ld.module1.testing.mode.FileOutputData;
import com.epam.ld.module1.testing.template.Template;
import com.epam.ld.module1.testing.template.TemplateEngine;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessengerTest {

    @TempDir
    File tempOutputFile;

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("console")
    public @interface ConsoleMode {
    }

    // Assuming a specific behavior on Windows for files management...
    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @EnabledOnOs(OS.WINDOWS)
    @Tag("file")
    public @interface FileMode {
    }

    @ConsoleMode
    @Tag("integrationTest")
    @Test
    public void testInputConsoleModeWithMockup() {
        // Data comes from console
        ConsoleInputData consoleInputMockup = mock(ConsoleInputData.class);
        when(consoleInputMockup.getExpression())
                .thenReturn(new String[]{"#{first}","One"})
                .thenReturn(new String[]{"#{second}","Two"});

        String messageExpected = "Template for testing: using One and then Two";
        Template template = new Template("Template for testing: using #{first} and then #{second}","#{first}","#{second}");

        Map<String, String> values = new HashMap<>();
        //First values
        String[] firstValue = consoleInputMockup.getExpression();
        values.put(firstValue[0],firstValue[1]);
        //Second value
        String[] secondValue = consoleInputMockup.getExpression();
        values.put(secondValue[0],secondValue[1]);

        TemplateEngine templateEngine = new TemplateEngine();

        String generatedMessage = templateEngine.generateMessage(template, values);
        assertEquals(messageExpected, generatedMessage, "Message generated is not as the expected");
    }

    @FileMode
    @Tag("integrationTest")
    @Test
    public void testInputFileModeWithSpy() {
        // Data comes from file
        FileInputData fileInputSpy = spy(FileInputData.class);
        doReturn(new String[]{"#{first},One","#{second},Two"}).when(fileInputSpy).getLines();

        String messageExpected = "Template for testing: using One and then Two";
        Template template = new Template("Template for testing: using #{first} and then #{second}","#{first}","#{second}");

        fileInputSpy.readLines();

        Map<String, String> values = new HashMap<>();
        //First values
        String[] firstValue = fileInputSpy.getExpression();
        values.put(firstValue[0],firstValue[1]);
        //Second value
        String[] secondValue = fileInputSpy.getExpression();
        values.put(secondValue[0],secondValue[1]);

        TemplateEngine templateEngine = new TemplateEngine();

        String generatedMessage = templateEngine.generateMessage(template, values);
        assertEquals(messageExpected, generatedMessage, "Message generated is not as the expected");

    }

    @FileMode
    @Tag("unitTest")
    @Test
    public void testOutputFileModeTempFile() {
        // Data goes to file
        FileOutputData fileOutput = new FileOutputData();
        fileOutput.setOutputFile(new File(tempOutputFile, "output.txt"));

        String messageReceived = "Template for testing: using One and then Two";

        assertDoesNotThrow(()->fileOutput.printOutputData(messageReceived));
        assertAll(
                ()->assertTrue(Files.exists(tempOutputFile.toPath()),"Output file is not in path"),
                ()->assertLinesMatch(Arrays.asList(messageReceived),Files.readAllLines(fileOutput.getOutputFile().toPath()))
        );

    }

    @ConsoleMode
    @Tag("unitTest")
    @Test
    public void testOutputConsoleModeWithMock() throws IOException {
        String[] messagesReceived = new String[]{
                "Template for testing: using One and then Two",
                "Template for testing: using Three and then Four"
        };

        // Data goes to console
        ConsoleOutputData consoleOutput = mock(ConsoleOutputData.class);
        doCallRealMethod().when(consoleOutput).printOutputLines(messagesReceived);

        assertDoesNotThrow(()->consoleOutput.printOutputLines(messagesReceived));
        verify(consoleOutput,times(2)).printOutputData(anyString());

    }
}
