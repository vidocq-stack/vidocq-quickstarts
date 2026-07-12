/**
 * Vidocq is JPMS-native, so the application is a proper named module.
 */
module io.example.greeting {
    // Runtime bootstrap entry point (io.vidocq.runtime.core.Vidocq).
    requires io.vidocq.runtime.core;

    // Jakarta REST + CDI Lite + Inject annotations used in the sources.
    requires jakarta.ws.rs;
    requires jakarta.cdi;
    requires jakarta.inject;

    // MicroProfile Config API (implemented by Ravel).
    requires org.eclipse.microprofile.config;

    // Referenced by the APT-generated code:
    //   *_Factory implements io.vidocq.vauban.core.BeanFactory
    //   *$$CassiniAdapter implements io.vidocq.cassini.spi.gen.ResourceAdapter
    requires io.vidocq.vauban.core;
    requires io.vidocq.cassini.api;

    // Let the runtime reflectively instantiate the generated factories/adapters
    // and let JSON-B (Champollion) read the Greeting record.
    opens io.example.greeting;
}
