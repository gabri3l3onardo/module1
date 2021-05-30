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

    @ParameterizedTest
    //CSV columns: #{date}, #{male}, #{color}, #{female}, expected message
    @CsvSource({
            "5.30.2021, JOHN, blue, JANE, 'Today 5.30.2021, this test message was sent to JANE. Nice weekend'",
            "6.1.2021, PETER, red, KATRINA, 'Today 6.1.2021, this test message was sent to KATRINA. Nice weekend'",
            "5.30.2021, JOHN, , JANE, 'Today 5.30.2021, this test message was sent to JANE. Nice weekend'",
            "6.1.2021, , , KATRINA, 'Today 6.1.2021, this test message was sent to KATRINA. Nice weekend'"
    })
    public void testGeneratorIgnoresValuesForVariablesNotFoundedInTemplate(String dateValue, String maleValue, String colorValue, String femaleValue, String messageExpected) {
        template = new Template("Today #{date}, this test message was sent to #{female}. Nice weekend","#{date}","#{female}");
        templateEngine = new TemplateEngine();

        Map<String, String> values = new HashMap<>();
        values.put("#{date}",dateValue);
        values.put("#{male}",maleValue);
        values.put("#{color}",colorValue);
        values.put("#{female}",femaleValue);

        String generatedMessage = templateEngine.generateMessage(template, values);
        assertEquals(messageExpected, generatedMessage, "Message generated is not as the expected");
    }

    @ParameterizedTest
    //CSV columns: #{male}, #{female}, expected message
    @CsvSource({
            "'#{name}', JANE, 'This is a test message for #{name} from JANE. Thanks in advance'",
            "PETER, '#{name}', 'This is a test message for PETER from #{name}. Thanks in advance'",
            "'#{female}', JANE, 'This is a test message for #{female} from JANE. Thanks in advance'",
            "PETER, '#{male}', 'This is a test message for PETER from #{male}. Thanks in advance'"
    })
    public void testPlaceholdersReplacementWithValuesUsingTemplatesSpecialChars(String maleValue, String femaleValue, String messageExpected) {
        template = new Template("This is a test message for #{male} from #{female}. Thanks in advance","#{male}","#{female}");
        templateEngine = new TemplateEngine();

        Map<String, String> values = new HashMap<>();
        values.put("#{male}",maleValue);
        values.put("#{female}",femaleValue);

        String generatedMessage = templateEngine.generateMessage(template, values);
        assertEquals(messageExpected, generatedMessage, "Message generated is not as the expected");
    }

}
