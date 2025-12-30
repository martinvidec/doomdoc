package at.videc;

import at.videc.bomblet.PackageTree;
import at.videc.bomblet.TypeElementConverter;
import at.videc.bomblet.dto.TypeInfo;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.StandardDoclet;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A custom JavaDoc doclet that generates single-file HTML documentation with advanced navigation features.
 *
 * <p>DoomDoc creates a modern, interactive documentation interface with the following key features:</p>
 * <ul>
 *   <li><strong>Single-File Output:</strong> All CSS, JavaScript, and content embedded in one HTML file</li>
 *   <li><strong>Tree Navigation:</strong> Hierarchical package and class browser in sidebar</li>
 *   <li><strong>Faceted Search:</strong> Real-time autocomplete search across types, methods, and fields</li>
 *   <li><strong>Type Navigation:</strong> Clickable type references throughout documentation</li>
 *   <li><strong>Responsive Design:</strong> Optimized for desktop and mobile viewing</li>
 * </ul>
 *
 * <p><strong>Architecture:</strong></p>
 * <p>The doclet follows a pipeline architecture:</p>
 * <ol>
 *   <li>Extract type information via {@link TypeElementConverter}</li>
 *   <li>Organize types by package via {@link PackageTree}</li>
 *   <li>Generate search index for autocomplete</li>
 *   <li>Inline all resources (CSS/JS) into HTML</li>
 *   <li>Serialize model to JSON and embed in output</li>
 * </ol>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * javadoc -doclet at.videc.DoomDoclet \
 *   -docletpath target/classes:target/dependencies/gson-2.8.9.jar \
 *   -classpath target/dependencies/gson-2.8.9.jar \
 *   -sourcepath ./src/main/java \
 *   -subpackages at.videc
 * }</pre>
 *
 * @author DoomDoc Team
 * @version 1.0.0
 * @since 1.0.0
 * @see TypeElementConverter
 * @see PackageTree
 * @see jdk.javadoc.doclet.StandardDoclet
 */
public class DoomDoclet extends StandardDoclet {

