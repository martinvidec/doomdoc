package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing a Java class definition.
 *
 * <p>This class extends {@link TypeInfo} to add class-specific members including
 * inheritance relationships, fields, constructors, and methods.</p>
 *
 * <p><strong>Class Components:</strong></p>
 * <ul>
 *   <li><strong>Inheritance:</strong> Single superclass (or Object if none specified)</li>
 *   <li><strong>Interfaces:</strong> Zero or more implemented interfaces</li>
 *   <li><strong>Fields:</strong> Instance and static variables</li>
 *   <li><strong>Constructors:</strong> All declared constructors (including default)</li>
 *   <li><strong>Methods:</strong> Instance and static methods (excluding inherited)</li>
 * </ul>
 *
 * <p><strong>Inheritance Representation:</strong></p>
 * <p>The superclass and interfaces are stored as fully qualified names (strings).
 * The documentation UI makes these clickable for navigation. For example:</p>
 * <pre>{@code
 * public class ArrayList<E> extends AbstractList<E>
 *     implements List<E>, RandomAccess, Cloneable
 * }</pre>
 * <p>Would have:</p>
 * <ul>
 *   <li>{@code superClass = "java.util.AbstractList"}</li>
 *   <li>{@code interfaces = ["java.util.List", "java.util.RandomAccess", ...]}</li>
 * </ul>
 *
 * <p><strong>Member Organization:</strong></p>
 * <p>Members are organized into separate lists for efficient access and rendering.
 * This allows the UI to group and display members by category.</p>
 *
 * <p><strong>Generic Types:</strong></p>
 * <p>Type parameters (e.g., {@code <T extends Comparable<T>>}) are inherited from
 * {@link TypeInfo#getTypeParameters()}. They apply to the entire class scope.</p>
 *
 * @author DoomDoc Team
 * @version 1.0.0
 * @since 1.0.0
 * @see TypeInfo
 * @see FieldInfo
 * @see ConstructorInfo
 * @see MethodInfo
 */
public class ClassInfo extends TypeInfo {

    /**
     * The qualified name of the superclass
     */
    private String superClass;

    /**
     * List of interface qualified names that this class implements
     */
    private List<String> interfaces = new ArrayList<>();

    /**
     * Fields declared in this class
     */
    private List<FieldInfo> fields = new ArrayList<>();

    /**
     * Constructors declared in this class
     */
    private List<ConstructorInfo> constructors = new ArrayList<>();

    /**
     * Methods declared in this class
     */
    private List<MethodInfo> methods = new ArrayList<>();

    public ClassInfo() {
        setKind("class");
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }

    public List<ConstructorInfo> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<ConstructorInfo> constructors) {
        this.constructors = constructors;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }
}
