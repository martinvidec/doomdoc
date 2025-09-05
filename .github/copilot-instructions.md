# Copilot Instructions for Doomdoc

## Project Overview
Doomdoc is a Java doclet generator that creates searchable HTML documentation from Java source code. It generates a single HTML file containing all documentation with inlined JavaScript and CSS, featuring a faceted search, component tree, and detail view interface.

## Technical Requirements & Constraints

### Language & Build System
- **Java 11** - Language level requirement
- **Maven** - Build system and dependency management
- Dependencies: Gson 2.8.9, JUnit 4.12

### Frontend Constraints
- **NO external JavaScript libraries** - All JavaScript must be custom-written
- **NO external CSS libraries** - All CSS must be custom-written
- All JavaScript and CSS files are inlined into the generated HTML
- Target look and feel: **JSDuck-style interface**

### Code Organization
- JavaScript files: `src/main/resources/javascript/` - exactly one file per component
- CSS files: `src/main/resources/stylesheets/` - exactly one file per component
- Main classes in package: `at.videc`

## Architecture & Design Principles

### Core Principles
- **Simplicity and Performance** - Primary design goals
- **Modular Code** - Write components that are easy to understand, maintain, extend, test, and debug
- **SOLID, DRY, KISS** principles must be followed

### UI Architecture (3-part interface)
1. **Faceted Search** (top) - Filter elements
2. **Component Tree** (left) - Navigate packages and classes
3. **Detail View** (right) - Display documentation

## Component-Specific Guidelines

### Tree Component (`tree.js` & `tree.css`)
The tree component has specific behavioral requirements:

**Structure:**
- One root element (may have detail view)
- Packages = nodes (have detail views, can contain nodes/leafs)
- Classes = leafs (have detail views, cannot contain children)
- Java packages should be treated as nodes with classes as leafs

**Behavior:**
- Root node always expanded
- Child nodes only visible when parent expanded
- Leafs invisible when parent collapsed
- Child nodes are indented
- Expandable/collapsible functionality
- Search results highlighted with parent nodes auto-expanded
- Searchable and filterable
- Scrollable and resizable

## Usage Context
The doclet is invoked via javadoc:
```bash
javadoc -doclet at.videc.DoomDoclet -docletpath target/classes:target/dependencies/gson-2.8.9.jar -classpath target/dependencies/gson-2.8.9.jar -sourcepath ./src/main/java -subpackages at.videc
```

## Code Quality Guidelines
- Write modular code with clear separation of concerns
- Follow Java naming conventions
- Ensure code is easily testable and debuggable
- Maintain consistent code style throughout the project
- Focus on readability and maintainability

## Key Files to Understand
- `DoomDoclet.java` - Main doclet implementation
- `tree.js` - Tree component logic
- `tree.css` - Tree component styling
- Generated HTML contains all inlined resources