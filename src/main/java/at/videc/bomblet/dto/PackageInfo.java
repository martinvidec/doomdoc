package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Java package with all its types.
 */
public class PackageInfo {

    /**
     * The fully qualified name of the package
     */
    private String name;

    /**
     * JavaDoc comment from package-info.java
     */
    private JavaDocComment javadoc;

    /**
     * All types (classes, interfaces, enums, annotations) in this package
     */
    private List<TypeInfo> types = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JavaDocComment getJavadoc() {
        return javadoc;
    }

    public void setJavadoc(JavaDocComment javadoc) {
        this.javadoc = javadoc;
    }

    public List<TypeInfo> getTypes() {
        return types;
    }

    public void setTypes(List<TypeInfo> types) {
        this.types = types;
    }
}
