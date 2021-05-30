package com.epam.ld.module1.testing.template;

import com.epam.ld.module1.testing.Client;
import com.epam.ld.module1.testing.template.Template;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TemplateEngineTest {

    TemplateEngine templateEngine;
    Template template;
    Client client;

    @ParameterizedTest
    //CSV columns: #{male}, #{female}, expected message
    @CsvSource({
            "JOHN, JANE, 'This is a test message for JOHN from JANE. Thanks in advance'",
            "PETER, KATRINA, 'This is a test message for PETER from KATRINA. Thanks in advance'"
    })
    public void testPlaceholdersReplacementWithValuesNoTemplatesSpecialChars(String maleValue, String femaleValue, String messageExpected) {
        template = new Template("This is a test message for #{male} from #{female}. Thanks in advance");
        templateEngine = new TemplateEngine();

        Map<String, String> values = new HashMap<>();
        values.put("#{male}",maleValue);
        values.put("#{female}",femaleValue);

        String generatedMessage = templateEngine.generateMessage(template, values);
        assertEquals(messageExpected, generatedMessage, "Message generated is not as the expected");
    }

}
