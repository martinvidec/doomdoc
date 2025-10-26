package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a constant in an enum type.
 */
public class EnumConstantInfo {

    /**
     * The name of the enum constant
     */
    private String name;

    /**
     * JavaDoc comment for this enum constant
     */
    private JavaDocComment javadoc;

    /**
     * Annotations applied to this enum constant
     */
    private List<AnnotationUsage> annotations = new ArrayList<>();

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

    public List<AnnotationUsage> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationUsage> annotations) {
        this.annotations = annotations;
    }
}
