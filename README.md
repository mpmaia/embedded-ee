# EmbeddedEE

EmbeddedEE is a small library that provides an embedded HTTP server with JAX-RS, CDI and JPA support. Internally, EmbeddedEE works as a glue layer between Jetty, Weld, Jersey, and Hibernate.

## Usage

- Add the EmbeddedEE dependency to your maven project:

		<dependency>
			<groupId>me.sigtrap</groupId>
			<artifactId>embeddedee-core</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>

- Start the server. You can optionally forward the application command line arguments:

		public class App {  
			public static void main(String[] args) {  
				EmbeddedEE app = new EmbeddedEE();  
				//forward command line args to EmbeddedEE  
				app.start(args);  
			}
		}

## Configuration

The server can be configured through a YAML file called application.yaml. During the startup search the library you search for the configuration file on this order:

1. One or more configuration files provided by the command line option "-c". If multiple files are provided, the configuration will be merged;
2. A file called application.yaml located on the current directory;
3. A resource file called application.yaml.

### Syntax

The example below describes the main configuration options available:

    ---
    server:
      # HTTP port the server should listen
      httpPort: 8080
      # Application URL
      serverUrl: http://localhost:8080/
      # Upper limit on the number of threads the server will start
      maxThreads: 512
      # Minimum number of idle threads the server will keep on the pool
      minThreads: 128
      # Directory from where static files will be served. If this value is not provided, we'll look for a webapp folder
      # inside the jar or on the current directory
      webRoot: /var/www
      httpConfiguration:
        # Sets if the server should send the server version header on each response
        sendServerVersion: false
    dataSources:
      # Datasource name
      ExampleDS:
        # JNDI name where the datasource will be published
        jndiName: datasources/ExampleDS
        # JDBC connection URL
        jdbcUrl: jdbc:h2:~/exemplo.db;AUTO_SERVER=TRUE
        # JDBC driver
        driverClassName: org.h2.Driver
        # Username and password that will be provided to the database driver
        username: sa
        password: ''
        # Maximum number of connections that the pool will support
        maxPoolSize: 20
        # Minimum idle connections
        minPoolSize: 1
        # Query executed by the pool to test the connection
        connectionTestQuery: SELECT 1
        # SQL statement executed for each new connection
        connectionInitSql: SELECT 1
        # Maximum number of milliseconds that a client will wait for a connection
        connectionTimeout: 30000
        # Maximum number of milliseconds that a connection will stay idle on the pool
        maxIdleTime: 120000
    properties:
      # One or more key/value configuration properties that will be available via 'System.getProperty'
      property: value

## Command line options

The following command line options are supported by the EmbeddedEE server:

- "-c [config file]": Path to a yaml configuration file located on the filesystem. If this option is provided, the configuration file provided inside the jar will be ignored.
- "-l [log4j config file]": Path to a log4j configuration file that should be used by the application.
- "-p [http port]": HTTP port that the server should listen. If this option is provided, the port configured on the application.yaml file is ignored.

## Static Files

Any files located inside a "webapp" directory at the current directory will be served at the root application level.

## Sample

This repository contains a sample application that can be used as a starting point for new projects.

## License

EmbeddedEE is licensed under the Apache License Version 2.0. Check the LICENSE file for the full license text.

## TO-DO:

- Add full JTA and XA support. At the moment the library supports only non-XA datasources and RESOURCE_LOCAL persistence contexts;
- Authentication support;
- Unit tests;