package com.epam.ld.module1.testing.template;

import com.epam.ld.module1.testing.Client;
import com.epam.ld.module1.testing.template.Template;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

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

    @ParameterizedTest
    @ValueSource(strings = {
            "Using characters as "+"\u0020"+"\u0021"+"\""+"\u0023"+"\u0024"+"\u0025"+"\u0026"+"\u0027"+"\u0028"+"\u0029"+"\u002A"+"\u002B"+"\u002C"+"\u002D"+"\u002E"+"\u002F"+" in body",
            "Using characters as "+"\u0030"+"\u0031"+"\u0032"+"\u0033"+"\u0034"+"\u0035"+"\u0036"+"\u0037"+"\u0038"+"\u0039"+"\u003A"+"\u003B"+"\u003C"+"\u003D"+"\u003E"+"\u003F"+" in body",
            "Using characters as "+"\u0040"+"\u0041"+"\u0042"+"\u0043"+"\u0044"+"\u0045"+"\u0046"+"\u0047"+"\u0048"+"\u0049"+"\u004A"+"\u004B"+"\u004C"+"\u004D"+"\u004E"+"\u004F"+" in body",
            "Using characters as "+"\u0050"+"\u0051"+"\u0052"+"\u0053"+"\u0054"+"\u0055"+"\u0056"+"\u0057"+"\u0058"+"\u0059"+"\u005A"+"\u005B"+"\\"+"\u005D"+"\u005E"+"\u005F"+" in body",
            "Using characters as "+"\u0060"+"\u0061"+"\u0062"+"\u0063"+"\u0064"+"\u0065"+"\u0066"+"\u0067"+"\u0068"+"\u0069"+"\u006A"+"\u006B"+"\u006C"+"\u006D"+"\u006E"+"\u006F"+" in body",
            "Using characters as "+"\u0070"+"\u0071"+"\u0072"+"\u0073"+"\u0074"+"\u0075"+"\u0076"+"\u0077"+"\u0078"+"\u0079"+"\u007A"+"\u007B"+"\u007C"+"\u007D"+"\u007E"+" in body",
            "Using characters as "+"\u00A0"+"\u00A1"+"\u00A2"+"\u00A3"+"\u00A4"+"\u00A5"+"\u00A6"+"\u00A7"+"\u00A8"+"\u00A9"+"\u00AA"+"\u00AB"+"\u00AC"+"\u00AD"+"\u00AE"+"\u00AF"+" in body",
            "Using characters as "+"\u00B0"+"\u00B1"+"\u00B2"+"\u00B3"+"\u00B4"+"\u00B5"+"\u00B6"+"\u00B7"+"\u00B8"+"\u00B9"+"\u00BA"+"\u00BB"+"\u00BC"+"\u00BD"+"\u00BE"+"\u00BF"+" in body",
            "Using characters as "+"\u00C0"+"\u00C1"+"\u00C2"+"\u00C3"+"\u00C4"+"\u00C5"+"\u00C6"+"\u00C7"+"\u00C8"+"\u00C9"+"\u00CA"+"\u00CB"+"\u00CC"+"\u00CD"+"\u00CE"+"\u00CF"+" in body",
            "Using characters as "+"\u00D0"+"\u00D1"+"\u00D2"+"\u00D3"+"\u00D4"+"\u00D5"+"\u00D6"+"\u00D7"+"\u00D8"+"\u00D9"+"\u00DA"+"\u00DB"+"\u00DC"+"\u00DD"+"\u00DE"+"\u00DF"+" in body",
            "Using characters as "+"\u00E0"+"\u00E1"+"\u00E2"+"\u00E3"+"\u00E4"+"\u00E5"+"\u00E6"+"\u00E7"+"\u00E8"+"\u00E9"+"\u00EA"+"\u00EB"+"\u00EC"+"\u00ED"+"\u00EE"+"\u00EF"+" in body",
            "Using characters as "+"\u00F0"+"\u00F1"+"\u00F2"+"\u00F3"+"\u00F4"+"\u00F5"+"\u00F6"+"\u00F7"+"\u00F8"+"\u00F9"+"\u00FA"+"\u00FB"+"\u00FC"+"\u00FD"+"\u00FE"+"\u00FF"+" in body"
    })
    public void testSupportForLatin1CharsetInTemplate(String templateBody) {

        template = new Template("Template with value #{value}: " + templateBody,"#{value}");
        templateEngine = new TemplateEngine();

        Map<String, String> values = new HashMap<>();
        values.put("#{value}","any_value");

        String generatedMessage = templateEngine.generateMessage(template, values);

        assertTrue(generatedMessage.contains(templateBody), "Some characters from charset are not allowed");
    }

    public static String[][] getVariablesWithCharset(){
        return new String[][]{
                {"#{"+"\u0020"+"\u0021"+"\""+"\u0023"+"\u0024"+"\u0025"+"}" , "#{"+"\u0026"+"\u0027"+"\u0028"+"\u0029"+"\u002A"+"\u002B"+"}" , "#{"+"\u002C"+"\u002D"+"\u002E"+"\u002F"+"}"},
                {"#{"+"\u0030"+"\u0031"+"\u0032"+"\u0033"+"\u0034"+"\u0035"+"}" , "#{"+"\u0036"+"\u0037"+"\u0038"+"\u0039"+"\u003A"+"}" , "#{"+"\u003B"+"\u003C"+"\u003D"+"\u003E"+"\u003F"+"}"},
                {"#{"+"\u0040"+"\u0041"+"\u0042"+"\u0043"+"\u0044"+"\u0045"+"\u0046"+"}" , "#{"+"\u0047"+"\u0048"+"\u0049"+"\u004A"+"\u004B"+"\u004C"+"}" , "#{"+"\u004D"+"\u004E"+"\u004F"+"}"},
                {"#{"+"\u0050"+"\u0051"+"\u0052"+"\u0053"+"\u0054"+"\u0055"+"}" , "#{"+"\u0056"+"\u0057"+"\u0058"+"\u0059"+"\u005A"+"}" , "#{"+"\u005B"+"\\"+"\u005D"+"\u005E"+"\u005F"+"}"},
                {"#{"+"\u0060"+"\u0061"+"\u0062"+"\u0063"+"\u0064"+"\u0065"+"\u0066"+"}" , "#{"+"\u0067"+"\u0068"+"\u0069"+"\u006A"+"\u006B"+"}" , "#{"+"\u006C"+"\u006D"+"\u006E"+"\u006F"+"}"},
                {"#{"+"\u0070"+"\u0071"+"\u0072"+"\u0073"+"\u0074"+"\u0075"+"\u0076"+"\u0077"+"}" , "#{"+"\u0078"+"\u0079"+"\u007A"+"\u007B"+"\u007C"+"}" , "#{"+"\u007D"+"\u007E"+"}"},
                {"#{"+"\u00A0"+"\u00A1"+"\u00A2"+"\u00A3"+"\u00A4"+"\u00A5"+"\u00A6"+"}" , "#{"+"\u00A7"+"\u00A8"+"\u00A9"+"\u00AA"+"}" , "#{"+"\u00AB"+"\u00AC"+"\u00AD"+"\u00AE"+"\u00AF"+"}"},
                {"#{"+"\u00B0"+"\u00B1"+"\u00B2"+"\u00B3"+"\u00B4"+"\u00B5"+"}" , "#{"+"\u00B6"+"\u00B7"+"\u00B8"+"\u00B9"+"\u00BA"+"\u00BB"+"}" , "#{"+"\u00BC"+"\u00BD"+"\u00BE"+"\u00BF"+"}"},
                {"#{"+"\u00C0"+"\u00C1"+"\u00C2"+"\u00C3"+"\u00C4"+"\u00C5"+"\u00C6"+"\u00C7"+"}" , "#{"+"\u00C8"+"\u00C9"+"\u00CA"+"\u00CB"+"\u00CC"+"\u00CD"+"}" , "#{"+"\u00CE"+"\u00CF"+"}"},
                {"#{"+"\u00D0"+"\u00D1"+"\u00D2"+"\u00D3"+"\u00D4"+"\u00D5"+"\u00D6"+"\u00D7"+"\u00D8"+"}" , "#{"+"\u00D9"+"\u00DA"+"}" , "#{"+"\u00DB"+"\u00DC"+"\u00DD"+"\u00DE"+"\u00DF"+"}"},
                {"#{"+"\u00E0"+"\u00E1"+"\u00E2"+"\u00E3"+"\u00E4"+"\u00E5"+"\u00E6"+"}" ,  "#{"+"\u00E7"+"\u00E8"+"\u00E9"+"\u00EA"+"\u00EB"+"\u00EC"+"}" , "#{"+"\u00ED"+"\u00EE"+"\u00EF"+"}"},
                {"#{"+"\u00F0"+"\u00F1"+"\u00F2"+"\u00F3"+"\u00F4"+"\u00F5"+"\u00F6"+"\u00F7"+"}" , "#{"+"\u00F8"+"\u00F9"+"\u00FA"+"\u00FB"+"\u00FC"+"}" , "#{"+"\u00FD"+"\u00FE"+"\u00FF"+"}"}
           };
    }

    @ParameterizedTest
    @MethodSource("getVariablesWithCharset")
    public void testSupportForLatin1CharsetInVariables(String firstVar, String secondVar, String thirdVar) {
        String messageExpected = "Template: any_value any_value any_value";

        StringBuilder templateBody = new StringBuilder("Template: ");
        templateBody.append(firstVar);
        templateBody.append(" ");
        templateBody.append(secondVar);
        templateBody.append(" ");
        templateBody.append(thirdVar);

        template = new Template(templateBody.toString(),firstVar,secondVar,thirdVar);
        templateEngine = new TemplateEngine();

        Map<String, String> values = new HashMap<>();
        values.put(firstVar,"any_value");
        values.put(secondVar,"any_value");
        values.put(thirdVar,"any_value");

        String generatedMessage = templateEngine.generateMessage(template, values);

        assertEquals(messageExpected, generatedMessage, "Message generated is not as the expected");
    }

}
