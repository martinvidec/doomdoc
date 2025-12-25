/**
 * DoomDoc Search Component
 * Provides faceted autocomplete search functionality for classes, interfaces, enums, annotations, methods, and fields.
 */

// Global state for search functionality
var searchIndex = null;
var searchDropdownVisible = false;
var searchSelectedIndex = -1;
var searchCurrentResults = [];
var searchDebounceTimer = null;

/**
 * Initializes the search functionality with the documentation model.
 * Should be called after the documentation model is loaded.
 *
 * @param {Object} documentationModel - The documentation model containing searchIndex
 */
function initializeSearch(documentationModel) {
    if (!documentationModel || !documentationModel.searchIndex) {
        console.warn('Search index not available in documentation model');
        return;
    }

    searchIndex = documentationModel.searchIndex;

    var searchInput = document.getElementById('globalSearch');
    if (!searchInput) {
        console.warn('Search input element not found (id="globalSearch")');
        return;
    }

    // Set up event listeners
    searchInput.addEventListener('input', handleSearchInput);
    searchInput.addEventListener('keydown', handleKeyboardNavigation);
    searchInput.addEventListener('focus', function() {
        // Show dropdown if there's already input and results
        if (searchInput.value.length >= 2) {
            handleSearchInput({ target: searchInput });
        }
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', function(event) {
        var searchContainer = searchInput.parentElement;
        var dropdown = document.getElementById('searchDropdown');

        if (searchContainer && !searchContainer.contains(event.target) && dropdown) {
            closeAutocomplete();
        }
    });

    // Set up accessibility attributes
    searchInput.setAttribute('role', 'combobox');
    searchInput.setAttribute('aria-autocomplete', 'list');
    searchInput.setAttribute('aria-controls', 'searchDropdown');
    searchInput.setAttribute('aria-expanded', 'false');
}

/**
 * Handles search input events with debouncing for performance.
 * Triggers search and dropdown rendering when input length >= 2.
 *
 * @param {Event} event - The input event
 */
function handleSearchInput(event) {
    var input = event.target;
    var query = input.value;

    // Clear any pending debounce timer
    if (searchDebounceTimer) {
        clearTimeout(searchDebounceTimer);
    }

    // Show/hide clear button
    var clearButton = document.querySelector('.search-clear');
    if (clearButton) {
        clearButton.style.display = query.length > 0 ? 'inline-block' : 'none';
    }

    // Hide dropdown if query is too short
    if (query.length < 2) {
        closeAutocomplete();
        return;
    }

    // Debounce search for performance
    searchDebounceTimer = setTimeout(function() {
        var facetedResults = filterSearchIndex(query);
        renderAutocompleteDropdown(facetedResults);

        // Update ARIA state
        input.setAttribute('aria-expanded', 'true');
        searchDropdownVisible = true;
    }, 150);
}

/**
 * Filters the search index based on the query string.
 * Performs case-insensitive substring matching on name, qualifiedName, and signature.
 * Results are grouped by category and limited to 5 items per category (max 30 total).
 *
 * @param {string} query - The search query
 * @return {Object} Faceted results object with categories as keys
 */
function filterSearchIndex(query) {
    if (!searchIndex || !query) {
        return {
            classes: [],
            interfaces: [],
            enums: [],
            annotations: [],
            methods: [],
            fields: []
        };
    }

    var queryLower = query.toLowerCase();
    var results = {
        classes: [],
        interfaces: [],
        enums: [],
        annotations: [],
        methods: [],
        fields: []
    };

    // Map category names to result arrays
    var categoryMap = {
        'class': 'classes',
        'interface': 'interfaces',
        'enum': 'enums',
        'annotation': 'annotations',
        'method': 'methods',
        'field': 'fields'
    };

    // Filter search index entries
    for (var i = 0; i < searchIndex.length; i++) {
        var entry = searchIndex[i];

        // Check if any searchable field matches the query
        var nameMatches = entry.name && entry.name.toLowerCase().indexOf(queryLower) !== -1;
        var qualifiedMatches = entry.qualifiedName && entry.qualifiedName.toLowerCase().indexOf(queryLower) !== -1;
        var signatureMatches = entry.signature && entry.signature.toLowerCase().indexOf(queryLower) !== -1;

        if (nameMatches || qualifiedMatches || signatureMatches) {
            var categoryKey = categoryMap[entry.category];
            if (categoryKey && results[categoryKey].length < 5) {
                results[categoryKey].push(entry);
            }
        }

        // Early exit if we've hit the max total results
        var totalResults = results.classes.length + results.interfaces.length +
                          results.enums.length + results.annotations.length +
                          results.methods.length + results.fields.length;
        if (totalResults >= 30) {
            break;
        }
    }

    return results;
}

