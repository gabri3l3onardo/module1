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
        String templateWithValues = template.getBody();
        // Validating values...
        validateValuesForTemplate(template, values);

        // Replacing placeholders with values...
        for(String key: values.keySet()){
            templateWithValues = templateWithValues.replace(key,values.get(key).toString());
        }
        return templateWithValues;
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
