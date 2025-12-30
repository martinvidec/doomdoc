package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing any Java type (class, interface, enum, or annotation).
 *
 * <p>This class defines the common structure and metadata shared by all type definitions
 * in the documentation model. It serves as the parent for specialized type representations:</p>
 *
 * <ul>
 *   <li>{@link ClassInfo} - for classes</li>
 *   <li>{@link InterfaceInfo} - for interfaces</li>
 *   <li>{@link EnumInfo} - for enumerations</li>
 *   <li>{@link AnnotationInfo} - for annotation types</li>
 * </ul>
 *
 * <p><strong>Common Metadata:</strong></p>
 * <p>All types share these properties:</p>
 * <ul>
 *   <li><strong>Identity:</strong> Simple name and fully qualified name</li>
 *   <li><strong>Classification:</strong> Kind ("class", "interface", "enum", "annotation")</li>
 *   <li><strong>Access Control:</strong> Modifiers (public, private, protected, abstract, final, static)</li>
 *   <li><strong>Documentation:</strong> Complete JavaDoc with all tags</li>
 *   <li><strong>Metadata:</strong> Annotations applied to the type</li>
 *   <li><strong>Generics:</strong> Type parameters with bounds</li>
 *   <li><strong>Nesting:</strong> Inner/nested types</li>
 * </ul>
 *
 * <p><strong>Polymorphism:</strong></p>
 * <p>This abstract class enables polymorphic handling of different type kinds.
 * Subclasses add specialized members:</p>
 * <ul>
 *   <li>Classes add fields, methods, constructors, superclass</li>
 *   <li>Interfaces add methods and superinterfaces</li>
 *   <li>Enums add constants and methods</li>
 *   <li>Annotations add elements</li>
 * </ul>
 *
 * <p><strong>Serialization:</strong></p>
 * <p>Instances are serialized to JSON for client-side rendering. The {@code kind}
 * field allows JavaScript to determine the type and render appropriately.</p>
 *
 * @author DoomDoc Team
 * @version 1.0.0
 * @since 1.0.0
 * @see ClassInfo
 * @see InterfaceInfo
 * @see EnumInfo
 * @see AnnotationInfo
 * @see at.videc.bomblet.TypeElementConverter
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
