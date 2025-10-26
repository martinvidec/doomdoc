package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method or constructor parameter.
 */
public class ParameterInfo {

    /**
     * The name of the parameter
     */
    private String name;

    /**
     * The type of the parameter (qualified name for reference types, simple name for primitives)
     */
    private String type;

    /**
     * Annotations applied to this parameter
     */
    private List<AnnotationUsage> annotations = new ArrayList<>();

    /**
     * Whether this is a variable arguments parameter
     */
    private boolean isVarArgs;

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

    public List<AnnotationUsage> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationUsage> annotations) {
        this.annotations = annotations;
    }

    public boolean isVarArgs() {
        return isVarArgs;
    }

    public void setVarArgs(boolean varArgs) {
        isVarArgs = varArgs;
    }
}
