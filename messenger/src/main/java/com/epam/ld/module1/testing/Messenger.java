package com.epam.ld.module1.testing;


import com.epam.ld.module1.testing.template.Template;
import com.epam.ld.module1.testing.template.TemplateEngine;

import java.util.Map;

/**
 * The type Messenger.
 */
public class Messenger {
    private MailServer mailServer;
    private TemplateEngine templateEngine;

    /**
     * Instantiates a new Messenger.
     *
     * @param mailServer     the mail server
     * @param templateEngine the template engine
     */
    public Messenger(MailServer mailServer,
                     TemplateEngine templateEngine) {
        this.mailServer = mailServer;
        this.templateEngine = templateEngine;
    }

    /**
     * Send message.
     *
     * @param client   the client
     * @param template the template
     * @param values   values for filling in the template
     */
    public void sendMessage(Client client, Template template, Map<String, String> values) {
        String messageContent =
            templateEngine.generateMessage(template, values);
        mailServer.send(client.getAddresses(), messageContent);
    }
}