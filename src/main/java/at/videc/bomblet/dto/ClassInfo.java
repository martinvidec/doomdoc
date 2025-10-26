package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Java class.
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
