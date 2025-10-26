package at.videc.bomblet.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the usage of an annotation on a program element.
 */
public class AnnotationUsage {

    /**
     * The qualified name of the annotation type (e.g., "at.videc.BOMB")
     */
    private String type;

    /**
     * Map of annotation element names to their values.
     * Values can be strings, numbers, arrays, or other annotations.
     */
    private Map<String, Object> values = new HashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
