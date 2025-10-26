package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete JavaDoc comment with description and tags.
 */
public class JavaDocComment {

    /**
     * The main description text of the JavaDoc comment.
     * May contain multiple paragraphs.
     */
    private String description;

    /**
     * List of JavaDoc tags (@param, @return, @throws, etc.)
     */
    private List<JavaDocTag> tags = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JavaDocTag> getTags() {
        return tags;
    }

    public void setTags(List<JavaDocTag> tags) {
        this.tags = tags;
    }
}
