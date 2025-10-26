package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Java annotation type.
 */
public class AnnotationInfo extends TypeInfo {

    /**
     * Elements (methods) declared in this annotation type
     */
    private List<AnnotationElementInfo> elements = new ArrayList<>();

    public AnnotationInfo() {
        setKind("annotation");
    }

    public List<AnnotationElementInfo> getElements() {
        return elements;
    }

    public void setElements(List<AnnotationElementInfo> elements) {
        this.elements = elements;
    }
}
