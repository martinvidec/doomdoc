package at.videc.bomblet.dto;

/**
 * Represents a single searchable item in the documentation index.
 * Used for faceted autocomplete search functionality.
 */
public class SearchIndexEntry {
    /**
     * Category of the search item.
     * Values: "class", "interface", "enum", "annotation", "method", "field"
     */
    private String category;

    /**
     * Simple name of the item (e.g., "TestClass1", "myMethod").
     */
    private String name;

    /**
     * Fully qualified name for navigation (e.g., "at.videc.TestClass1").
     */
    private String qualifiedName;

    /**
     * Package name for context display and navigation.
     */
    private String packageName;

    /**
     * Parent type name for methods and fields (null for types).
     */
    private String typeName;

    /**
     * Method signature for methods (e.g., "methodName(String, int)").
     * Null for types and fields.
     */
    private String signature;

    /**
     * Return type for methods (null for types and fields).
     */
    private String returnType;

    // Constructor
    public SearchIndexEntry() {
    }

    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
