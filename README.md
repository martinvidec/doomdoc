# DoomDoc

A modern Java documentation generator that creates beautiful, searchable, single-file HTML documentation from your Java source code.

![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

## What is DoomDoc?

DoomDoc is a custom Javadoc doclet that generates comprehensive, interactive HTML documentation. Unlike standard Javadoc, DoomDoc produces a **single, self-contained HTML file** with all JavaScript and CSS inlined, making it perfect for distribution, offline viewing, or embedding in documentation systems.

### Key Features

✨ **Single File Output** - Everything in one portable HTML file
🔍 **Real-time Search** - Instant filtering across packages, classes, and members
📦 **Package Tree Navigation** - Collapsible hierarchical view
📝 **Rich JavaDoc Support** - Full parsing of all JavaDoc tags and HTML formatting
🎨 **Compact Design** - High-density layout optimized for technical documentation
⚡ **Fast & Lightweight** - No external dependencies, pure JavaScript
🎯 **Member Details** - Fields, methods, constructors with full signatures
🏷️ **Annotations & Generics** - Complete type parameter and annotation support

## Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone https://github.com/martinvidec/doomdoc.git
cd doomdoc
```

2. Build the project:
```bash
mvn clean package -DskipTests
```

This creates `doomdoc-0.0.1-SNAPSHOT.jar` and copies dependencies to `target/dependencies/`.

### Usage

Generate documentation for your Java project:

```bash
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar \
  -sourcepath ./src/main/java \
  -subpackages your.package.name
```

This generates `output.html` in your project root.

#### Example: Document DoomDoc Itself

```bash
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar \
  -sourcepath ./src/main/java \
  -subpackages at.videc
```

### Viewing Documentation

Simply open `output.html` in any modern web browser. No server required!

## Features in Detail

### Documentation Extraction

DoomDoc extracts comprehensive information from your Java code:

- **All Member Types**: Fields, methods, constructors, enum constants, annotation elements
- **Full JavaDoc**: All tags (@param, @return, @throws, @see, @since, @author, @deprecated, etc.)
- **Type Information**: Generic type parameters, wildcards, bounds
- **Modifiers**: Access levels (public, private, protected), static, final, abstract, etc.
- **Annotations**: Complete annotation metadata with values
- **Inheritance**: Superclasses and implemented interfaces
- **HTML in JavaDoc**: Preserves HTML formatting (`<p>`, `<pre>`, `<ul>`, etc.)
- **Inline Tags**: Processes `{@link}`, `{@code}`, `{@literal}`

### User Interface

The generated documentation features a clean, efficient 3-panel layout:

1. **Search Bar** (top) - Real-time filtering as you type
2. **Package Tree** (left) - Expandable/collapsible navigation
3. **Detail View** (right) - Complete member documentation

**Tree Navigation:**
- Packages shown as expandable nodes
- Classes, interfaces, enums, and annotations as leaves
- Click any element to view its documentation
- Search automatically expands parent packages

**Detail View:**
- Class/interface header with inheritance
- Type parameters and annotations
- Grouped member sections (Fields, Constructors, Methods)
- Full method signatures with parameters and exceptions
- Complete JavaDoc with all tags

### Design Philosophy

DoomDoc follows these principles:

- **Simplicity**: Single file, no external dependencies, pure JavaScript
- **Performance**: Fast loading, instant search, minimal overhead
- **Density**: Compact layout shows maximum information per screen
- **Portability**: Works offline, easy to distribute and archive
- **Familiarity**: JSDuck-inspired design for developer comfort

## Configuration

### Custom Output Location

By default, `output.html` is generated in the current directory. To specify a different location, you can redirect the output or use the `-d` option:

```bash
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar \
  -sourcepath ./src/main/java \
  -subpackages your.package.name \
  -d ./docs
```

### Multiple Packages

Document multiple packages by separating them with colons:

```bash
-subpackages com.example.api:com.example.impl:com.example.util
```

### Include/Exclude Patterns

Use standard javadoc options:

```bash
-subpackages com.example -exclude com.example.internal:com.example.test
```

## Project Structure

```
doomdoc/
├── src/main/java/at/videc/
│   ├── DoomDoclet.java              # Main doclet entry point
│   └── bomblet/
│       ├── PackageTree.java         # Package organization
│       ├── TypeElementConverter.java # Member extraction
│       └── dto/                     # Data model classes
├── src/main/resources/
│   ├── javascript/tree.js           # UI logic
│   └── stylesheets/                 # CSS files
│       ├── common.css               # Global styles
│       ├── tree.css                 # Tree navigation
│       └── detail.css               # Detail view
├── target/
│   ├── classes/                     # Compiled doclet
│   └── dependencies/                # Gson library
└── output.html                      # Generated documentation
```

## Development

For developers who want to contribute or customize DoomDoc, see [CLAUDE.md](CLAUDE.md) for detailed technical documentation including:

- Architecture and design patterns
- Build commands and validation workflow
- Component specifications
- Data structures (JSON schema)
- Development guidelines

## Dependencies

- **Gson 2.8.9**: JSON serialization
- **JDK 11+**: Doclet API and compilation

No runtime dependencies - the generated HTML is completely standalone.

## Troubleshooting

### Common Issues

**"Error: Could not find or load main class"**
- Ensure you've run `mvn package` to build the doclet
- Check that `target/classes` and `target/dependencies/gson-2.8.9.jar` exist

**"No source files for package"**
- Verify your `-sourcepath` points to the correct directory
- Check that `-subpackages` matches your actual package names

**"Module access error" (Java 17+)**
- This is normal - DoomDoc handles Java 17 module restrictions automatically

**Empty output.html**
- Ensure your source files have at least one public class
- Check that javadoc can parse your source files

## Examples

### Basic Project
```bash
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar \
  -sourcepath ./src/main/java \
  -subpackages com.mycompany.myproject
```

### With External Dependencies
```bash
javadoc -doclet at.videc.DoomDoclet \
  -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
  -classpath target/dependencies/gson-2.8.9.jar:lib/external-lib.jar \
  -sourcepath ./src/main/java \
  -subpackages com.mycompany
```

### Maven Integration

Add to your `pom.xml`:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-javadoc-plugin</artifactId>
      <version>3.4.1</version>
      <configuration>
        <doclet>at.videc.DoomDoclet</doclet>
        <docletArtifact>
          <groupId>at.videc</groupId>
          <artifactId>doomdoc</artifactId>
          <version>0.0.1-SNAPSHOT</version>
        </docletArtifact>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Then run: `mvn javadoc:javadoc`

## License

MIT License - see LICENSE file for details.

## Development Workflow

DoomDoc uses an integrated workflow with JIRA and GitHub for structured development. See [WORKFLOW.md](WORKFLOW.md) for complete details.

### Quick Setup

1. **Install Git Hooks** (validates branch names and commit messages):
   ```bash
   ./scripts/setup-hooks.sh
   ```

2. **Branch Naming** - Always include JIRA ticket reference:
   ```bash
   git checkout -b feature/DOOM-123-add-dark-mode
   git checkout -b bugfix/DOOM-456-fix-search-bug
   ```

3. **Commit Format** - Start with JIRA ticket:
   ```bash
   git commit -m "DOOM-123: Add dark mode toggle functionality"
   ```

4. **Pull Request Title** - Include JIRA reference:
   ```
   [DOOM-123] Add dark mode toggle functionality
   ```

### Automated Validation

GitHub Actions automatically:
- Validates PR titles contain JIRA ticket reference
- Validates branch names follow naming convention
- Builds project and generates documentation
- Can optionally update JIRA status (see `.github/JIRA_API_SETUP.md`)

## Contributing

Contributions are welcome! Please read [CLAUDE.md](CLAUDE.md) for development guidelines and [WORKFLOW.md](WORKFLOW.md) for the complete development workflow.

**JIRA Project Key:** `DOOM`

1. Pick an open JIRA ticket or create a new one
2. Create your feature branch (`git checkout -b feature/DOOM-XXX-description`)
3. Install Git hooks (`./scripts/setup-hooks.sh`)
4. Commit your changes (`git commit -m 'DOOM-XXX: Description'`)
5. Push to the branch (`git push origin feature/DOOM-XXX-description`)
6. Open a Pull Request with title `[DOOM-XXX] Description`

## Roadmap

- [ ] Gradle plugin
- [ ] Dark/light theme toggle
- [ ] Export to PDF
- [ ] Cross-reference linking
- [ ] Custom CSS themes
- [ ] Multi-project documentation aggregation

## Support

- **Documentation**: See [CLAUDE.md](CLAUDE.md) for technical details
- **Issues**: [GitHub Issues](https://github.com/martinvidec/doomdoc/issues)
- **Questions**: Open a discussion on GitHub

---

**Made with ☕ and Java**
