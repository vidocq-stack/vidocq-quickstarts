package io.example.greeting;

/**
 * Entry point. Vidocq.main(args) runs the full boot sequence: load config,
 * start the CDI container (Vauban), register REST routes (Cassini) and open
 * the HTTP listener (Chappe) on virtual threads.
 */
public class App {
    public static void main(String[] args) {
        io.vidocq.runtime.core.Vidocq.main(args);
    }
}
