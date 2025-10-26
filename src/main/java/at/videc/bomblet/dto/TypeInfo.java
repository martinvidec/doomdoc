package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all type definitions (class, interface, enum, annotation).
 * This is an abstract representation that can be extended by specific type kinds.
 */
public abstract class TypeInfo {

    /**
     * The kind of type ("class", "interface", "enum", "annotation")
     */
    private String kind;

    /**
     * The simple name of the type
     */
    private String name;

    /**
     * The fully qualified name of the type
     */
    private String qualifiedName;

    /**
     * Modifiers (e.g., "public", "abstract", "final", "static")
     */
    private List<String> modifiers = new ArrayList<>();

    /**
     * JavaDoc comment for this type
     */
    private JavaDocComment javadoc;

    /**
     * Annotations applied to this type
     */
    private List<AnnotationUsage> annotations = new ArrayList<>();

    /**
     * Type parameters for generic types
     */
    private List<TypeParameter> typeParameters = new ArrayList<>();

    /**
     * Nested/inner types
     */
    private List<TypeInfo> innerTypes = new ArrayList<>();

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
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

    public List<TypeInfo> getInnerTypes() {
        return innerTypes;
    }

    public void setInnerTypes(List<TypeInfo> innerTypes) {
        this.innerTypes = innerTypes;
    }
}
