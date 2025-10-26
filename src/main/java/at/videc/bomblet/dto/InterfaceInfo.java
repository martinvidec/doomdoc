package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Java interface.
 */
public class InterfaceInfo extends TypeInfo {

    /**
     * List of qualified names of interfaces that this interface extends
     */
    private List<String> superInterfaces = new ArrayList<>();

    /**
     * Methods declared in this interface (including default and static methods)
     */
    private List<MethodInfo> methods = new ArrayList<>();

    /**
     * Fields declared in this interface (only constants are allowed)
     */
    private List<FieldInfo> fields = new ArrayList<>();

    public InterfaceInfo() {
        setKind("interface");
    }

    public List<String> getSuperInterfaces() {
        return superInterfaces;
    }

    public void setSuperInterfaces(List<String> superInterfaces) {
        this.superInterfaces = superInterfaces;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }
}
