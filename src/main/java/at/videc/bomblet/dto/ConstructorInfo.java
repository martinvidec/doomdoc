package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a constructor in a class or enum.
 */
public class ConstructorInfo {

    /**
     * The name of the constructor (typically same as the class name)
     */
    private String name;

    /**
     * Modifiers (e.g., "public", "protected", "private")
     */
    private List<String> modifiers = new ArrayList<>();

    /**
     * JavaDoc comment for this constructor
     */
    private JavaDocComment javadoc;

    /**
     * Annotations applied to this constructor
     */
    private List<AnnotationUsage> annotations = new ArrayList<>();

    /**
     * List of parameters
     */
    private List<ParameterInfo> parameters = new ArrayList<>();

    /**
     * List of exception types this constructor can throw
     */
    private List<String> exceptions = new ArrayList<>();

    /**
     * Type parameters for generic constructors
     */
    private List<TypeParameter> typeParameters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterInfo> parameters) {
        this.parameters = parameters;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }
}