    /**
     * Generates the complete HTML documentation from the provided DocletEnvironment.
     *
     * <p>This is the main entry point of the doclet. It orchestrates the entire documentation
     * generation process:</p>
     *
     * <ol>
     *   <li><strong>Resource Inlining:</strong> Reads and embeds all CSS and JavaScript files</li>
     *   <li><strong>Type Extraction:</strong> Converts all {@link TypeElement}s to {@link TypeInfo} DTOs</li>
     *   <li><strong>Tree Building:</strong> Organizes types into hierarchical package structure</li>
     *   <li><strong>Search Index:</strong> Builds searchable index of all documentation elements</li>
     *   <li><strong>HTML Generation:</strong> Creates single-file HTML with embedded data</li>
     *   <li><strong>File Output:</strong> Writes to {@code output.html} in project root</li>
     * </ol>
     *
     * <p><strong>Output Format:</strong></p>
     * <p>The generated HTML contains:</p>
     * <ul>
     *   <li>Embedded CSS from {@code src/main/resources/stylesheets/}</li>
     *   <li>Embedded JavaScript from {@code src/main/resources/javascript/}</li>
     *   <li>JSON model with complete documentation data</li>
     *   <li>Bootstrap code to initialize UI and search</li>
     * </ul>
     *
     * @param environment the doclet environment providing access to program structure and utilities
     * @return {@code true} if documentation generation succeeded, {@code false} on I/O or processing errors
     * @throws NullPointerException if environment is null
     * @see DocletEnvironment
     * @see #determineProjectName(Set)
     */
    @Override
    public boolean run(DocletEnvironment environment) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Documentation</title>");
        // Add CSS and JavaScript
        try {
            Files.walk(Paths.get("src/main/resources/stylesheets"))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String cssContent = new String(Files.readAllBytes(path));
                            html.append("<style>").append(cssContent).append("</style>");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            Files.walk(Paths.get("src/main/resources/javascript"))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String jsContent = new String(Files.readAllBytes(path));
                            html.append("<script>").append(jsContent).append("</script>");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        html.append("</head><body>");

        // Build package tree from type elements
        PackageTree packageTree = new PackageTree();
        TypeElementConverter converter = new TypeElementConverter(environment);

        List<TypeElement> typeElements = environment.getIncludedElements().stream()
                .filter(e -> e instanceof TypeElement)
                .map(e -> (TypeElement) e)
                .collect(Collectors.toList());

        // Collect all package names to determine project name
        Set<String> packageNames = new TreeSet<>();
        for (TypeElement typeElement : typeElements) {
            String packageName = environment.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
            packageNames.add(packageName);
            TypeInfo typeInfo = converter.convert(typeElement);
            packageTree.addType(packageName, typeInfo);
        }

        // Build search index after all types are added
        packageTree.buildSearchIndex();

        // Determine project name from root package
        String projectName = determineProjectName(packageNames);

        // Title bar with search
        html.append("<div class=\"title-bar\">");
        html.append("<div class=\"title-bar-content\">").append(projectName).append("</div>");
        html.append("<div class=\"title-bar-search-container\">");
        html.append("<input type=\"text\" id=\"globalSearch\" class=\"title-bar-search\" placeholder=\"Search...\" autocomplete=\"off\">");
        html.append("<button class=\"search-clear\" onclick=\"clearSearch()\" style=\"display: none;\">×</button>");
        html.append("<div id=\"searchDropdown\" class=\"search-dropdown\"></div>");
        html.append("</div>");
        html.append("</div>");

        // Container with sidebar and content
        html.append("<div class=\"container\">");
        html.append("<div class=\"sidebar\">");
        html.append("<ul id=\"packageTree\"></ul></div>");
        html.append("<div class=\"content\"><div id=\"docContent\"></div></div>");
        html.append("</div></body></html>");

        // Generate tree view and initialize search
        html.append("<script>");
        html.append("var model = ").append(packageTree.toCompactJson()).append(";");
        html.append("generateTree(model);");
        html.append("initializeSearch(model);");
        html.append("</script>");

        // Write HTML to file
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.html"))) {
            writer.write(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Determines an appropriate project name from the available package names.
     *
     * <p>This method analyzes the package structure to generate a meaningful title for the
     * documentation. It uses the shortest (root) package name as the basis for the title.</p>
     *
     * <p><strong>Algorithm:</strong></p>
     * <ol>
     *   <li>Find the shortest package name (typically the root package)</li>
     *   <li>Append " Documentation" to create the title</li>
     *   <li>Return "API Documentation" if no packages found</li>
     * </ol>
     *
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>{@code at.videc} → "at.videc Documentation"</li>
     *   <li>{@code com.example.myapp} → "com.example.myapp Documentation"</li>
     *   <li>Empty set → "API Documentation"</li>
     * </ul>
     *
     * @param packageNames a {@link Set} of all package names discovered during processing
     * @return a formatted string suitable for display in the title bar
     * @throws NullPointerException if packageNames is null
     */
    private String determineProjectName(Set<String> packageNames) {
        if (packageNames.isEmpty()) {
            return "API Documentation";
        }

        // Find the shortest package name (likely the root package)
        String rootPackage = packageNames.stream()
                .min(Comparator.comparingInt(String::length))
                .orElse("");

        if (rootPackage.isEmpty()) {
            return "API Documentation";
        }

        // Format the package name for display
        // Convert "at.videc" to "at.videc Documentation"
        // or "com.example.myproject" to "com.example.myproject Documentation"
        return rootPackage + " Documentation";
    }

    /**
     * Initializes the doclet with locale and reporter for internationalization and logging.
     *
     * <p>This method is called by the JavaDoc framework before {@link #run(DocletEnvironment)}.
     * It sets up the locale for message formatting and the reporter for diagnostic output.</p>
     *
     * <p><strong>Note:</strong> This implementation delegates to the superclass for standard
     * initialization. Custom initialization logic can be added here if needed.</p>
     *
     * @param locale the locale to use for formatting messages and output
     * @param reporter the reporter for emitting diagnostic messages (warnings, errors, notices)
     * @see StandardDoclet#init(Locale, Reporter)
     */
    @Override
    public void init(Locale locale, Reporter reporter) {
        super.init(locale, reporter);
    }
}