/**
 * Renders the autocomplete dropdown with faceted search results.
 * Creates sections for each non-empty category with appropriate styling and badges.
 *
 * @param {Object} facetedResults - Faceted results object from filterSearchIndex
 */
function renderAutocompleteDropdown(facetedResults) {
    var dropdown = document.getElementById('searchDropdown');
    if (!dropdown) {
        console.warn('Search dropdown element not found (id="searchDropdown")');
        return;
    }

    // Clear previous results
    dropdown.innerHTML = '';
    searchCurrentResults = [];
    searchSelectedIndex = -1;

    // Check if there are any results
    var hasResults = false;
    for (var category in facetedResults) {
        if (facetedResults[category].length > 0) {
            hasResults = true;
            break;
        }
    }

    if (!hasResults) {
        dropdown.innerHTML = '<div class="search-empty-state">No results found</div>';
        dropdown.classList.add('visible');
        dropdown.setAttribute('role', 'listbox');
        return;
    }

    // Render each category section
    var categories = [
        { key: 'classes', label: 'Classes', badgeClass: 'badge-class' },
        { key: 'interfaces', label: 'Interfaces', badgeClass: 'badge-interface' },
        { key: 'enums', label: 'Enums', badgeClass: 'badge-enum' },
        { key: 'annotations', label: 'Annotations', badgeClass: 'badge-annotation' },
        { key: 'methods', label: 'Methods', badgeClass: 'badge-method' },
        { key: 'fields', label: 'Fields', badgeClass: 'badge-field' }
    ];

    var query = document.getElementById('globalSearch').value;

    categories.forEach(function(category) {
        var items = facetedResults[category.key];
        if (items.length === 0) return;

        // Create category section
        var section = document.createElement('div');
        section.className = 'search-facet-section';

        var header = document.createElement('div');
        header.className = 'search-facet-header';
        header.textContent = category.label;
        section.appendChild(header);

        // Create items list
        items.forEach(function(entry) {
            var item = createSearchResultItem(entry, category.badgeClass, query);
            section.appendChild(item);
            searchCurrentResults.push(entry);
        });

        dropdown.appendChild(section);
    });

    dropdown.classList.add('visible');
    dropdown.setAttribute('role', 'listbox');
}

/**
 * Creates a single search result item element.
 *
 * @param {Object} entry - Search index entry
 * @param {string} badgeClass - CSS class for the type badge
 * @param {string} query - Search query for highlighting
 * @return {HTMLElement} The search result item element
 */
