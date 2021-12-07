#!/bin/bash
# Compiles and runs the test app using the sample test document provided

# If there is an error, stop executing
set -e

# Compile
mvn clean verify

# Run
java -classpath target/dependencies/*:target/classes com.snowbound.re.maven_examples.conversion.Main test-documents/FreedomTrail.pdf
