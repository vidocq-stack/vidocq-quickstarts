package io.example.greeting;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * CDI Lite bean (Vauban). Its bean metadata is generated at build time, so the
 * container just instantiates it at boot — no classpath scanning, no reflection.
 */
@ApplicationScoped
public class GreetingService {

    /**
     * MicroProfile Config (Ravel), read programmatically via ConfigProvider.
     * "greeting.message" is resolved from the GREETING_MESSAGE environment
     * variable if set, otherwise from vidocq.properties.
     */
    private final Config config = ConfigProvider.getConfig();

    public Greeting greet(String recipient) {
        String message = config.getOptionalValue("greeting.message", String.class).orElse("Hello");
        String who = (recipient == null || recipient.isBlank()) ? "World" : recipient;
        return new Greeting(message + ", " + who + "!", who);
    }
}
