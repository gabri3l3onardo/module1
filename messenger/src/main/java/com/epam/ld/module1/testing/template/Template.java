package com.epam.ld.module1.testing.template;

/**
 * The type Template.
 */
public class Template {

    private String body;
    private String[] variables;

    public Template(String body, String... variables) {
        this.body = body;
        this.variables = variables;
    }

    public String getBody() {
        return body;
    }

    public String[] getVariables() {
        return variables;
    }
}
