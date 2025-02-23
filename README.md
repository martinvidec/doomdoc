# Most important information for Github Copilot
When generating code make sure that the requirements given in this file are fulfilled and everything complies with the information given in this file.
The README.md file is the most important document in this project.
It contains all the information about the project.

- It contains a description of the project.
- It contains all the features of the project.
- It contains all the requirements for the project.
- It contains all the remarks for Github Copilot.
- It contains all the information about the project structure.
- It contains all the information about the project design.
- It contains all the information about the project architecture.
- It contains all the information about the project implementation.
- It contains all the information about the project testing.
- It contains all the information about the project deployment.
- It contains all the information about the project development.
- It contains all the information about the project security.
- It contains all the information about the project privacy.
- It contains all the information about the project compliance.
- It contains all the information about the project standards. 
- It contains all the information about the project guidelines.
- It contains all the information about the project best practices.
- It contains all the information about the project recommendations.


# Description of Doomdoc
Java doclet generator

Doomdoc is a Java doclet generator that generates searchable HTML documentation from Java source code. It is designed to be used with the javadoc tool that comes with the Java Development Kit (JDK).

Doomdoc itself is a maven project. Dependencies are managed by maven. The project is built using maven.

Usage:
```sh
javadoc -doclet at.videc.DoomDoclet -docletpath target/classes:target/dependencies/gson-2.8.9.jar -classpath target/dependencies/gson-2.8.9.jar -sourcepath ./src/main/java -subpackages at.videc
```

## Features
- Generates a single HTML file that contains all the documentation for a package.
- Generates a search index that allows you to search for classes, methods, and fields.
- All Javascript and CSS is inlined in the HTML file.
- The UI consists of 3 parts
    - A facetted search on the top which allows you to filter the elements.
    - A component tree on the left side which contains elements for packages and classes.
    - A detail view on the right side which displays the documentation for the selected element.
- Doomdoc's design principles are simplicity and performance.
- Doomdoc uses no external javascript libraries.

## Requirements
- Language Level is Java 11
- Javascript files are maintained in the src/main/resources/javascript folder. These files are included inlined in the HTML file.
- Exactly one Javascript file is used per component.
- Styles are maintained in src/main/resources/stylesheets. These files are included inlined in the HTML file.
- Exactly one CSS file is used per component.
- Don't use any external javascript libraries.
- Don't use any external CSS libraries.
- The UI should look and feel like JSDuck.

### Requirements for the component tree
- The tree has one root element.
- The root element may have a detail view.
- Classes are leafs
- Classes have a detail view.
- Packages are nodes
- Packages have a detail view.
- Nodes may contain other nodes or leafs.
- The tree should treat Java packages as nodes. Given two classes in the same package, there should be one node for the package and two leafs for the classes.
- Child nodes are only visible when the parent node is expanded.
- The root node is always expanded.
- Child nodes are indentend.
- leafs must not be visible in the tree when the parent node is collapsed.
- Search results are highlighted in the tree and parent nodes of search results are expanded.
- The tree is expandable and collapsible.
- The tree is searchable.
- The tree is filterable.
- The tree is scrollable.
- The tree is resizable.
- The Code for the tree is in src/main/resources/javascript/tree.js
- The Styles for the tree are in src/main/resources/stylesheets/tree.css

## Remarks for Github Copilot
- This project is a Java project.
- This project is a Maven project.
- Write modular code.
- Write code that is easy to understand.
- Write code that is easy to maintain.
- Write code that is easy to extend.
- Write code that is easy to test.
- Write code that is easy to debug.
- Use Principles like SOLID, DRY, KISS, etc...

