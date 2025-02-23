package at.videc;

import com.google.gson.Gson;
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

public class DoomDoclet extends StandardDoclet {

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
        html.append("</head><body><div class=\"container\">");
        html.append("<div class=\"sidebar\"><input type=\"text\" id=\"search\" placeholder=\"Search...\" onkeyup=\"search()\">");
        html.append("<ul id=\"packageTree\"></ul></div>");
        html.append("<div class=\"content\"><h1 id=\"docTitle\">Documentation</h1><div id=\"docContent\"></div></div>");
        html.append("</div></body></html>");

        // Group classes by package
        Map<String, List<String>> packages = new HashMap<>();
        List<TypeElement> typeElements = environment.getIncludedElements().stream()
                .filter(e -> e instanceof TypeElement)
                .map(e -> (TypeElement) e)
                .collect(Collectors.toList());

        for (TypeElement typeElement : typeElements) {
            String packageName = environment.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
            String className = typeElement.getSimpleName().toString();
            packages.computeIfAbsent(packageName, k -> new ArrayList<>()).add(className);
        }

        // Generate tree view for packages and classes
        html.append("<script>generateTree(").append(new Gson().toJson(packages)).append(");</script>");

        // Write HTML to file
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.html"))) {
            writer.write(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void init(Locale locale, Reporter reporter) {
        super.init(locale, reporter);
    }
}