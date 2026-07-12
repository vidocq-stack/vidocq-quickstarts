package io.example.greeting;

/**
 * Immutable JSON payload. Vidocq's Champollion generates the JSON-B
 * (de)serializer for this record at compile time (APT) — no runtime reflection.
 */
public record Greeting(String message, String recipient) {
}
