package at.videc.bomblet.dto;

/**
 * Represents an element (method) in an annotation type.
 */
public class AnnotationElementInfo {

    /**
     * The name of the annotation element
     */
    private String name;

    /**
     * The type of the annotation element
     */
    private String type;

    /**
     * JavaDoc comment for this annotation element
     */
    private JavaDocComment javadoc;

    /**
     * The default value for this element, null if no default
     */
    private Object defaultValue;

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

    public JavaDocComment getJavadoc() {
        return javadoc;
    }

    public void setJavadoc(JavaDocComment javadoc) {
        this.javadoc = javadoc;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
