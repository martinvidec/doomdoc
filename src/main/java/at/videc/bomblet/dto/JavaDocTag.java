package at.videc.bomblet.dto;

/**
 * Represents a JavaDoc tag such as @param, @return, @throws, @see, etc.
 */
public class JavaDocTag {

    /**
     * The kind of tag (e.g., "param", "return", "throws", "see", "since", "deprecated", "author", "version", "link", "code")
     */
    private String kind;

    /**
     * The name parameter for tags like @param
     */
    private String name;

    /**
     * The description text for the tag
     */
    private String description;

    /**
     * The exception type for @throws/@exception tags
     */
    private String exception;

    /**
     * The reference for @see/@link tags
     */
    private String reference;

    /**
     * The label for @link/@linkplain tags
     */
    private String label;

    /**
     * The content for @code/@literal tags
     */
    private String content;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
