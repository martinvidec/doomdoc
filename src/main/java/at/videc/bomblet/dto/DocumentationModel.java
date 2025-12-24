package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Root documentation model containing all packages.
 * This is the top-level structure that gets serialized to JSON.
 */
public class DocumentationModel {

    /**
     * All packages in the documentation
     */
    private List<PackageInfo> packages = new ArrayList<>();

    /**
     * Search index for faceted autocomplete functionality.
     * Contains all searchable items (types, methods, fields).
     */
    private List<SearchIndexEntry> searchIndex = new ArrayList<>();

    public List<PackageInfo> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageInfo> packages) {
        this.packages = packages;
    }

    public List<SearchIndexEntry> getSearchIndex() {
        return searchIndex;
    }

    public void setSearchIndex(List<SearchIndexEntry> searchIndex) {
        this.searchIndex = searchIndex;
    }
}
