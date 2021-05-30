package com.epam.ld.module1.testing.template;

import com.epam.ld.module1.testing.Client;

import java.util.Arrays;
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
        // Checking if all variables are in the map
        if(!values.keySet().containsAll(Arrays.asList(template.getVariables()))) {
            throw new IllegalArgumentException("There are not present all variables");
        }
        // Checking if all variables have a value
        if(values.values().contains(null)) {
            throw new IllegalArgumentException("Exist at least one null value");
        }
        // Replacing placeholders with values...
        for(String key: values.keySet()){
            templateWithValues = templateWithValues.replace(key,values.get(key).toString());
        }
        return templateWithValues;
    }
}