function createSearchResultItem(entry, badgeClass, query) {
    var item = document.createElement('div');
    item.className = 'search-result-item';
    item.setAttribute('role', 'option');
    item.setAttribute('aria-selected', 'false');

    // Create badge
    var badge = document.createElement('span');
    badge.className = 'search-result-badge ' + badgeClass;
    badge.textContent = entry.category.charAt(0).toUpperCase();
    item.appendChild(badge);

    // Create main content
    var content = document.createElement('div');
    content.className = 'search-result-content';

    // Name with highlighting
    var name = document.createElement('div');
    name.className = 'search-result-name';

    var displayName = entry.name;
    if (entry.category === 'method' && entry.signature) {
        displayName = entry.signature;
    }

    name.innerHTML = highlightMatch(displayName, query);
    content.appendChild(name);

    // Context (package + type for methods/fields)
    var context = document.createElement('div');
    context.className = 'search-result-context';

    if (entry.category === 'method' || entry.category === 'field') {
        context.textContent = entry.packageName + '.' + entry.typeName;
        if (entry.returnType) {
            var returnType = document.createElement('span');
            returnType.className = 'search-result-return-type';
            returnType.textContent = ' : ' + entry.returnType;
            context.appendChild(returnType);
        }
    } else {
        context.textContent = entry.packageName;
    }
    content.appendChild(context);

    item.appendChild(content);

    // Attach click handler
    item.addEventListener('click', function(event) {
        event.stopPropagation();
        navigateToItem(entry);
    });

    // Attach hover handler for keyboard navigation sync
    item.addEventListener('mouseenter', function() {
        var index = searchCurrentResults.indexOf(entry);
        if (index !== -1) {
            updateSelectedItem(index);
        }
    });

    return item;
}

/**
 * Highlights matching text in a string using <mark> tags.
 *
 * @param {string} text - The text to highlight
 * @param {string} query - The search query
 * @return {string} HTML string with highlighted matches
 */
function highlightMatch(text, query) {
    if (!text || !query) return escapeHtml(text);

    var escapedText = escapeHtml(text);
    var queryLower = query.toLowerCase();
    var textLower = text.toLowerCase();
    var index = textLower.indexOf(queryLower);

    if (index === -1) return escapedText;

    // Find all matches and highlight them
    var result = '';
    var lastIndex = 0;

    while (index !== -1) {
        result += escapeHtml(text.substring(lastIndex, index));
        result += '<mark>' + escapeHtml(text.substring(index, index + query.length)) + '</mark>';
        lastIndex = index + query.length;
        index = textLower.indexOf(queryLower, lastIndex);
    }

    result += escapeHtml(text.substring(lastIndex));
    return result;
}

/**
 * Handles keyboard navigation in the search dropdown.
 * Supports Arrow Up, Arrow Down, Enter, and Escape keys.
 *
 * @param {KeyboardEvent} event - The keyboard event
 */
function handleKeyboardNavigation(event) {
    if (!searchDropdownVisible || searchCurrentResults.length === 0) {
        return;
    }

    var key = event.key || event.keyCode;

    // Arrow Down
    if (key === 'ArrowDown' || key === 40) {
        event.preventDefault();
        var newIndex = searchSelectedIndex + 1;
        if (newIndex >= searchCurrentResults.length) {
            newIndex = 0; // Wrap to first
        }
        updateSelectedItem(newIndex);
    }
    // Arrow Up
    else if (key === 'ArrowUp' || key === 38) {
        event.preventDefault();
        var newIndex = searchSelectedIndex - 1;
        if (newIndex < 0) {
            newIndex = searchCurrentResults.length - 1; // Wrap to last
        }
        updateSelectedItem(newIndex);
    }
    // Enter
    else if (key === 'Enter' || key === 13) {
        event.preventDefault();
        if (searchSelectedIndex >= 0 && searchSelectedIndex < searchCurrentResults.length) {
            navigateToItem(searchCurrentResults[searchSelectedIndex]);
        }
    }
    // Escape
    else if (key === 'Escape' || key === 27) {
        event.preventDefault();
        closeAutocomplete();
        document.getElementById('globalSearch').blur();
    }
}

/**
 * Updates the selected item in the dropdown based on keyboard navigation.
 *
 * @param {number} newIndex - The new selected index
 */
