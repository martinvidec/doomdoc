package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Java enum type.
 */
public class EnumInfo extends TypeInfo {

    /**
     * List of qualified names of interfaces that this enum implements
     */
    private List<String> interfaces = new ArrayList<>();

    /**
     * Enum constants
     */
    private List<EnumConstantInfo> constants = new ArrayList<>();

    /**
     * Fields declared in this enum
     */
    private List<FieldInfo> fields = new ArrayList<>();

    /**
     * Constructors declared in this enum
     */
    private List<ConstructorInfo> constructors = new ArrayList<>();

    /**
     * Methods declared in this enum
     */
    private List<MethodInfo> methods = new ArrayList<>();

    public EnumInfo() {
        setKind("enum");
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public List<EnumConstantInfo> getConstants() {
        return constants;
    }

    public void setConstants(List<EnumConstantInfo> constants) {
        this.constants = constants;
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
