package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method in a class or interface.
 */
public class MethodInfo {

    /**
     * The name of the method
     */
    private String name;

    /**
     * Modifiers (e.g., "public", "static", "final", "abstract", "synchronized", "native")
     */
    private List<String> modifiers = new ArrayList<>();

    /**
     * JavaDoc comment for this method
     */
    private JavaDocComment javadoc;

    /**
     * Annotations applied to this method
     */
    private List<AnnotationUsage> annotations = new ArrayList<>();

    /**
     * Type parameters for generic methods
     */
    private List<TypeParameter> typeParameters = new ArrayList<>();

    /**
     * The return type of the method
     */
    private String returnType;

    /**
     * List of parameters
     */
    private List<ParameterInfo> parameters = new ArrayList<>();

    /**
     * List of exception types this method can throw
     */
    private List<String> exceptions = new ArrayList<>();

    /**
     * Whether this is a default method in an interface
     */
    private boolean isDefault;

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

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
