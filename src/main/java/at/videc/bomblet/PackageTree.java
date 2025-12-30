package at.videc.bomblet;

import at.videc.bomblet.dto.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Organizes types into a hierarchical package structure and provides JSON serialization.
 *
 * <p>This class serves as the central data structure for organizing documentation elements.
 * It wraps a {@link DocumentationModel} and provides convenient methods for:</p>
 *
 * <ul>
 *   <li><strong>Package Management:</strong> Creating and organizing package hierarchies</li>
 *   <li><strong>Type Registration:</strong> Adding classes, interfaces, enums, and annotations</li>
 *   <li><strong>Search Indexing:</strong> Building searchable index of all elements</li>
 *   <li><strong>JSON Serialization:</strong> Converting to JSON for client-side rendering</li>
 * </ul>
 *
 * <p><strong>Typical Usage:</strong></p>
 * <pre>{@code
 * PackageTree tree = new PackageTree();
 * tree.addType("com.example", typeInfo);
 * tree.buildSearchIndex();
 * String json = tree.toCompactJson();
 * }</pre>
 *
 * <p><strong>Serialization Formats:</strong></p>
 * <p>The class provides three JSON serialization options:</p>
 * <ul>
 *   <li>{@link #toJson()} - Pretty-printed JSON for debugging</li>
 *   <li>{@link #toCompactJson()} - Compact JSON for production (recommended)</li>
 *   <li>{@link #toLegacyJson()} - Legacy format for backward compatibility</li>
 * </ul>
 *
 * <p><strong>HTML Handling:</strong></p>
 * <p>All JSON serialization uses {@code disableHtmlEscaping()} to preserve HTML tags
 * in JavaDoc comments, allowing rich formatting in the generated documentation.</p>
 *
 * @author DoomDoc Team
 * @version 1.0.0
 * @since 1.0.0
 * @see DocumentationModel
 * @see PackageInfo
 * @see TypeInfo
 * @see SearchIndexEntry
 */
public class PackageTree {

    /**
     * The underlying documentation model containing all packages and types.
     */
    private final DocumentationModel model;

    /**
     * Gson instance configured with pretty printing and HTML escaping disabled.
     * Used for {@link #toJson()} method.
     */
    private final Gson gson;

    /**
     * Creates a new PackageTree with an empty documentation model.
     * Initializes Gson with pretty printing and disabled HTML escaping.
     */
    public PackageTree() {
        this.model = new DocumentationModel();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    /**
     * Creates a new PackageTree with the given documentation model.
     *
     * @param model the documentation model to use
     */
    public PackageTree(DocumentationModel model) {
        this.model = model;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    /**
     * Gets the underlying documentation model.
     *
     * @return the documentation model
     */
    public DocumentationModel getModel() {
        return model;
    }

    /**
     * Adds a package to the tree.
     *
     * @param packageInfo the package information to add
     */
    public void addPackage(PackageInfo packageInfo) {
        model.getPackages().add(packageInfo);
    }

    /**
     * Adds a type to a specific package. If the package doesn't exist, it will be created.
     *
     * @param packageName the fully qualified package name
     * @param typeInfo the type information to add
     */
    public void addType(String packageName, TypeInfo typeInfo) {
        PackageInfo packageInfo = findOrCreatePackage(packageName);
        packageInfo.getTypes().add(typeInfo);
    }

    /**
     * Finds a package by name or creates it if it doesn't exist.
     *
     * @param packageName the fully qualified package name
     * @return the package info for the given name
     */
    private PackageInfo findOrCreatePackage(String packageName) {
        return model.getPackages().stream()
                .filter(p -> p.getName().equals(packageName))
                .findFirst()
                .orElseGet(() -> {
                    PackageInfo newPackage = new PackageInfo();
                    newPackage.setName(packageName);
                    model.getPackages().add(newPackage);
                    return newPackage;
                });
    }

    /**
     * Gets all packages in the tree.
     *
     * @return list of all packages
     */
    public List<PackageInfo> getPackages() {
        return model.getPackages();
    }

    /**
     * Converts the package tree to a JSON string representation.
     * This JSON can be passed to the JavaScript generateTree() function.
     *
     * @return JSON string representation of the package tree
     */
    public String toJson() {
        return gson.toJson(model);
    }

    /**
     * Converts the package tree to a compact JSON string (without pretty printing).
     *
     * @return compact JSON string representation of the package tree
     */
    public String toCompactJson() {
        Gson compactGson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        return compactGson.toJson(model);
    }

    /**
     * Converts the package tree to a legacy format compatible with the old generateTree() JavaScript function.
     * The legacy format is a simple map: {"packageName": ["className1", "className2"]}.
     *
     * @return JSON string in legacy format
     */
    public String toLegacyJson() {
        java.util.Map<String, java.util.List<String>> legacyMap = new java.util.HashMap<>();

        for (PackageInfo packageInfo : model.getPackages()) {
            java.util.List<String> classNames = new java.util.ArrayList<>();
            for (TypeInfo typeInfo : packageInfo.getTypes()) {
                classNames.add(typeInfo.getName());
            }
            legacyMap.put(packageInfo.getName(), classNames);
        }

        Gson compactGson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        return compactGson.toJson(legacyMap);
    }

    /**
     * Checks if the tree is empty (contains no packages).
     *
     * @return true if the tree has no packages, false otherwise
     */
    public boolean isEmpty() {
        return model.getPackages().isEmpty();
    }

    /**
     * Gets the total number of packages in the tree.
     *
     * @return the number of packages
     */
    public int getPackageCount() {
        return model.getPackages().size();
    }

    /**
     * Gets the total number of types across all packages.
     *
     * @return the total number of types
     */
    public int getTypeCount() {
        return model.getPackages().stream()
                .mapToInt(p -> p.getTypes().size())
                .sum();
    }

    /**
     * Builds a comprehensive search index from all documentation elements.
     *
     * <p>This method creates a searchable index containing:</p>
     * <ul>
     *   <li><strong>Types:</strong> All classes, interfaces, enums, and annotations</li>
     *   <li><strong>Methods:</strong> All methods with signatures and return types</li>
     *   <li><strong>Fields:</strong> All fields with types</li>
     *   <li><strong>Annotation Elements:</strong> All annotation element declarations</li>
     * </ul>
     *
     * <p><strong>Search Index Structure:</strong></p>
     * <p>Each {@link SearchIndexEntry} contains:</p>
     * <ul>
     *   <li>Category (class, interface, enum, annotation, method, field)</li>
     *   <li>Name and qualified name</li>
     *   <li>Package name for context</li>
     *   <li>Parent type name (for methods and fields)</li>
     *   <li>Signature (for methods) or type (for fields)</li>
     * </ul>
     *
     * <p><strong>Usage:</strong></p>
     * <p>This method must be called after all types have been added to the tree,
     * typically in {@link at.videc.DoomDoclet#run(jdk.javadoc.doclet.DocletEnvironment)}.</p>
     *
     * <p><strong>Performance:</strong></p>
     * <p>Time complexity: O(n) where n is the total number of documentation elements.
     * Space complexity: O(n) for the index storage.</p>
     *
     * @see SearchIndexEntry
     * @see #createTypeEntry(TypeInfo, String)
     * @see #createMethodEntry(MethodInfo, TypeInfo, String)
     * @see #createFieldEntry(FieldInfo, TypeInfo, String)
     */
    public void buildSearchIndex() {
        List<SearchIndexEntry> index = new ArrayList<>();

        for (PackageInfo pkg : model.getPackages()) {
            for (TypeInfo type : pkg.getTypes()) {
                // Add type entry
                index.add(createTypeEntry(type, pkg.getName()));

                // Add method and field entries for classes
                if (type instanceof ClassInfo) {
                    ClassInfo classInfo = (ClassInfo) type;
                    for (MethodInfo method : classInfo.getMethods()) {
                        index.add(createMethodEntry(method, type, pkg.getName()));
                    }
                    for (FieldInfo field : classInfo.getFields()) {
                        index.add(createFieldEntry(field, type, pkg.getName()));
                    }
                }
                // Add method entries for interfaces
                else if (type instanceof InterfaceInfo) {
                    InterfaceInfo interfaceInfo = (InterfaceInfo) type;
                    for (MethodInfo method : interfaceInfo.getMethods()) {
                        index.add(createMethodEntry(method, type, pkg.getName()));
                    }
                }
                // Add method and field entries for enums
                else if (type instanceof EnumInfo) {
                    EnumInfo enumInfo = (EnumInfo) type;
                    for (MethodInfo method : enumInfo.getMethods()) {
                        index.add(createMethodEntry(method, type, pkg.getName()));
                    }
                    for (FieldInfo field : enumInfo.getFields()) {
                        index.add(createFieldEntry(field, type, pkg.getName()));
                    }
                }
                // Add element entries for annotations
                else if (type instanceof AnnotationInfo) {
                    AnnotationInfo annotationInfo = (AnnotationInfo) type;
                    for (AnnotationElementInfo element : annotationInfo.getElements()) {
                        index.add(createAnnotationElementEntry(element, type, pkg.getName()));
                    }
                }
            }
        }

        model.setSearchIndex(index);
    }

    /**
     * Creates a search index entry for a type (class, interface, enum, or annotation).
     *
     * @param type the type information
     * @param packageName the package name
     * @return search index entry for the type
     */
    private SearchIndexEntry createTypeEntry(TypeInfo type, String packageName) {
        SearchIndexEntry entry = new SearchIndexEntry();
        entry.setCategory(type.getKind()); // "class", "interface", "enum", "annotation"
        entry.setName(type.getName());
        entry.setQualifiedName(type.getQualifiedName());
        entry.setPackageName(packageName);
        return entry;
    }

    /**
     * Creates a search index entry for a method.
     *
     * @param method the method information
     * @param parentType the parent type containing this method
     * @param packageName the package name
     * @return search index entry for the method
     */
    private SearchIndexEntry createMethodEntry(MethodInfo method, TypeInfo parentType, String packageName) {
        SearchIndexEntry entry = new SearchIndexEntry();
        entry.setCategory("method");
        entry.setName(method.getName());
        entry.setQualifiedName(parentType.getQualifiedName() + "." + method.getName());
        entry.setPackageName(packageName);
        entry.setTypeName(parentType.getName());
        entry.setReturnType(method.getReturnType());

        // Build method signature with parameter types
        StringBuilder signature = new StringBuilder(method.getName());
        signature.append("(");
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                if (i > 0) signature.append(", ");
                signature.append(method.getParameters().get(i).getType());
            }
        }
        signature.append(")");
        entry.setSignature(signature.toString());

        return entry;
    }

    /**
     * Creates a search index entry for a field.
     *
     * @param field the field information
     * @param parentType the parent type containing this field
     * @param packageName the package name
     * @return search index entry for the field
     */
    private SearchIndexEntry createFieldEntry(FieldInfo field, TypeInfo parentType, String packageName) {
        SearchIndexEntry entry = new SearchIndexEntry();
        entry.setCategory("field");
        entry.setName(field.getName());
        entry.setQualifiedName(parentType.getQualifiedName() + "." + field.getName());
        entry.setPackageName(packageName);
        entry.setTypeName(parentType.getName());
        entry.setReturnType(field.getType()); // Store field type in returnType for consistency
        return entry;
    }

    /**
     * Creates a search index entry for an annotation element.
     *
     * @param element the annotation element information
     * @param parentType the parent annotation type containing this element
     * @param packageName the package name
     * @return search index entry for the annotation element
     */
    private SearchIndexEntry createAnnotationElementEntry(AnnotationElementInfo element, TypeInfo parentType, String packageName) {
        SearchIndexEntry entry = new SearchIndexEntry();
        entry.setCategory("field"); // Treat annotation elements as fields for search purposes
        entry.setName(element.getName());
        entry.setQualifiedName(parentType.getQualifiedName() + "." + element.getName());
        entry.setPackageName(packageName);
        entry.setTypeName(parentType.getName());
        entry.setReturnType(element.getType()); // AnnotationElementInfo uses getType() not getReturnType()
        return entry;
    }
}
