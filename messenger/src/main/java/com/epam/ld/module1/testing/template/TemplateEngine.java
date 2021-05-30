package com.epam.ld.module1.testing.template;

import com.epam.ld.module1.testing.Client;

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
        // Replacing placeholders with values...
        for(String key: values.keySet()){
            templateWithValues = templateWithValues.replace(key,values.get(key).toString());
        }
        return templateWithValues;
    }
}
