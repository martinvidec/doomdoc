# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

DoomDoc is a Java doclet generator that creates searchable HTML documentation from Java source code. It generates a single HTML file with inlined CSS and JavaScript, featuring a component tree sidebar and search functionality. The UI design follows JSDuck principles.

**Key characteristics:**
- Maven project with Java 11 language level
- Single HTML file output with all assets inlined
- No external JavaScript or CSS libraries
- Design principles: simplicity and performance
- UI design follows JSDuck principles for familiar developer experience

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

Expected result: `output.html` should contain complete package/class data with all members, methods, and JavaDoc.

## Current Features

**Documentation Extraction:**
- Complete JavaDoc parsing with all tags (@param, @return, @throws, @see, @since, @author, @deprecated, etc.)
- Full member extraction (fields, methods, constructors, enum constants, annotation elements)
- Modifiers and access levels (public, private, protected, static, final, abstract, etc.)
- Type parameters and generics
- Annotations with values
- Inheritance hierarchy (extends, implements)
- HTML tags in JavaDoc are preserved and rendered correctly
- Inline tags ({@link}, {@code}, {@literal}) are processed

**UI Features:**
- Single-file HTML output (all CSS/JS inlined)
- Real-time search with highlighting
- Collapsible/expandable package tree
- Responsive 3-column layout
- Compact, high-density design optimized for technical documentation
- Syntax highlighting for code elements
- Member grouping (fields, constructors, methods)
- Type badges (class, interface, enum, annotation)

## Architecture

### Core Components

**DoomDoclet.java** (src/main/java/at/videc/DoomDoclet.java)
- Extends `jdk.javadoc.doclet.StandardDoclet`
- Main entry point: `run(DocletEnvironment environment)` method
- Walks `src/main/resources/stylesheets/` and `src/main/resources/javascript/` to inline assets
- Groups classes by package using Java Doclet API
- Serializes package structure to JSON using Gson
- Outputs single HTML file with embedded tree generation call

**UI Structure**
The generated HTML documentation has a 3-part layout:
1. **Search Bar** (top) - Faceted search for filtering classes, methods, and fields
2. **Component Tree** (left sidebar) - Hierarchical package/class navigation
3. **Detail View** (main content) - Displays documentation for selected element

**Component Architecture**
The UI follows a strict component pattern:
- Each component = 1 JavaScript file + 1 CSS file
- Tree component: `tree.js` + `tree.css`
- Detail view: `detail.css` for member rendering
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

1. Doclet API provides `TypeElement` instances for all included classes
2. `TypeElementConverter` extracts complete information (see DATA_STRUCTURES.md):
   - Class/Interface/Enum/Annotation metadata
   - Fields with types, modifiers, annotations
   - Constructors with parameters and exceptions
   - Methods with return types, parameters, exceptions
   - JavaDoc with all tags parsed
   - Type parameters and generics
   - Inheritance relationships
3. `PackageTree` organizes types by package into `DocumentationModel`
4. Model serialized to JSON via Gson (with HTML escaping disabled)
5. JSON embedded in HTML and passed to `generateTree()` JavaScript function
6. Tree and detail views dynamically generated in DOM on page load

## Development Guidelines

### Code Organization Requirements
- **Language Level**: Java 11
- **UI Resources**: Place in `src/main/resources/javascript/` or `src/main/resources/stylesheets/`
- **Component Rule**: Exactly one JavaScript file and one CSS file per component
- **No External Libraries**: Do not use external JavaScript or CSS libraries
- **Modular Code**: Follow SOLID, DRY, KISS principles

### Tree Component Specifications
When modifying tree functionality (src/main/resources/javascript/tree.js):

**Structure:**
- Tree has one root element (always expanded)
- Packages are nodes (expandable/collapsible, have detail views)
- Classes are leafs (have detail views, not expandable)
- Nodes may contain other nodes or leafs
- Child nodes are indented with visual hierarchy (border-left)
- Leafs must not be visible when parent node is collapsed
- Given two classes in the same package, there should be one node for the package and two leafs for the classes

**Behavior:**
- Tree is expandable and collapsible
- Tree is searchable (search bar at top)
- Tree is filterable
- Tree is scrollable
- Tree is resizable
- Search results are highlighted and parent nodes auto-expanded

**Files:**
- Code: `src/main/resources/javascript/tree.js`
- Styles: `src/main/resources/stylesheets/tree.css`

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
├── BOMB.java                 # Annotation interface
└── bomblet/                  # Core extraction logic
    ├── PackageTree.java      # Package/type organization
    ├── TypeElementConverter.java  # Member & JavaDoc extraction
    └── dto/                  # Data Transfer Objects (JSON schema)
        ├── DocumentationModel.java
        ├── PackageInfo.java
        ├── TypeInfo.java (+ ClassInfo, InterfaceInfo, EnumInfo, AnnotationInfo)
        ├── FieldInfo.java
        ├── MethodInfo.java
        ├── ConstructorInfo.java
        ├── JavaDocComment.java
        └── ... (other DTOs per DATA_STRUCTURES.md)

src/test/java/at/videc/
├── DoomDocTest.java          # Doclet integration test
└── dummy/                    # Test classes for doc generation
    ├── TestClass1.java
    └── FeatureClassOne.java

# UI Resources
src/main/resources/
├── stylesheets/
│   ├── common.css            # Global styles (layout, colors, spacing)
│   ├── tree.css              # Tree component styles
│   └── detail.css            # Detail view styles (members, javadoc)
└── javascript/
    └── tree.js               # Tree component logic and detail rendering

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
