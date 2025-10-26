package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic type parameter (e.g., T in List&lt;T&gt;).
 */
public class TypeParameter {

    /**
     * The name of the type parameter (e.g., "T", "K", "V")
     */
    private String name;

    /**
     * List of bounds for this type parameter.
     * For example, for "T extends Number & Serializable", bounds would be ["java.lang.Number", "java.io.Serializable"]
     */
    private List<String> bounds = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBounds() {
        return bounds;
    }

    public void setBounds(List<String> bounds) {
        this.bounds = bounds;
    }
}