function updateSelectedItem(newIndex) {
    var dropdown = document.getElementById('searchDropdown');
    if (!dropdown) return;

    var items = dropdown.querySelectorAll('.search-result-item');

    // Remove previous selection
    if (searchSelectedIndex >= 0 && searchSelectedIndex < items.length) {
        items[searchSelectedIndex].classList.remove('selected');
        items[searchSelectedIndex].setAttribute('aria-selected', 'false');
    }

    // Add new selection
    searchSelectedIndex = newIndex;
    if (searchSelectedIndex >= 0 && searchSelectedIndex < items.length) {
        items[searchSelectedIndex].classList.add('selected');
        items[searchSelectedIndex].setAttribute('aria-selected', 'true');

        // Scroll into view if needed
        items[searchSelectedIndex].scrollIntoView({
            block: 'nearest',
            behavior: 'smooth'
        });

        // Update aria-activedescendant
        var searchInput = document.getElementById('globalSearch');
        if (searchInput) {
            var itemId = 'search-result-' + searchSelectedIndex;
            items[searchSelectedIndex].id = itemId;
            searchInput.setAttribute('aria-activedescendant', itemId);
        }
    }
}

/**
 * Navigates to the selected search result item.
 * For types: calls showType() and updates URL hash.
 * For methods/fields: calls showType() for parent, then scrolls to member.
 *
 * @param {Object} searchIndexEntry - The search index entry to navigate to
 */
function navigateToItem(searchIndexEntry) {
    if (!searchIndexEntry) return;

    var category = searchIndexEntry.category;

    // Navigate to type (class, interface, enum, annotation)
    if (category === 'class' || category === 'interface' || category === 'enum' || category === 'annotation') {
        showType(searchIndexEntry.packageName, searchIndexEntry.name);
        window.location.hash = searchIndexEntry.qualifiedName;
    }
    // Navigate to method or field
    else if (category === 'method' || category === 'field') {
        // First show the parent type
        showType(searchIndexEntry.packageName, searchIndexEntry.typeName);

        // Then scroll to the member after a brief delay for rendering
        setTimeout(function() {
            scrollToMember(searchIndexEntry.name, category);
        }, 100);

        window.location.hash = searchIndexEntry.qualifiedName;
    }

    // Close dropdown and clear input
    closeAutocomplete();
    document.getElementById('globalSearch').value = '';
}

/**
 * Scrolls to a specific member (method or field) in the detail view.
 * Adds a temporary highlight animation to draw attention to the member.
 *
 * @param {string} memberName - The name of the member to scroll to
 * @param {string} memberType - The type of member ('method' or 'field')
 */
function scrollToMember(memberName, memberType) {
    var docContent = document.getElementById('docContent');
    if (!docContent) return;

    // Find the member element by searching for the member name in signatures
    var memberItems = docContent.querySelectorAll('.member-item');

    for (var i = 0; i < memberItems.length; i++) {
        var memberItem = memberItems[i];
        var memberNameElement = memberItem.querySelector('.member-name');

        if (memberNameElement && memberNameElement.textContent === memberName) {
            // Scroll into view
            memberItem.scrollIntoView({
                behavior: 'smooth',
                block: 'center'
            });

            // Add temporary highlight
            memberItem.classList.add('search-highlight');
            setTimeout(function() {
                memberItem.classList.remove('search-highlight');
            }, 2000);

            break;
        }
    }
}

/**
 * Closes the autocomplete dropdown and resets search state.
 */
function closeAutocomplete() {
    var dropdown = document.getElementById('searchDropdown');
    if (dropdown) {
        dropdown.classList.remove('visible');
        dropdown.innerHTML = '';
    }

    var searchInput = document.getElementById('globalSearch');
    if (searchInput) {
        searchInput.setAttribute('aria-expanded', 'false');
        searchInput.removeAttribute('aria-activedescendant');
    }

    searchDropdownVisible = false;
    searchSelectedIndex = -1;
    searchCurrentResults = [];
}

/**
 * Clears the search input and closes the autocomplete dropdown.
 */
function clearSearch() {
    var searchInput = document.getElementById('globalSearch');
    if (searchInput) {
        searchInput.value = '';
    }

    var clearButton = document.querySelector('.search-clear');
    if (clearButton) {
        clearButton.style.display = 'none';
    }

    closeAutocomplete();

    // Return focus to search input
    if (searchInput) {
        searchInput.focus();
    }
}
