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
 * DoomDoclet generates HTML documentation with a tree view of packages and classes.
 */
public class DoomDoclet extends StandardDoclet {

    /**
     * Generates HTML documentation with a tree view of packages and classes.
     * @param environment from which essential information can be extracted
     * @return true if the doclet ran without error
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

        // Determine project name from root package
        String projectName = determineProjectName(packageNames);

        html.append("<div class=\"title-bar\"><div class=\"title-bar-content\">").append(projectName).append("</div></div>");
        html.append("<div class=\"container\">");
        html.append("<div class=\"sidebar\"><input type=\"text\" id=\"search\" placeholder=\"Search...\" onkeyup=\"search()\">");
        html.append("<ul id=\"packageTree\"></ul></div>");
        html.append("<div class=\"content\"><h1 id=\"docTitle\">Documentation</h1><div id=\"docContent\"></div></div>");
        html.append("</div></body></html>");

        // Generate tree view for packages and classes
        // Using full DTO structure for rich documentation display
        html.append("<script>generateTree(").append(packageTree.toCompactJson()).append(");</script>");

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
     * Determines the project name from the set of package names.
     * Uses the root package name to derive a meaningful project title.
     * @param packageNames set of all package names in the documentation
     * @return a formatted project name for the title bar
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
     * Initializes the doclet with the given locale and reporter.
     * @param locale the locale to use
     * @param reporter the reporter for logging messages
     */
    @Override
    public void init(Locale locale, Reporter reporter) {
        super.init(locale, reporter);
    }
}