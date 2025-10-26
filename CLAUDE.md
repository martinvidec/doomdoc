# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

DoomDoc is a Java doclet generator that creates searchable HTML documentation from Java source code. It generates a single HTML file with inlined CSS and JavaScript, featuring a component tree sidebar and search functionality. The UI design follows JSDuck principles.

**Key characteristics:**
- Maven project with Java 11 language level
- Single HTML file output with all assets inlined
- No external JavaScript or CSS libraries
- Design principles: simplicity and performance

## Important Documents

**DATA_STRUCTURES.md**
Defines the comprehensive JSON data structure for representing Java classes and JavaDoc comments. This is the canonical reference for:
- How to extract ALL relevant information from Java source files
- The intermediate JSON format that feeds the HTML generation
- Complete structure for classes, interfaces, enums, annotations, fields, methods, and JavaDoc tags

**When working on DoomDoclet changes**, always consult DATA_STRUCTURES.md to ensure the JSON output matches the defined schema.

## Build and Test Commands

### Essential Build Commands
```bash
# Clean build artifacts
mvn clean

# Compile project (8-10 seconds first run, 2 seconds subsequent)
mvn compile

# Build package with dependencies (18-20 seconds first run, 3-5 seconds subsequent)
mvn package -DskipTests

# Run tests (NOTE: Tests currently fail - this is expected behavior)
mvn test

# Full build workflow
mvn clean compile package -DskipTests
```

### Generate Documentation (Core Functionality)
```bash
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar \
  -sourcepath ./src/main/java \
  -subpackages at.videc
```
This generates `output.html` in the project root (~1 second).

### Validation Workflow
After making changes, always validate:
1. Build: `mvn clean compile package -DskipTests`
2. Generate docs: Run the javadoc command above
3. Verify output: `ls -la output.html` and `grep "generateTree" output.html`

Expected result: `output.html` should be ~5KB and contain package/class data for at.videc and at.videc.dummy packages.

## Architecture

### Core Components

**DoomDoclet.java** (src/main/java/at/videc/DoomDoclet.java)
- Extends `jdk.javadoc.doclet.StandardDoclet`
- Main entry point: `run(DocletEnvironment environment)` method
- Walks `src/main/resources/stylesheets/` and `src/main/resources/javascript/` to inline assets
- Groups classes by package using Java Doclet API
- Serializes package structure to JSON using Gson
- Outputs single HTML file with embedded tree generation call

**Component Architecture**
The UI follows a strict component pattern:
- Each component = 1 JavaScript file + 1 CSS file
- Tree component: `tree.js` + `tree.css`
- Common styles: `common.css`

### Key Design Patterns

**Resource Inlining**
All CSS and JavaScript files are read from `src/main/resources/` at generation time and inlined into the HTML output. The doclet walks the stylesheets and javascript directories and embeds content in `<style>` and `<script>` tags.

**Tree Structure**
- Packages are nodes (expandable/collapsible)
- Classes are leafs (clickable for detail view)
- Root node always expanded
- Child nodes indented with border-left visual hierarchy
- Search expands parent nodes automatically

**Data Flow**

*Current implementation:*
1. Doclet API provides `TypeElement` instances for all included classes
2. Elements grouped into `Map<String, List<String>>` by package
3. Map serialized to JSON via Gson
4. JSON passed to `generateTree()` JavaScript function
5. Tree dynamically generated in DOM on page load

*Target architecture (see DATA_STRUCTURES.md):*
The goal is to extract complete information from Java classes including fields, methods, constructors, JavaDoc comments, annotations, and generics into a comprehensive JSON structure. This will enable rich detail views and full-text search. Refer to DATA_STRUCTURES.md for the complete JSON schema.

## Development Guidelines

### Code Organization Requirements
- **Language Level**: Java 11
- **UI Resources**: Place in `src/main/resources/javascript/` or `src/main/resources/stylesheets/`
- **Component Rule**: Exactly one JavaScript file and one CSS file per component
- **No External Libraries**: Do not use external JavaScript or CSS libraries
- **Modular Code**: Follow SOLID, DRY, KISS principles

### Tree Component Specifications
When modifying tree functionality (src/main/resources/javascript/tree.js):
- Root element may have detail view
- Packages have detail views
- Classes have detail views
- Child nodes only visible when parent expanded
- Search results highlighted and parents auto-expanded
- Tree must be expandable, collapsible, searchable, filterable, scrollable, resizable

### Making Changes

**For Doclet Logic** (DoomDoclet.java):
- Remember that changes require recompilation (`mvn compile`)
- Must copy dependencies to target/dependencies (`mvn package`)
- Test by running the javadoc command

**For UI Components** (JavaScript/CSS):
- Files are read at doclet runtime, so changes require regenerating docs
- Test by rebuilding and running javadoc command
- Verify changes in generated output.html

**For Build Configuration** (pom.xml):
- Dependencies are copied to `target/dependencies/` during package phase
- Gson 2.8.9 is required dependency
- JUnit 4.12 for testing

### Known Issues
- Duplicate maven-surefire-plugin declaration in pom.xml (non-critical warning)
- DoomDocTest.testDoomDoclet always fails - this is expected (placeholder test)
- Tests expect output.html but don't run doclet to create it

## Dependencies

- **Gson 2.8.9**: JSON serialization for package/class data
- **JUnit 4.12**: Test framework (tests currently placeholder)
- **JDK 11+**: Required for compilation and javadoc API

## Important Files

```
# Documentation
DATA_STRUCTURES.md            # JSON schema for Java class representation
CLAUDE.md                     # This file - guidance for Claude Code
README.md                     # Project description and requirements

# Source Code
src/main/java/at/videc/
├── DoomDoclet.java           # Main doclet implementation
├── Main.java                 # Entry point class
└── BOMB.java                 # Annotation interface

src/test/java/at/videc/
├── DoomDocTest.java          # Doclet integration test
└── dummy/                    # Test classes for doc generation
    ├── TestClass1.java
    └── FeatureClassOne.java

# UI Resources
src/main/resources/
├── stylesheets/
│   ├── common.css            # Global styles
│   └── tree.css              # Tree component styles
└── javascript/
    └── tree.js               # Tree component logic

# Build Artifacts
target/                       # Generated by Maven
├── classes/                  # Compiled .class files
├── dependencies/             # Gson JAR (required for doclet classpath)
└── doomdoc-0.0.1-SNAPSHOT.jar

# Generated Output
output.html                   # Generated documentation
```

## Timeout Recommendations

Maven commands can take time, especially on first run:
- Use 60+ second timeout for `mvn compile`
- Use 120+ second timeout for `mvn package` (first run downloads dependencies)
- Documentation generation is fast (~1 second)
