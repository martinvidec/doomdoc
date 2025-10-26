package at.videc.bomblet;

import at.videc.bomblet.dto.DocumentationModel;
import at.videc.bomblet.dto.PackageInfo;
import at.videc.bomblet.dto.TypeInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Represents a package tree structure for documentation generation.
 * This class provides a wrapper around the DocumentationModel and offers
 * convenient methods for building and serializing the package tree to JSON.
 */
public class PackageTree {

    private final DocumentationModel model;
    private final Gson gson;

    /**
     * Creates a new PackageTree with an empty documentation model.
     */
    public PackageTree() {
        this.model = new DocumentationModel();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
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
        Gson compactGson = new Gson();
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

        Gson compactGson = new Gson();
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
}
