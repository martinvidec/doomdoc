package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a field in a class, interface, or enum.
 */
public class FieldInfo {

    /**
     * The name of the field
     */
    private String name;

    /**
     * The type of the field (qualified name for reference types)
     */
    private String type;

    /**
     * Modifiers (e.g., "public", "static", "final", "volatile", "transient")
     */
    private List<String> modifiers = new ArrayList<>();

    /**
     * JavaDoc comment for this field
     */
    private JavaDocComment javadoc;

    /**
     * Annotations applied to this field
     */
    private List<AnnotationUsage> annotations = new ArrayList<>();

    /**
     * The constant value if this field is a compile-time constant, null otherwise
     */
    private Object constantValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
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

    public Object getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(Object constantValue) {
        this.constantValue = constantValue;
    }
}
