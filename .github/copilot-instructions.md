# DoomDoc - Java Doclet Generator

DoomDoc is a Java Maven project that generates searchable HTML documentation from Java source code. It creates a single HTML file with inlined CSS and JavaScript, featuring a component tree sidebar and search functionality.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Prerequisites and Setup
- Requires Java 11+ (tested with Java 17)
- Requires Maven 3.6+
- Project uses Java 11 language level

### Build Commands (VALIDATED)
Run these commands in order for a complete setup:

```bash
# Clean any existing build artifacts
mvn clean
# Takes ~1 second

# Compile the project (NEVER CANCEL - wait for completion)
mvn compile
# Takes ~8-10 seconds on first run, ~2 seconds on subsequent runs
# Set timeout to 60+ seconds for safety

# Build full package with dependencies (NEVER CANCEL - wait for completion)  
mvn package -DskipTests
# Takes ~3-5 seconds when dependencies already downloaded
# Takes ~18-20 seconds on first run (downloads dependencies)
# Set timeout to 120+ seconds for safety
```

### Test Commands
```bash
# Run tests (EXPECT 1 FAILURE - this is normal)
mvn test
# Takes ~7 seconds. NEVER CANCEL - wait for completion
# Set timeout to 60+ seconds
# NOTE: The test intentionally fails - it's a placeholder that expects output.html but doesn't generate it
```

### Generate Documentation (Primary Use Case)
```bash
# Generate documentation using the doclet (CORE FUNCTIONALITY)
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar \
  -sourcepath ./src/main/java \
  -subpackages at.videc
# Takes ~1 second. Generates output.html in project root
```

## Validation

### Manual Testing Requirements
Always validate changes by running this complete workflow:

1. **Build the project**:
   ```bash
   mvn clean compile package -DskipTests
   ```

2. **Generate documentation**:
   ```bash
   javadoc -doclet at.videc.DoomDoclet -docletpath target/classes:target/dependencies/gson-2.8.9.jar -classpath target/dependencies/gson-2.8.9.jar -sourcepath ./src/main/java -subpackages at.videc
   ```

3. **Verify output**:
   - Check that `output.html` exists and is not empty: `ls -la output.html`
   - Verify HTML structure contains tree data: `grep "generateTree" output.html`

### Expected Results
- `output.html` should be ~5KB and contain inlined CSS, JavaScript, and package/class data
- Should include classes: DoomDoclet, Main, BOMB from at.videc package and TestClass1, TestClass2 from at.videc.dummy package
- Generated HTML includes search functionality and collapsible tree navigation

## Project Structure

### Key Directories and Files
```
src/main/java/at/videc/     # Main Java source code
├── DoomDoclet.java         # Main doclet implementation (extends StandardDoclet)
├── Main.java               # Entry point class  
├── BOMB.java               # Utility class
└── dummy/                  # Test classes for documentation generation
    ├── TestClass1.java
    └── TestClass2.java

src/main/resources/         # Web assets (inlined into output)
├── stylesheets/            # CSS files
│   ├── common.css          # Global styles
│   └── tree.css            # Tree component styles
├── javascript/             # JavaScript files  
│   └── tree.js             # Tree component functionality
└── index.template.html     # HTML template (not currently used)

src/test/java/at/videc/     # Test files
└── DoomDocTest.java        # Test (currently fails - placeholder only)

pom.xml                     # Maven configuration
target/                     # Build output directory
├── classes/                # Compiled Java classes
├── dependencies/           # Downloaded JAR dependencies
└── doomdoc-0.0.1-SNAPSHOT.jar  # Built JAR file
```

### Build Artifacts Created
- `target/classes/` - Compiled Java classes including the doclet
- `target/dependencies/gson-2.8.9.jar` - Required dependency for JSON processing  
- `target/doomdoc-0.0.1-SNAPSHOT.jar` - Main project JAR
- `output.html` - Generated documentation (created in project root)

## Common Issues and Solutions

### Maven Warnings (NON-CRITICAL)
The build shows these warnings but they don't affect functionality:
- Duplicate maven-surefire-plugin declaration in pom.xml
- Missing version for maven-compiler-plugin
- Platform encoding warnings (build still works correctly)

### Test Failure (EXPECTED)
- `DoomDocTest.testDoomDoclet` always fails because it's a placeholder test
- This is normal and does not indicate a problem with the project
- Tests expect output.html to exist but don't actually run the doclet to create it

### Dependencies
- **Gson 2.8.9**: Required for JSON serialization of documentation data
- **JUnit 4.12**: For testing (though tests are currently placeholder)

## Development Guidelines

### Code Organization
- Main doclet logic is in `DoomDoclet.java`
- UI components: one JavaScript file and one CSS file per component
- No external JavaScript or CSS libraries are used
- All assets are inlined into the generated HTML

### Key Design Principles  
- Simplicity and performance
- Single HTML file output
- JSDuck-like UI design
- Modular, maintainable code following SOLID principles

### Making Changes
1. **Always run the full build and validation after changes**
2. **Test doclet generation** - the core functionality must work
3. **Check generated HTML structure** - ensure tree data and styling are correct
4. **For UI changes**: Modify files in `src/main/resources/stylesheets/` or `src/main/resources/javascript/`
5. **For doclet logic**: Modify `src/main/java/at/videc/DoomDoclet.java`

## Quick Reference Commands

```bash
# Full clean build and test workflow
mvn clean compile package -DskipTests && \
javadoc -doclet at.videc.DoomDoclet -docletpath target/classes:target/dependencies/gson-2.8.9.jar -classpath target/dependencies/gson-2.8.9.jar -sourcepath ./src/main/java -subpackages at.videc && \
ls -la output.html

# Check if doclet is working
grep "generateTree" output.html

# View project structure
find src -name "*.java" -o -name "*.css" -o -name "*.js" | head -20
```

## Timeout Guidelines
- **NEVER CANCEL** builds or long-running commands
- Use timeouts of **60+ seconds** for compile operations  
- Use timeouts of **120+ seconds** for package operations (first run)
- Documentation generation is fast (~1 second) but always wait for completion