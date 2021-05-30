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
        template = new Template("This is a test message for #{male} from #{female}. Thanks in advance","#{male}","#{female}");
        templateEngine = new TemplateEngine();

        Map<String, String> values = new HashMap<>();
        values.put("#{male}",maleValue);
        values.put("#{female}",femaleValue);

        String generatedMessage = templateEngine.generateMessage(template, values);
        assertEquals(messageExpected, generatedMessage, "Message generated is not as the expected");
    }

    @ParameterizedTest
    //CSV columns: #{male}, #{female}
    @CsvSource({
            "JOHN, ",
            ", KATRINA"
    })
    public void testExceptionThrowForAtLeastOneValueNotProvided(String maleValue, String femaleValue) {
        template = new Template("This is a test message for #{male} from #{female}. Thanks in advance","#{male}","#{female}");
        templateEngine = new TemplateEngine();
        // When variables are not sent...
        Map<String, String> notNullvalues = new HashMap<>();
        if(maleValue!=null){
            notNullvalues.put("#{male}",maleValue);
        }
        if(femaleValue!=null){
            notNullvalues.put("#{female}",femaleValue);
        }

        Exception noValueException = assertThrows(IllegalArgumentException.class, ()->templateEngine.generateMessage(template, notNullvalues));
        assertTrue(noValueException.getMessage().contains("There are not present all variables"));

        // When variables are sent, but with null values...
        Map<String, String> withNullValues = new HashMap<>();
        withNullValues.put("#{male}",maleValue);
        withNullValues.put("#{female}",femaleValue);

        noValueException = assertThrows(IllegalArgumentException.class, ()->templateEngine.generateMessage(template, withNullValues));
        assertTrue(noValueException.getMessage().contains("Exist at least one null value"));
    }

}
