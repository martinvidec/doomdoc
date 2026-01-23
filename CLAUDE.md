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

**JIRA Integration:**
- JIRA Project Name: DoomDoc
- JIRA Project Key: DOOM
- JIRA URL: https://rx451g.atlassian.net

**Confluence Integration:**
- Confluence Space: DoomDoc
- Confluence URL: https://rx451g.atlassian.net/wiki/spaces/DoomDoc

## Quick Navigation

- [Component Map](#component-map) - Find code quickly by functionality
- [Common Tasks](#common-modification-patterns) - How-to guides for typical tasks
- [Architecture](#architecture) - System design and data flow
- [Build Commands](#build-and-test-commands) - Compile, test, and generate docs
- [Workflow](#development-workflow-with-jiraconfluence-integration) - Development process
- [Search Keywords](#search-keywords) - Find information by topic
- [Recent Changes](#recent-changes) - Latest architectural updates
- [External Docs](#external-documentation) - Confluence and JIRA links
- [Data Structures](DATA_STRUCTURES.md) - Complete JSON schema reference

## Development Workflow with Jira/Confluence Integration

This workflow leverages the Jira and Confluence MCP integration to streamline development and maintain synchronized documentation.

**IMPORTANT: Use the jira-confluence-workflow Agent**

To minimize context usage in the main conversation, **ALWAYS use the `jira-confluence-workflow` agent** for Jira/Confluence operations:

- **Session Start:** Use agent to check for available tasks
- **Task Creation:** Use agent to create Jira tasks + Confluence specs
- **Task Updates:** Use agent to transition tasks, add comments
- **PR Linking:** Use agent to link PRs to Jira tasks

**Example usage:**
```
User: "What tasks are available?"
Assistant: Uses Task tool with subagent_type='jira-confluence-workflow'

User: "Create a new task for dark mode"
Assistant: Uses Task tool with subagent_type='jira-confluence-workflow'
```

The agent has access to all MCP Jira/Confluence tools and knows the complete workflow. This keeps the main conversation focused on implementation.

### Session Start Protocol

At the beginning of each Claude Code session, follow these steps:

1. **Check Repository Status**
   ```bash
   git status
   git fetch origin
   git log --oneline HEAD..origin/main  # Check if behind main
   ```
   - Ensure the repository is clean and up-to-date with remote
   - Pull latest changes if needed: `git pull origin main`

2. **Query Jira for Tasks (via Agent)**
   - Use `jira-confluence-workflow` agent to search for open tasks in DOOM project
   - Agent identifies tasks with status "To Do" or "In Progress"
   - Agent fetches task details and presents them

3. **Ask User for Task Selection**
   - Agent presents available tasks to the user
   - For "In Progress" tasks, agent notes the last update and current state
   - User selects which task to work on next

### Branch Naming Convention

**Format:** `{type}/DOOM-{ticket-number}-{short-description}`

**Branch Types:**
- `feature/` - New features or enhancements
- `bugfix/` - Bug fixes
- `improvement/` - Improvements to existing features
- `docs/` - Documentation-only changes
- `refactor/` - Code refactoring and maintenance tasks (dependencies, cleanup, tooling)

**Examples:**
```bash
feature/DOOM-123-add-dark-mode
bugfix/DOOM-456-fix-search-highlighting
improvement/DOOM-789-optimize-tree-rendering
docs/DOOM-567-api-documentation
refactor/DOOM-234-update-maven-dependencies
```

**Rules:**
- Always include the JIRA ticket key (DOOM-XXX)
- Use lowercase with hyphens for readability
- Keep description short (3-5 words max)
- Description should be meaningful but concise
- Use only the branch types listed above (validated by git hooks)
- For maintenance tasks (dependencies, cleanup, tooling), use `refactor/`
- Compatible with GitHub Actions validation

### Implementation Workflow

#### 1. Create Feature Branch

```bash
git checkout main
git pull origin main
git checkout -b feature/DOOM-XXX-short-description
```

#### 2. Create Technical Specification (BEFORE Implementation)

**Use the `jira-confluence-workflow` agent to:**
- Create Confluence technical specification
- Link specification to Jira task
- Transition task to "In Progress"
- Add comment with branch name and spec link

The agent will create a complete specification with sections:
- Overview, Requirements, Technical Approach
- Implementation Details, Testing Strategy
- Risks and Considerations

**Manual alternative** (if agent unavailable):
- Create Confluence page manually
- Use `jira_create_remote_issue_link` to link spec
- Use `jira_transition_issue` to move to "In Progress"

#### 3. Update Jira Task Status (via Agent)

For task transitions, use the `jira-confluence-workflow` agent:
- Agent handles: transitions, comments, worklog
- Keeps main conversation context clean
- Ensures consistent comment formatting

#### 4. Implement the Feature/Fix

Follow the technical specification and development guidelines:
- Write code following SOLID, DRY, KISS principles
- Validate frequently using build commands
- Update specification in Confluence if approach changes
- Add comments to Jira task for significant progress or blockers

#### 5. Update Jira During Implementation (via Agent)

**Use `jira-confluence-workflow` agent for progress updates:**
- Agent adds formatted comments to track progress
- Agent updates task status if needed
- Agent links related tasks if dependencies discovered

**Example Progress Comments:**
```
"Implemented core functionality for dark mode toggle in tree.css and common.css"
"Encountered issue with state persistence - investigating localStorage approach"
"Completed implementation, ready for testing"
```

#### 6. Complete Implementation

Before creating PR:
- Run full validation workflow:
  ```bash
  mvn clean compile package -DskipTests
  javadoc -doclet at.videc.DoomDoclet ...
  # Verify output.html
  ```
- Update Confluence specification with any final changes
- Add completion comment to Jira task

#### 7. Create Pull Request

```bash
git add .
git commit -m "DOOM-XXX: Brief description of changes

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>"

git push origin feature/DOOM-XXX-short-description
```

Create PR using GitHub CLI:
```bash
gh pr create --title "[DOOM-XXX] Feature/Fix Name" --body "$(cat <<'EOF'
## Summary
- Bullet point summary of changes
- Link to Confluence spec: [Technical Specification](https://rx451g.atlassian.net/wiki/...)

## Jira Task
Closes DOOM-XXX

## Test Plan
- [ ] Build succeeds: `mvn clean package -DskipTests`
- [ ] Documentation generates: `javadoc -doclet at.videc.DoomDoclet ...`
- [ ] Manual testing completed
- [ ] No regressions in existing functionality

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

#### 8. Update Jira After PR Creation (via Agent)

**Use `jira-confluence-workflow` agent to:**
- Link PR to Jira task
- Add structured comment with PR details
- Optionally transition task to "In Review"

The agent ensures consistent PR linking and comment formatting.

### Documentation Strategy

**All documentation is created in Confluence, not in the repository.**

**Documentation Types:**

1. **Technical Specifications** (Before Implementation)
   - Location: Confluence under "Technical Specifications" parent page
   - One spec per JIRA task
   - Linked from Jira task

2. **User Documentation** (After Implementation)
   - Location: Confluence main pages
   - Update existing pages or create new ones
   - Reference from JIRA task for visibility

3. **API Documentation** (Generated)
   - Generated via DoomDoc itself
   - `output.html` not committed to repository
   - Can be uploaded as Confluence attachment if needed

4. **Code Comments** (Inline)
   - JavaDoc comments in source code (minimal, only where needed)
   - Focus on "why" not "what"

**DO NOT create:**
- Markdown documentation files in repository (except CLAUDE.md, README.md, DATA_STRUCTURES.md)
- Duplicate documentation in multiple locations
- Extensive inline code comments for self-explanatory code

### Resuming In-Progress Tasks

When starting a new session with a task "In Progress":

1. **Use `jira-confluence-workflow` agent to:**
   - Query task details
   - Read latest comments
   - Find Confluence spec link
   - Add "Resuming work" comment

2. **Check out the existing branch:**
   ```bash
   git fetch origin
   git checkout feature/DOOM-XXX-short-description
   git pull origin feature/DOOM-XXX-short-description
   ```

3. **Review recent commits** to understand what was completed

4. **Continue from where previous session left off**

### Important Notes

- **Always use `jira-confluence-workflow` agent for Jira/Confluence operations**
- **Always create Confluence spec BEFORE starting implementation**
- **Update Jira status transitions as you progress (via agent)**
- **Link all artifacts (branches, PRs, specs) in Jira task (via agent)**
- **Keep Confluence as single source of truth for documentation**
- **Use agent for Jira comments for progress updates and blockers**
- **Never create divergent documentation in multiple places**
- **Agent minimizes context usage in main conversation**

## Recent Changes

Track recent modifications for context. Update this section when making significant architectural changes or workflow updates.

### 2025-12-23

**Agent Template Synchronization Infrastructure**
- Added: `scripts/sync-agents.sh` for automatic agent synchronization
- Added: Pre-commit hook to sync `.claude/agents/` → `claude-templates/agents/`
- Purpose: Ensure agent modifications are preserved in repository
- Location: Agent templates in `claude-templates/agents/`
- Documentation: Added "Agent Template Synchronization" section to CLAUDE.md
- Impact: Agent configurations now automatically synced on commit

**Context Optimization:** Created `jira-confluence-workflow` agent
- Added: Specialized agent for all Jira/Confluence operations
- Location: `claude-templates/agents/jira-confluence-workflow.md`
- Purpose: Minimize context usage in main conversation
- Handles: Session start, task creation, spec creation, transitions, PR linking
- Updated: CLAUDE.md workflow documentation to reference agent
- Impact: Significantly reduced context consumption for Jira/Confluence interactions

**DOOM-10:** UI Redesign Phase 4 - Polish (Navigation & Animations)
- Implemented: Consistent 150ms ease-out transitions across all interactive elements
- Added: Smooth expand/collapse animations for tree navigation
- Added: Accessibility support for `prefers-reduced-motion`
- Updated: tree.css, detail.css, common.css
- Completed: Full UI Redesign series (Phases 1-4)

### 2025-12-21

**DOOM-4:** Enhance CLAUDE.md with navigation and search optimization
- Added: Quick Navigation section
- Added: Component Map with file:line references
- Added: Common Modification Patterns (how-to guides)
- Added: Search Keywords for findability
- Added: Recent Changes tracking (this section)
- Added: External Documentation links
- [Confluence Spec](https://rx451g.atlassian.net/spaces/DoomDoc/pages/13697068)

**DOOM-2:** Removed duplicate documentation
- Deleted: WORKFLOW.md, .github/README.md, .github/JIRA_API_SETUP.md, .github/copilot-instructions.md
- Updated: README.md references to point to CLAUDE.md
- Migrated: JIRA API Setup Guide to Confluence
- Repository now contains only: CLAUDE.md, README.md, DATA_STRUCTURES.md
- [Confluence Spec](https://rx451g.atlassian.net/wiki/spaces/DoomDoc/pages/13762584)

**DOOM-1:** Setup JIRA workflow integration
- Added: Git hooks for branch/commit validation
- Added: GitHub Actions for PR validation and JIRA integration
- Added: Comprehensive workflow documentation in CLAUDE.md
- Established: Confluence as documentation hub
- Created: Session start protocol for Claude Code

### Earlier Changes

- Initial project setup with Maven, Gson, basic doclet structure
- Implemented: Tree navigation, search functionality, JavaDoc parsing
- UI design: JSDuck-inspired 3-column layout
- Resource inlining: Single HTML file output

**Note:** Only document architectural changes, workflow updates, or major refactoring here. Regular feature additions and bug fixes should be tracked in JIRA/Confluence only.

## External Documentation

### Confluence Space

**Main Space:** [DoomDoc](https://rx451g.atlassian.net/wiki/spaces/DoomDoc)
- Technical Specifications (created before each task implementation)
- Architecture Decision Records (ADRs)
- User guides and how-to documentation

**Key Pages:**
- [JIRA API Integration Setup Guide](https://rx451g.atlassian.net/wiki/spaces/DoomDoc/pages/13762604)
- Technical Specifications for each DOOM-XXX task

### JIRA Project

**Project:** [DOOM - DoomDoc](https://rx451g.atlassian.net/browse/DOOM)
- **Project Key:** DOOM
- **Task Format:** DOOM-XXX
- **Workflow:** To Do → In Progress → Done
- **Check for:** "To Do" and "In Progress" tasks at session start

### GitHub Repository

**Repository:** [martinvidec/doomdoc](https://github.com/martinvidec/doomdoc)
- **Branch naming:** `{type}/DOOM-XXX-{description}`
- **PR titles:** Must include `[DOOM-XXX]`
- **GitHub Actions:** Validates PR titles, branch names, runs builds
- **Main branch:** `main` (target for all PRs)

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

## Component Map

Quick reference for finding code locations by functionality.

### Documentation Generation Pipeline

**1. DoomDoclet.java** (src/main/java/at/videc/DoomDoclet.java)
- Entry point: `run()` method at line ~50
- Orchestrates: Resource inlining, JSON generation, HTML output
- Key methods: `inlineResources()`, `generateDocumentation()`
- Related: All DTO classes, PackageTree, TypeElementConverter

**2. TypeElementConverter.java** (src/main/java/at/videc/bomblet/TypeElementConverter.java)
- Extraction engine for classes, methods, fields
- JavaDoc parsing at line ~200
- Returns: TypeInfo DTOs (ClassInfo, InterfaceInfo, EnumInfo, AnnotationInfo)
- Key methods: `convert()`, `extractJavaDoc()`, `extractMethods()`
- See: DATA_STRUCTURES.md for complete extraction schema

**3. PackageTree.java** (src/main/java/at/videc/bomblet/PackageTree.java)
- Organizes types by package hierarchy
- Builds: DocumentationModel structure
- Key methods: `organize()`, `buildTree()`
- Output: Nested package structure ready for JSON serialization

**4. DTO Classes** (src/main/java/at/videc/bomblet/dto/)
- Data models (see DATA_STRUCTURES.md for complete schema)
- TypeInfo.java - Base type representation
- MethodInfo.java - Method signatures with parameters
- FieldInfo.java - Field metadata
- JavaDocComment.java - Comment structure with all tags
- AnnotationInfo.java - Annotation metadata

### UI Rendering

**5. tree.js** (src/main/resources/javascript/tree.js)
- Package tree generation: `generateTree()` function at ~50
- Detail view rendering: `showDetail()` function at ~150
- Search functionality: `filterTree()` function at ~200
- Click handlers: Element selection and navigation
- DOM manipulation: Tree expansion/collapse

**6. Stylesheets** (src/main/resources/stylesheets/)
- **common.css** - Layout, global styles, color scheme, typography
- **tree.css** - Tree component: nodes, leafs, indentation, hover states
- **detail.css** - Detail view: members, JavaDoc, signatures, badges

### Common Code Locations

**JavaDoc Tag Support:**
- Extraction: TypeElementConverter.java:~200-300
- DTO: JavaDocComment.java
- Rendering: tree.js:showDetail() at ~150-200

**Tree Rendering Logic:**
- Generation: tree.js:generateTree() at ~50-150
- Styling: tree.css
- Node structure: Packages as nodes, classes as leafs

**Search/Filter:**
- Implementation: tree.js:filterTree() at ~200-250
- Auto-expand: Parent nodes revealed when child matches
- Highlighting: Matched elements get special CSS class

**Package Organization:**
- Logic: PackageTree.java:~30-100
- Structure: Hierarchical nesting based on package names
- Output: DocumentationModel with nested PackageInfo objects

**HTML Template Generation:**
- Template building: DoomDoclet.java:~100-200
- Resource inlining: DoomDoclet.java:inlineResources()
- JSON embedding: Serialized model injected into HTML

## Common Modification Patterns

How-to guides for typical development tasks.

### Add Support for New JavaDoc Tag

**When:** You need to parse and display a new @tag in JavaDoc comments

**Steps:**
1. **Extract tag** - TypeElementConverter.java:~200
   - Add tag parsing in `extractJavaDoc()` method
   - Example: Parse `@customTag value` from DocCommentTree
   - Store in JavaDocComment DTO

2. **Update DTO** - JavaDocComment.java
   - Add field for new tag: `private String customTag;`
   - Update constructor and add getter method
   - See DATA_STRUCTURES.md for schema conventions

3. **Render tag** - tree.js:showDetail()
   - Add rendering logic around line ~170
   - Example: `html += '<div class="tag-custom">' + doc.customTag + '</div>';`
   - Style in detail.css: `.tag-custom { ... }`

4. **Test** - Create test class with new tag
   - Add to src/test/java/at/videc/dummy/
   - Include /**  * @customTag test value  */ in JavaDoc
   - Run: `mvn clean compile package -DskipTests && javadoc...`
   - Verify in output.html

**Reference:** DATA_STRUCTURES.md for JavaDoc tag structure

### Add UI Component/Feature

**When:** Adding new functionality to the tree or detail view

**Steps:**
1. **JavaScript** - tree.js
   - Add function for new feature
   - Hook into existing event handlers or create new ones
   - Example: Search is in `filterTree()` at line ~200
   - Keep code modular and well-commented

2. **CSS** - tree.css or detail.css
   - Add styles for new component
   - Follow existing naming conventions: `.component-name`
   - Use existing color scheme from common.css
   - Test responsiveness at different viewport sizes

3. **Test rendering:**
   ```bash
   mvn clean compile package -DskipTests
   javadoc -doclet at.videc.DoomDoclet \
     -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
     -classpath target/dependencies/gson-2.8.9.jar \
     -sourcepath ./src/main/java \
     -subpackages at.videc
   open output.html  # or your browser
   ```

**Note:** All JavaScript/CSS is inlined at generation time. Full rebuild required to see changes.

### Modify Extraction Logic

**When:** Changing what information is extracted from Java source files

**Steps:**
1. **Update converter** - TypeElementConverter.java
   - Modify extraction methods for your target element type
   - Fields: around line ~150
   - Methods: around line ~250
   - Constructors: around line ~300
   - Preserve existing extraction for backward compatibility

2. **Update DTO** - Corresponding DTO in bomblet/dto/
   - Add or modify fields in the appropriate DTO class
   - Example: Add field to MethodInfo.java for new method metadata
   - Update DATA_STRUCTURES.md if this changes the schema

3. **Update rendering** - tree.js:showDetail()
   - Modify detail view rendering to display new data
   - Add appropriate HTML structure and CSS classes
   - Consider edge cases (null values, empty lists, etc.)

4. **Validate:**
   ```bash
   mvn clean compile package -DskipTests
   javadoc -doclet at.videc.DoomDoclet ...
   grep "newField" output.html  # Check if new data appears
   ```

**Important:** Always check DATA_STRUCTURES.md before and after schema changes.

### Debug Doclet Issues

**When:** Doclet fails to run or generates incorrect output

**Common Issues:**

**Build fails:**
- Run `mvn clean` to remove stale artifacts
- Then `mvn compile` to rebuild fresh
- Check for Java syntax errors in source files
- Verify pom.xml dependencies

**Empty output.html:**
- Ensure Gson JAR exists: `ls target/dependencies/gson-2.8.9.jar`
- Check docletpath includes both: `target/classes:target/dependencies/gson-2.8.9.jar`
- Verify source path is correct: `./src/main/java`
- Run with `-verbose` flag for detailed output

**Missing data in output:**
- Add debug output in TypeElementConverter: `System.out.println()`
- Check if extraction methods are called: Add logging
- Verify DTO objects are populated correctly
- Inspect JSON in output.html source (search for "generateTree")

**CSS not applied:**
- Verify CSS files exist in: `src/main/resources/stylesheets/`
- Check file names: common.css, tree.css, detail.css
- Rebuild after CSS changes: `mvn clean compile package`
- Inspect `<style>` tags in output.html to confirm CSS was inlined

**Debug steps:**
1. Add `System.out.println()` statements in DoomDoclet.java
2. Rebuild: `mvn clean compile package -DskipTests`
3. Run javadoc command and watch console output
4. Inspect output.html source for embedded JSON/CSS/JS
5. Use browser dev tools to check JavaScript console for errors

## Search Keywords

Quick lookup by topic - helps find relevant information quickly.

### By Functionality

- **JavaDoc parsing, tag extraction:** TypeElementConverter.java, JavaDocComment.java
- **Tree rendering, navigation:** tree.js, tree.css
- **Package organization, hierarchy:** PackageTree.java, DocumentationModel.java
- **Search, filtering:** tree.js:filterTree() at ~200-250
- **Detail view, member display:** tree.js:showDetail() at ~150-200, detail.css
- **CSS styling, layout:** common.css, tree.css, detail.css
- **JSON generation, serialization:** DoomDoclet.java, Gson usage
- **Resource inlining:** DoomDoclet.java:inlineResources()
- **Build, compilation, Maven:** pom.xml, Build Commands section above
- **Testing, validation:** See "Validation Workflow" section

### By File Type

- **Doclet logic:** DoomDoclet.java, TypeElementConverter.java, PackageTree.java
- **Data models (DTOs):** bomblet/dto/*.java (see DATA_STRUCTURES.md)
- **UI components:** tree.js, *.css files
- **Configuration:** pom.xml
- **Documentation:** CLAUDE.md (this file), DATA_STRUCTURES.md, README.md
- **Tests:** src/test/java/at/videc/DoomDocTest.java, dummy/*.java

### By Technology

- **Maven:** Build system, dependency management, pom.xml
- **JavaDoc API:** Doclet framework, TypeElement, ExecutableElement, DocCommentTree
- **Gson:** JSON serialization (version 2.8.9), HTML escaping disabled
- **JavaScript:** Vanilla JS, no frameworks, tree.js
- **CSS:** Pure CSS, no preprocessors, common.css/tree.css/detail.css

### By Common Task

- **Add new JavaDoc tag:** See "Common Modification Patterns" → "Add Support for New JavaDoc Tag"
- **Add UI feature:** See "Common Modification Patterns" → "Add UI Component/Feature"
- **Change extraction logic:** See "Common Modification Patterns" → "Modify Extraction Logic"
- **Debug issues:** See "Common Modification Patterns" → "Debug Doclet Issues"
- **Understand data flow:** See "Architecture" → "Data Flow"
- **Find file locations:** See "Component Map" above

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

### When to Build and Test

**IMPORTANT: Only build and test when source code or configuration changes.**

**Requires build (`mvn clean compile package -DskipTests`):**
- Changes to Java source files (*.java in src/main/java/ or src/test/java/)
- Changes to UI resources (*.js, *.css in src/main/resources/)
- Changes to pom.xml (dependencies, build configuration)
- Changes that affect the generated output.html

**Does NOT require build:**
- Changes to documentation files only (CLAUDE.md, README.md, DATA_STRUCTURES.md)
- Changes to Confluence pages
- Jira task updates
- Git/GitHub configuration (.gitignore, .github/workflows/)
- Branch operations, commits, PR descriptions

**Context savings:**
Skip unnecessary builds to reduce token usage and improve response time. Documentation-only changes can be committed directly without validation workflow.

### Known Issues
- Duplicate maven-surefire-plugin declaration in pom.xml (non-critical warning)
- DoomDocTest.testDoomDoclet always fails - this is expected (placeholder test)
- Tests expect output.html but don't run doclet to create it

### Agent Template Synchronization

**Important:** Changes to Claude Code agents must be synchronized between two locations:

**Active agents:**
- Location: `.claude/agents/` (local, not tracked in git)
- Used by: Claude Code during execution

**Template agents:**
- Location: `claude-templates/agents/` (tracked in git)
- Purpose: Preserve agent configurations in repository

**Synchronization mechanism:**

1. **Automatic sync (recommended):**
   - Pre-commit hook automatically syncs `.claude/agents/` → `claude-templates/agents/`
   - No manual action needed when committing

2. **Manual sync:**
   ```bash
   ./scripts/sync-agents.sh
   ```
   - Use this if you want to check sync status
   - Or if you modified agents and want to see changes before committing

**When modifying agents:**
- Edit files in `.claude/agents/` during development
- Sync runs automatically on `git commit`
- Changed templates are staged automatically
- Verify with `git status` before pushing

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

# Agent Templates
claude-templates/agents/      # Claude Code agent templates (tracked in git)
├── jira-confluence-workflow.md
├── js-specialist.md
└── ui-design-specialist.md

.claude/agents/               # Active agents (not tracked, synced from templates)

# Scripts
scripts/
└── sync-agents.sh            # Sync agents from .claude to claude-templates

# Git Hooks
.git/hooks/
└── pre-commit                # Auto-sync agents before commit
```

## Timeout Recommendations

Maven commands can take time, especially on first run:
- Use 60+ second timeout for `mvn compile`
- Use 120+ second timeout for `mvn package` (first run downloads dependencies)
- Documentation generation is fast (~1 second)