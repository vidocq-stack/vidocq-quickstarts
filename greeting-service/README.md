# Vidocq Greeting Service

A minimal REST service running on the **[Vidocq Runtime](https://doc.vidocq.dev/home-fr/)** —
a suite of Jakarta EE / MicroProfile runtimes written in pure Java 25: JPMS-native,
compile-time code generation (no runtime reflection/proxies), virtual threads.

Modules used:

| Module | Role |
| --- | --- |
| **Vidocq Runtime** | Boot orchestrator + extension SPI |
| **Cassini** | Jakarta REST 4.0 (JAX-RS) |
| **Chappe** | HTTP/1.1 + HTTP/2 server, virtual threads |
| **Champollion** | JSON-P / JSON-B serialization |
| **Vauban** | CDI 4.1 Lite (build-time DI) |
| **Ravel** | MicroProfile Config |

Built and run against **Vidocq `0.1.1`** (Maven Central) with **Java 25** and **Maven 3.9**.

> ⚠️ Vidocq `0.1.1` is an early snapshot with no stability guarantees — not for production.

## Project layout

```
greeting-service/
├── pom.xml
└── src/main/
    ├── java/
    │   ├── module-info.java            # JPMS module descriptor (Vidocq is JPMS-native)
    │   └── io/example/greeting/
    │       ├── App.java                # main() -> io.vidocq.runtime.core.Vidocq.main(args)
    │       ├── Greeting.java           # JSON record payload
    │       ├── GreetingService.java    # @ApplicationScoped CDI bean + MicroProfile Config
    │       └── GreetingResource.java   # @Path("/api/greetings") REST resource
    └── resources/
        └── vidocq.properties           # app + HTTP config
```

## Build & run

```bash
mvn -DskipTests package

# run the generated distribution (JPMS module launcher)
./target/greeting-service-1.0.0-SNAPSHOT/bin/greeting-service.sh
```

The service boots in ~100 ms and listens on **http://localhost:8080**.

## Try it

```bash
curl http://localhost:8080/api/greetings
# {"message":"Hello, World!","recipient":"World"}

curl 'http://localhost:8080/api/greetings?name=Alice'
# {"message":"Hello, Alice!","recipient":"Alice"}

curl http://localhost:8080/api/greetings/Bob
# {"message":"Hello, Bob!","recipient":"Bob"}
```

## Configuration

`greeting.message` is read via MicroProfile Config (Ravel) from `vidocq.properties`,
and can be overridden at runtime with an environment variable:

```bash
GREETING_MESSAGE=Bonjour ./target/greeting-service-1.0.0-SNAPSHOT/bin/greeting-service.sh

curl 'http://localhost:8080/api/greetings?name=Alice'
# {"message":"Bonjour, Alice!","recipient":"Alice"}
```

## How it works

- **Compile time.** The `vidocq-runtime-maven-plugin` runs `checkpom` → `generate`, and
  the `*-extension-codegen` annotation processors (declared in the compiler plugin's
  `annotationProcessorPaths`) generate everything up front: Vauban emits
  `GreetingService_Factory` / `_ClientProxy` and a `META-INF/vauban-beans.list` bean index;
  Cassini emits the REST adapter/route classes; injection is validated at compile time
  (a missing dependency fails the build, not the boot).
- **Boot.** `Vidocq.main` discovers the extensions (Chappe, Cassini, Ravel), starts the CDI
  container by instantiating the pre-generated beans (no classpath scanning, no reflective
  proxies), mounts the REST resources, and opens the Chappe listener on virtual threads.
- **Packaging.** `mvn package` produces `target/greeting-service-1.0.0-SNAPSHOT/` with a
  `bin/` launcher and a `lib/` module path, plus a `-dist.zip`.

## Notes & 0.1.1 caveats

These were verified against `0.1.1` and shaped the project:

- **JPMS is required for the launcher.** The generated `bin/` launcher runs
  `java --module-path lib --module io.example.greeting/io.example.greeting.App`, so the app
  is a real named module (`module-info.java`). The package is `opens` so the runtime can
  instantiate the generated factories/adapters and JSON-B can read the `Greeting` record.
- **Codegen pairing.** Each `vidocq-runtime-*-extension` dependency must be paired with its
  `*-extension-codegen` bundle in `annotationProcessorPaths` (enforced by `checkpom`). The
  `-codegen` artifacts are `pom`-packaged aggregators, hence `<type>pom</type>` on each path.
  Ravel has no codegen companion, so it is a plain dependency only.
- **`jvmArgs` must be non-empty.** The `package` goal throws an NPE on a null `jvmArgs`
  (an empty element collapses to null), so the pom passes a benign `-Dfile.encoding=UTF-8`.
- **HTTP port is fixed at 8080.** `vidocq.http.port` is not yet wired to Chappe in `0.1.1`;
  the listener always binds 8080 regardless of the property or `VIDOCQ_HTTP_PORT`.
- **Config profiles inert.** MicroProfile `%profile` keys / `VIDOCQ_PROFILE` did not activate
  in `0.1.1`, so no `%dev` overrides are used here.
- **`jlink` omitted.** The `jlink` goal needs `<mainModule>`/`<mainClass>`; it can be added
  for a self-contained image, but the plain module distribution above is enough to run.
```
