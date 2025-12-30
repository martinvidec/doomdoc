package at.videc.bomblet.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Root data model containing the complete documentation structure.
 *
 * <p>This class represents the top-level container for all documentation data.
 * It is serialized to JSON and embedded in the generated HTML for client-side
 * rendering and navigation.</p>
 *
 * <p><strong>Structure:</strong></p>
 * <p>The model consists of two main components:</p>
 * <ul>
 *   <li><strong>Packages:</strong> Hierarchical organization of types by package</li>
 *   <li><strong>Search Index:</strong> Flat, searchable list of all documentation elements</li>
 * </ul>
 *
 * <p><strong>Data Flow:</strong></p>
 * <ol>
 *   <li>{@link at.videc.bomblet.TypeElementConverter} extracts types from source</li>
 *   <li>{@link at.videc.bomblet.PackageTree} organizes types into this model</li>
 *   <li>{@link at.videc.bomblet.PackageTree#buildSearchIndex()} populates search index</li>
 *   <li>{@link at.videc.bomblet.PackageTree#toCompactJson()} serializes to JSON</li>
 *   <li>{@link at.videc.DoomDoclet} embeds JSON in HTML output</li>
 *   <li>JavaScript reads model and generates interactive UI</li>
 * </ol>
 *
 * <p><strong>Serialization:</strong></p>
 * <p>The entire model is serialized using Gson with:</p>
 * <ul>
 *   <li>HTML escaping disabled (preserves JavaDoc HTML tags)</li>
 *   <li>Null values omitted for compact output</li>
 *   <li>All metadata and documentation preserved</li>
 * </ul>
 *
 * <p><strong>Size Considerations:</strong></p>
 * <p>For large codebases, the serialized JSON can be substantial. The compact
 * format (without pretty printing) minimizes size. Typical sizes:</p>
 * <ul>
 *   <li>Small project (10-50 classes): ~50-200 KB</li>
 *   <li>Medium project (100-500 classes): ~200 KB - 1 MB</li>
 *   <li>Large project (1000+ classes): 1-5 MB+</li>
 * </ul>
 *
 * @author DoomDoc Team
 * @version 1.0.0
 * @since 1.0.0
 * @see PackageInfo
 * @see SearchIndexEntry
 * @see at.videc.bomblet.PackageTree
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
