package com.epam.ld.module1.testing.template;

import com.epam.ld.module1.testing.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type Template engine.
 */
public class TemplateEngine {
    /**
     * Generate message string.
     *
     * @param template the template
     * @param values   values for filling in the template
     * @return the string
     */
    public String generateMessage(Template template, Map<String, String> values) {
        String fakePlaceholderMark = "<not-placeholder>";
        String placeholderPrefix = "#{";
        String templateWithValues = template.getBody();
        // Validating values...
        validateValuesForTemplate(template, values);

        // Replacing placeholders with values...
        for(String key: values.keySet()){
            String valueForVariable = values.get(key);
            // Adding a mark for value in format '#{...}', for avoid it as placeholder
            if(valueForVariable.matches("#\\{[^(#{)]*}")){
                valueForVariable = valueForVariable.replace(placeholderPrefix,fakePlaceholderMark);
            }
            templateWithValues = templateWithValues.replace(key,valueForVariable);
        }

        // Removing mark for fake placeholders...
        return templateWithValues.replace(fakePlaceholderMark,placeholderPrefix);
    }

    /**
     * Validate parameters for template generation
     *
     * @param template the template
     * @param values   values for filling in the template
     */
    public void validateValuesForTemplate(Template template, Map<String, String> values) {
        List<String> listVariablesInTemplate = Arrays.asList(template.getVariables());
        List<String> listVariablesToRemove = new ArrayList<>();

        // Remove all variables not present in template
        for (String key :values.keySet()){
            if(!listVariablesInTemplate.contains(key)) {
                listVariablesToRemove.add(key);
            }
        }
        listVariablesToRemove.forEach(values::remove);

        // Checking if all variables are in the map
        if(!values.keySet().containsAll(listVariablesInTemplate)) {
            throw new IllegalArgumentException("There are not present all variables");
        }

        // Checking if all variables have a value...
        if(values.values().contains(null)) {
            throw new IllegalArgumentException("Exist at least one null value");
        }
    }
}
