# Vidocq Quickstarts

Runnable, self-contained examples for the **[Vidocq](https://vidocq.dev/) stack** — a
sovereign ("souverain") Jakarta EE / MicroProfile implementation written in pure Java 25:
JPMS-native, compile-time code generation (no runtime reflection or proxies), and virtual
threads throughout.

This repository is the **landing zone for new users**. Each subdirectory is an independent,
buildable project you can copy, run, and use as the starting point for your own service.

> ⚠️ The examples currently target Vidocq `0.1.1`, an early snapshot with no stability
> guarantees — great for learning, not for production.

## The stack at a glance

Vidocq assembles a set of focused runtimes behind a single boot orchestrator:

| Module | Role |
| --- | --- |
| **Vidocq Runtime** | Boot orchestrator + extension SPI |
| **Cassini** | Jakarta REST 4.0 (JAX-RS) |
| **Chappe** | HTTP/1.1 + HTTP/2 server, virtual threads |
| **Champollion** | JSON-P / JSON-B serialization |
| **Vauban** | CDI 4.1 Lite (build-time dependency injection) |
| **Ravel** | MicroProfile Config |

## Prerequisites

- **Java 25** (JDK)
- **Maven 3.9+**

```bash
java -version   # should report 25
mvn -version    # should report 3.9 or newer
```

## Quickstarts

| Quickstart | What it shows | Endpoints |
| --- | --- | --- |
| [`greeting-service`](./greeting-service) | Minimal REST service: Jakarta REST resource, a CDI bean, JSON-B record payloads, and MicroProfile Config | `GET /api/greetings`, `GET /api/greetings/{name}` |

## Run a quickstart

Each quickstart is a standalone Maven project. For example:

```bash
cd greeting-service
mvn -DskipTests package

# run the generated JPMS module distribution
./target/greeting-service-1.0.0-SNAPSHOT/bin/greeting-service.sh

# in another terminal
curl http://localhost:8080/api/greetings
# {"message":"Hello, World!","recipient":"World"}
```

See each quickstart's own `README.md` for the full walkthrough and any version-specific caveats.

## Adding a new quickstart

The repo is intentionally flat and modular so it's easy to extend — a new quickstart is just a
new top-level directory with its own `pom.xml`. The fastest path is to copy `greeting-service`
and adapt it:

1. **Copy the template**

   ```bash
   cp -r greeting-service my-quickstart
   cd my-quickstart
   rm -rf target
   ```

2. **Rename the project.** In `pom.xml`, update `<artifactId>`, `<finalName>`, `<name>`,
   `<description>`, and the plugin's `<mainClass>` / `<scriptName>`.

3. **Rename the Java module & package.** Rename the `io.example.greeting` package and update
   `module-info.java` (`module` name, `opens`) plus every `package` / import statement to match.

4. **Write your code**, then build and run:

   ```bash
   mvn -DskipTests package
   ./target/<finalName>-1.0.0-SNAPSHOT/bin/<scriptName>.sh
   ```

5. **Document it.** Give the quickstart its own `README.md` and add a row to the
   [Quickstarts](#quickstarts) table above.

Keep each quickstart minimal and focused on a single concept so newcomers can read it end to end.

## Roadmap

- **Phase 1 (now):** a clean home repo with runnable quickstarts.
- **Phase 2:** fold these into the official Vidocq documentation.
