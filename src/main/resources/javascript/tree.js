// Global state
var documentationModel = null;
var currentSelectedElement = null;

document.addEventListener('DOMContentLoaded', function() {
    var toggler = document.getElementsByClassName("caret");
    for (var i = 0; i < toggler.length; i++) {
        toggler[i].addEventListener("click", function() {
            this.parentElement.querySelector(".nested").classList.toggle("active");
            this.classList.toggle("caret-down");
        });
    }
});

/**
 * Expands all parent nodes of an element
 * Used by search functionality to reveal matching items
 */
function expandParent(element) {
    var parent = element.parentElement;
    while (parent && parent.id !== "packageTree") {
        if (parent.classList.contains("nested")) {
            parent.classList.add("active");
            var previousSibling = parent.previousElementSibling;
            if (previousSibling && previousSibling.classList.contains("caret")) {
                previousSibling.classList.add("caret-down");
            }
        }
        parent.style.display = "";
        parent = parent.parentElement;
    }
}

/**
 * Generates the package tree from the documentation model
 * @param {Object} model - The DocumentationModel with packages array
 */
function generateTree(model) {
    documentationModel = model;
    var ul = document.getElementById("packageTree");
    ul.innerHTML = ''; // Clear existing content

    if (!model || !model.packages || model.packages.length === 0) {
        ul.innerHTML = '<li class="empty-state">No documentation available</li>';
        return;
    }

    // Sort packages by name
    var packages = model.packages.sort(function(a, b) {
        return a.name.localeCompare(b.name);
    });

    packages.forEach(function(packageInfo) {
        var packageNode = createPackageNode(packageInfo);
        ul.appendChild(packageNode);
    });
}

/**
 * Creates a tree node for a package
 */
function createPackageNode(packageInfo) {
    var li = document.createElement("li");

    var caret = document.createElement("span");
    caret.className = "caret caret-down"; // Expanded by default
    caret.textContent = packageInfo.name;
    caret.setAttribute("onclick", "showPackage('" + packageInfo.name + "')");
    li.appendChild(caret);

    var nestedUl = document.createElement("ul");
    nestedUl.className = "nested active"; // Expanded by default

    // Sort types by name
    var types = (packageInfo.types || []).sort(function(a, b) {
        return a.name.localeCompare(b.name);
    });

    types.forEach(function(typeInfo) {
        var typeNode = createTypeNode(packageInfo.name, typeInfo);
        nestedUl.appendChild(typeNode);
    });

    li.appendChild(nestedUl);
    return li;
}

/**
 * Creates a tree node for a type (class, interface, enum, annotation)
 */
function createTypeNode(packageName, typeInfo) {
    var li = document.createElement("li");

    var span = document.createElement("span");
    span.className = "element";
    span.textContent = typeInfo.name;
    span.setAttribute("onclick", "showType('" + packageName + "', '" + typeInfo.name + "')");
    li.appendChild(span);

    return li;
}

/**
 * Shows the package detail view
 */
function showPackage(packageName) {
    var packageInfo = findPackage(packageName);
    if (!packageInfo) {
        showError("Package not found: " + packageName);
        return;
    }

    // Highlight the selected package node in the tree
    highlightTreeNode(packageName, null);

    var html = '<div class="detail-container">';
    html += '<div class="package-header">';
    html += '<div class="package-name">' + escapeHtml(packageName) + '</div>';
    html += '</div>';

    // JavaDoc
    if (packageInfo.javadoc) {
        html += renderJavaDoc(packageInfo.javadoc);
    }

    // Types in this package
    html += '<div class="package-types">';
    html += '<h2 class="members-section-title">Types</h2>';
    html += '<ul class="package-type-list">';

    var types = packageInfo.types || [];
    types.forEach(function(typeInfo) {
        html += '<li class="package-type-item" onclick="showType(\'' + packageName + '\', \'' + typeInfo.name + '\')">';
        html += '<span class="package-type-name">' + escapeHtml(typeInfo.name) + '</span>';
        html += '<span class="package-type-kind">(' + escapeHtml(typeInfo.kind) + ')</span>';
        html += '</li>';
    });

    html += '</ul>';
    html += '</div>';
    html += '</div>';

    setContent(packageName, html);
}

/**
 * Shows the type detail view
 */
function showType(packageName, typeName) {
    var typeInfo = findType(packageName, typeName);
    if (!typeInfo) {
        showError("Type not found: " + packageName + "." + typeName);
        return;
    }

    // Highlight the selected type node in the tree
    highlightTreeNode(packageName, typeName);

    var html = '<div class="detail-container">';
    html += renderTypeHeader(typeInfo);
    html += renderModifiers(typeInfo.modifiers);
    html += renderAnnotations(typeInfo.annotations);
    html += renderTypeParameters(typeInfo.typeParameters);
    html += renderInheritance(typeInfo);
    html += renderJavaDoc(typeInfo.javadoc);

    // Render kind-specific content
    if (typeInfo.kind === 'class') {
        html += renderClassMembers(typeInfo);
    } else if (typeInfo.kind === 'interface') {
        html += renderInterfaceMembers(typeInfo);
    } else if (typeInfo.kind === 'enum') {
        html += renderEnumMembers(typeInfo);
    } else if (typeInfo.kind === 'annotation') {
        html += renderAnnotationMembers(typeInfo);
    }

    html += '</div>';

    setContent(typeInfo.qualifiedName, html);
}

/**
 * Renders the type header
 */
function renderTypeHeader(typeInfo) {
    var html = '<div class="type-header">';
    html += '<h1 class="type-title">';
    html += escapeHtml(typeInfo.name);
    html += '<span class="type-kind ' + typeInfo.kind + '">' + escapeHtml(typeInfo.kind) + '</span>';
    html += '</h1>';
    html += '<div class="type-qualified-name">' + escapeHtml(typeInfo.qualifiedName) + '</div>';
    html += '</div>';
    return html;
}

/**
 * Renders modifiers
 */
function renderModifiers(modifiers) {
    if (!modifiers || modifiers.length === 0) return '';

    var html = '<div class="modifiers">';
    modifiers.forEach(function(modifier) {
        html += '<span class="modifier ' + modifier + '">' + escapeHtml(modifier) + '</span>';
    });
    html += '</div>';
    return html;
}

/**
 * Renders annotations
 */
function renderAnnotations(annotations) {
    if (!annotations || annotations.length === 0) return '';

    var html = '<div class="annotations">';
    annotations.forEach(function(annotation) {
        html += '<div class="annotation">';
        html += '<span class="annotation-type">@' + makeTypeLink(annotation.type) + '</span>';
        if (annotation.values && Object.keys(annotation.values).length > 0) {
            html += '<span class="annotation-values">(';
            var values = [];
            for (var key in annotation.values) {
                values.push(key + '=' + JSON.stringify(annotation.values[key]));
            }
            html += escapeHtml(values.join(', '));
            html += ')</span>';
        }
        html += '</div>';
    });
    html += '</div>';
    return html;
}

/**
 * Renders type parameters
 */
function renderTypeParameters(typeParameters) {
    if (!typeParameters || typeParameters.length === 0) return '';

    var html = '<div class="type-parameters">';
    html += '<strong>Type Parameters:</strong> ';
    typeParameters.forEach(function(param, index) {
        if (index > 0) html += ', ';
        html += '<span class="type-parameter">' + escapeHtml(param.name);
        if (param.bounds && param.bounds.length > 0) {
            html += ' extends ';
            // Make bounds clickable
            var linkedBounds = param.bounds.map(function(bound) {
                return parseAndLinkTypes(bound);
            });
            html += linkedBounds.join(' &amp; ');
        }
        html += '</span>';
    });
    html += '</div>';
    return html;
}

/**
 * Renders inheritance information
 */
function renderInheritance(typeInfo) {
    var html = '';

    if (typeInfo.superClass) {
        html += '<div class="inheritance-section">';
        html += '<span class="inheritance-label">Extends:</span>';
        html += '<span class="inheritance-type">' + makeTypeLink(typeInfo.superClass) + '</span>';
        html += '</div>';
    }

    var interfaces = typeInfo.interfaces || typeInfo.superInterfaces || [];
    if (interfaces.length > 0) {
        html += '<div class="inheritance-section">';
        html += '<span class="inheritance-label">' + (typeInfo.kind === 'interface' ? 'Extends:' : 'Implements:') + '</span>';
        interfaces.forEach(function(iface, index) {
            if (index > 0) html += ', ';
            html += '<span class="inheritance-type">' + makeTypeLink(iface) + '</span>';
        });
        html += '</div>';
    }

    return html;
}

/**
 * Renders JavaDoc
 */
function renderJavaDoc(javadoc) {
    if (!javadoc) return '';

    var html = '<div class="javadoc-section">';

    if (javadoc.description) {
        html += '<div class="javadoc-description">' + javadoc.description + '</div>';
    }

    if (javadoc.tags && javadoc.tags.length > 0) {
        html += '<div class="javadoc-tags">';
        javadoc.tags.forEach(function(tag) {
            html += '<div class="javadoc-tag">';
            html += '<span class="javadoc-tag-kind">@' + escapeHtml(tag.kind) + '</span>';

            if (tag.name) {
                html += '<span class="javadoc-tag-name">' + escapeHtml(tag.name) + '</span>';
            }

            // Exception types are clickable
            if (tag.exception) {
                html += '<span class="javadoc-tag-name">' + makeTypeLink(tag.exception) + '</span>';
            }

            // @see references are clickable
            if (tag.reference) {
                html += '<span class="javadoc-tag-name">' + makeTypeLink(tag.reference) + '</span>';
            }

            if (tag.description) {
                html += '<span class="javadoc-tag-description">' + tag.description + '</span>';
            }

            if (tag.content) {
                html += '<code>' + escapeHtml(tag.content) + '</code>';
            }

            html += '</div>';
        });
        html += '</div>';
    }

    html += '</div>';
    return html;
}

/**
 * Renders class members
 */
function renderClassMembers(classInfo) {
    var html = '';

    // Fields
    if (classInfo.fields && classInfo.fields.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Fields</h2>';
        classInfo.fields.forEach(function(field) {
            html += renderField(field);
        });
        html += '</div>';
    }

    // Constructors
    if (classInfo.constructors && classInfo.constructors.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Constructors</h2>';
        classInfo.constructors.forEach(function(constructor) {
            html += renderConstructor(constructor);
        });
        html += '</div>';
    }

    // Methods
    if (classInfo.methods && classInfo.methods.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Methods</h2>';
        classInfo.methods.forEach(function(method) {
            html += renderMethod(method);
        });
        html += '</div>';
    }

    return html;
}

/**
 * Renders interface members
 */
function renderInterfaceMembers(interfaceInfo) {
    var html = '';

    // Fields (constants)
    if (interfaceInfo.fields && interfaceInfo.fields.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Constants</h2>';
        interfaceInfo.fields.forEach(function(field) {
            html += renderField(field);
        });
        html += '</div>';
    }

    // Methods
    if (interfaceInfo.methods && interfaceInfo.methods.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Methods</h2>';
        interfaceInfo.methods.forEach(function(method) {
            html += renderMethod(method);
        });
        html += '</div>';
    }

    return html;
}

/**
 * Renders enum members
 */
function renderEnumMembers(enumInfo) {
    var html = '';

    // Enum constants
    if (enumInfo.constants && enumInfo.constants.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Enum Constants</h2>';
        html += '<div class="enum-constants">';
        enumInfo.constants.forEach(function(constant) {
            html += '<div class="enum-constant">';
            html += '<div class="enum-constant-name">' + escapeHtml(constant.name) + '</div>';
            html += renderAnnotations(constant.annotations);
            html += renderJavaDoc(constant.javadoc);
            html += '</div>';
        });
        html += '</div>';
        html += '</div>';
    }

    // Fields
    if (enumInfo.fields && enumInfo.fields.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Fields</h2>';
        enumInfo.fields.forEach(function(field) {
            html += renderField(field);
        });
        html += '</div>';
    }

    // Constructors
    if (enumInfo.constructors && enumInfo.constructors.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Constructors</h2>';
        enumInfo.constructors.forEach(function(constructor) {
            html += renderConstructor(constructor);
        });
        html += '</div>';
    }

    // Methods
    if (enumInfo.methods && enumInfo.methods.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Methods</h2>';
        enumInfo.methods.forEach(function(method) {
            html += renderMethod(method);
        });
        html += '</div>';
    }

    return html;
}

/**
 * Renders annotation members
 */
function renderAnnotationMembers(annotationInfo) {
    var html = '';

    if (annotationInfo.elements && annotationInfo.elements.length > 0) {
        html += '<div class="members-section">';
        html += '<h2 class="members-section-title">Elements</h2>';
        html += '<div class="annotation-elements">';
        annotationInfo.elements.forEach(function(element) {
            html += '<div class="annotation-element">';
            html += '<span class="annotation-element-type">' + parseAndLinkTypes(element.type) + '</span> ';
            html += '<span class="annotation-element-name">' + escapeHtml(element.name) + '()</span>';
            if (element.defaultValue !== null && element.defaultValue !== undefined) {
                html += '<div class="annotation-element-default">default: ' + escapeHtml(JSON.stringify(element.defaultValue)) + '</div>';
            }
            html += renderJavaDoc(element.javadoc);
            html += '</div>';
        });
        html += '</div>';
        html += '</div>';
    }

    return html;
}

/**
 * Helper function to check if JavaDoc has meaningful content
 */
function hasJavaDoc(javadoc) {
    if (!javadoc) return false;
    if (javadoc.description && javadoc.description.trim() !== '') return true;
    if (javadoc.tags && javadoc.tags.length > 0) return true;
    return false;
}

/**
 * Toggles the expanded state of a member item
 */
function toggleMemberExpand(event) {
    var target = event.currentTarget;
    target.classList.toggle('expanded');
    event.stopPropagation();
}

/**
 * Renders a field
 */
function renderField(field) {
    var expandable = hasJavaDoc(field.javadoc);
    var html = '<div class="member-item' + (expandable ? ' expandable' : '') + '"' +
               (expandable ? ' onclick="toggleMemberExpand(event)"' : '') + '>';

    // Render annotations first (on separate line if present)
    if (field.annotations && field.annotations.length > 0) {
        html += renderAnnotations(field.annotations);
    }

    // Render signature: name : type modifiers
    html += '<div class="member-signature">';

    // Field name first
    html += '<span class="member-name">' + escapeHtml(field.name) + '</span>';

    // Colon separator and type
    html += ' <span class="member-separator">:</span> ';
    html += '<span class="member-type">' + parseAndLinkTypes(field.type) + '</span>';

    // Modifiers last
    if (field.modifiers && field.modifiers.length > 0) {
        html += ' ';
        field.modifiers.forEach(function(modifier) {
            html += '<span class="modifier-inline ' + modifier + '">' + escapeHtml(modifier) + '</span> ';
        });
    }

    html += '</div>';

    if (field.constantValue !== null && field.constantValue !== undefined) {
        html += '<div class="member-constant-value">Value: ' + escapeHtml(JSON.stringify(field.constantValue)) + '</div>';
    }

    html += renderJavaDoc(field.javadoc);
    html += '</div>';
    return html;
}

/**
 * Renders a constructor
 */
function renderConstructor(constructor) {
    var expandable = hasJavaDoc(constructor.javadoc);
    var html = '<div class="member-item' + (expandable ? ' expandable' : '') + '"' +
               (expandable ? ' onclick="toggleMemberExpand(event)"' : '') + '>';

    // Render annotations first (on separate line if present)
    if (constructor.annotations && constructor.annotations.length > 0) {
        html += renderAnnotations(constructor.annotations);
    }

    // Render signature: name(params) modifiers
    html += '<div class="member-signature">';

    // Constructor name first
    html += '<span class="member-name">' + escapeHtml(constructor.name) + '</span>';

    // Parameters
    html += '(';
    if (constructor.parameters && constructor.parameters.length > 0) {
        var params = constructor.parameters.map(function(param) {
            var paramHtml = '<span class="member-parameter">';
            paramHtml += '<span class="member-parameter-type">' + parseAndLinkTypes(param.type) + '</span> ';
            paramHtml += '<span class="member-parameter-name">' + escapeHtml(param.name) + '</span>';
            if (param.isVarArgs) paramHtml += '...';
            paramHtml += '</span>';
            return paramHtml;
        });
        html += params.join(', ');
    }
    html += ')';

    // Modifiers last
    if (constructor.modifiers && constructor.modifiers.length > 0) {
        html += ' ';
        constructor.modifiers.forEach(function(modifier) {
            html += '<span class="modifier-inline ' + modifier + '">' + escapeHtml(modifier) + '</span> ';
        });
    }

    html += '</div>';

    if (constructor.exceptions && constructor.exceptions.length > 0) {
        html += '<div class="member-exceptions">throws ';
        html += constructor.exceptions.map(function(ex) {
            return '<span class="member-exception">' + makeTypeLink(ex) + '</span>';
        }).join(', ');
        html += '</div>';
    }

    html += renderJavaDoc(constructor.javadoc);
    html += '</div>';
    return html;
}

/**
 * Renders a method
 */
function renderMethod(method) {
    var expandable = hasJavaDoc(method.javadoc);
    var html = '<div class="member-item' + (expandable ? ' expandable' : '') + '"' +
               (expandable ? ' onclick="toggleMemberExpand(event)"' : '') + '>';

    // Render annotations first (on separate line if present)
    if (method.annotations && method.annotations.length > 0) {
        html += renderAnnotations(method.annotations);
    }

    // Render signature: name(params) : returnType modifiers
    html += '<div class="member-signature">';

    // Method name first
    html += '<span class="member-name">' + escapeHtml(method.name) + '</span>';

    // Parameters
    html += '(';
    if (method.parameters && method.parameters.length > 0) {
        var params = method.parameters.map(function(param) {
            var paramHtml = '<span class="member-parameter">';
            paramHtml += '<span class="member-parameter-type">' + parseAndLinkTypes(param.type) + '</span> ';
            paramHtml += '<span class="member-parameter-name">' + escapeHtml(param.name) + '</span>';
            if (param.isVarArgs) paramHtml += '...';
            paramHtml += '</span>';
            return paramHtml;
        });
        html += params.join(', ');
    }
    html += ')';

    // Colon separator and return type
    html += ' <span class="member-separator">:</span> ';
    html += '<span class="member-type">' + parseAndLinkTypes(method.returnType) + '</span>';

    // Type parameters (if any)
    if (method.typeParameters && method.typeParameters.length > 0) {
        html += '&lt;';
        html += method.typeParameters.map(function(tp) {
            return escapeHtml(tp.name);
        }).join(', ');
        html += '&gt;';
    }

    // Modifiers last
    if (method.modifiers && method.modifiers.length > 0) {
        html += ' ';
        method.modifiers.forEach(function(modifier) {
            html += '<span class="modifier-inline ' + modifier + '">' + escapeHtml(modifier) + '</span> ';
        });
    }

    html += '</div>';

    if (method.exceptions && method.exceptions.length > 0) {
        html += '<div class="member-exceptions">throws ';
        html += method.exceptions.map(function(ex) {
            return '<span class="member-exception">' + makeTypeLink(ex) + '</span>';
        }).join(', ');
        html += '</div>';
    }

    html += renderJavaDoc(method.javadoc);
    html += '</div>';
    return html;
}

/**
 * Helper function to find a package by name
 */
function findPackage(packageName) {
    if (!documentationModel || !documentationModel.packages) return null;

    for (var i = 0; i < documentationModel.packages.length; i++) {
        if (documentationModel.packages[i].name === packageName) {
            return documentationModel.packages[i];
        }
    }
    return null;
}

/**
 * Helper function to find a type by package and name
 */
function findType(packageName, typeName) {
    var packageInfo = findPackage(packageName);
    if (!packageInfo || !packageInfo.types) return null;

    for (var i = 0; i < packageInfo.types.length; i++) {
        if (packageInfo.types[i].name === typeName) {
            return packageInfo.types[i];
        }
    }
    return null;
}

/**
 * Helper function to find a type by its qualified name
 * @param {string} qualifiedName - The fully qualified type name (e.g., "java.lang.String")
 * @returns {Object|null} The type info object or null if not found
 */
function findTypeByQualifiedName(qualifiedName) {
    if (!qualifiedName || !documentationModel || !documentationModel.packages) return null;

    // Try to find the type across all packages
    for (var i = 0; i < documentationModel.packages.length; i++) {
        var pkg = documentationModel.packages[i];
        if (!pkg.types) continue;

        for (var j = 0; j < pkg.types.length; j++) {
            var type = pkg.types[j];
            if (type.qualifiedName === qualifiedName) {
                return type;
            }
        }
    }
    return null;
}

/**
 * Helper function to find a type by its simple name (searches across all packages)
 * @param {string} simpleName - The simple type name (e.g., "String")
 * @returns {Object|null} The type info object or null if not found
 */
function findTypeBySimpleName(simpleName) {
    if (!simpleName || !documentationModel || !documentationModel.packages) return null;

    // Try to find the type by simple name across all packages
    for (var i = 0; i < documentationModel.packages.length; i++) {
        var pkg = documentationModel.packages[i];
        if (!pkg.types) continue;

        for (var j = 0; j < pkg.types.length; j++) {
            var type = pkg.types[j];
            if (type.name === simpleName) {
                return type;
            }
        }
    }
    return null;
}

/**
 * Navigates to a type by its qualified name
 * @param {string} qualifiedName - The fully qualified type name
 */
function navigateToType(qualifiedName) {
    if (!qualifiedName) return;

    var typeInfo = findTypeByQualifiedName(qualifiedName);
    if (typeInfo) {
        // Extract package name from qualified name
        var lastDotIndex = qualifiedName.lastIndexOf('.');
        var packageName = lastDotIndex > 0 ? qualifiedName.substring(0, lastDotIndex) : '';
        var typeName = typeInfo.name;

        showType(packageName, typeName);
    } else {
        // Type not found in documentation - could be external (java.lang.*, etc.)
        console.log('Type not found in documentation: ' + qualifiedName);
    }
}

/**
 * Creates a clickable link for a type reference
 * @param {string} typeNameOrQualified - The type name (qualified or simple)
 * @param {string} displayText - The text to display (optional, defaults to simple name)
 * @returns {string} HTML string for a clickable type link
 */
function makeTypeLink(typeNameOrQualified, displayText) {
    if (!typeNameOrQualified) return '';

    // Try to find type - first by qualified name, then by simple name
    var typeInfo = findTypeByQualifiedName(typeNameOrQualified);
    if (!typeInfo) {
        // If not found by qualified name, try simple name
        typeInfo = findTypeBySimpleName(typeNameOrQualified);
    }

    // Determine display text
    var text = displayText || typeNameOrQualified;
    if (!displayText && typeInfo) {
        // Use simple name from found type
        text = typeInfo.name;
    } else if (!displayText) {
        // Extract simple name from input
        var lastDotIndex = typeNameOrQualified.lastIndexOf('.');
        text = lastDotIndex > 0 ? typeNameOrQualified.substring(lastDotIndex + 1) : typeNameOrQualified;
    }

    if (typeInfo) {
        // Type found in documentation - make it clickable
        return '<span class="type-link" onclick="navigateToType(\'' + escapeHtml(typeInfo.qualifiedName) + '\')">' +
               escapeHtml(text) + '</span>';
    } else {
        // External type (not in our documentation) - show as plain text with different styling
        return '<span class="type-reference-external">' + escapeHtml(text) + '</span>';
    }
}

/**
 * Parses a type string (potentially with generics) and creates clickable links
 * Handles complex types like "List<String>", "Map<String, List<Integer>>", arrays, etc.
 * @param {string} typeString - The type string to parse
 * @returns {string} HTML string with clickable type links
 */
function parseAndLinkTypes(typeString) {
    if (!typeString) return '';

    // Handle primitive types - not clickable
    var primitives = ['void', 'boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char'];
    if (primitives.indexOf(typeString) !== -1) {
        return escapeHtml(typeString);
    }

    var result = '';
    var i = 0;
    var currentType = '';

    while (i < typeString.length) {
        var char = typeString.charAt(i);

        if (char === '<' || char === '>' || char === ',' || char === '[' || char === ']') {
            // We hit a delimiter - process accumulated type
            if (currentType.trim()) {
                var trimmed = currentType.trim();
                // Check if it's a primitive or keyword
                if (primitives.indexOf(trimmed) === -1 && trimmed !== 'extends' && trimmed !== 'super') {
                    result += makeTypeLink(trimmed);
                } else {
                    result += escapeHtml(trimmed);
                }
                currentType = '';
            }

            // Add the delimiter
            if (char === ',') {
                result += escapeHtml(char) + ' ';
            } else {
                result += escapeHtml(char);
            }
            i++;
        } else if (char === ' ') {
            // Space - could be separator or part of "extends"/"super"
            if (currentType.trim()) {
                var trimmed = currentType.trim();
                if (trimmed === 'extends' || trimmed === 'super') {
                    result += escapeHtml(trimmed) + ' ';
                    currentType = '';
                } else {
                    currentType += char;
                }
            }
            i++;
        } else {
            // Regular character - accumulate
            currentType += char;
            i++;
        }
    }

    // Process any remaining type
    if (currentType.trim()) {
        var trimmed = currentType.trim();
        if (primitives.indexOf(trimmed) === -1 && trimmed !== 'extends' && trimmed !== 'super') {
            result += makeTypeLink(trimmed);
        } else {
            result += escapeHtml(trimmed);
        }
    }

    return result;
}

/**
 * Sets the content of the detail view
 */
function setContent(title, html) {
    var docContent = document.getElementById('docContent');
    if (docContent) docContent.innerHTML = html;
}

/**
 * Shows an error message
 */
function showError(message) {
    setContent('Error', '<div class="empty-state">' + escapeHtml(message) + '</div>');
}

/**
 * Escapes HTML special characters
 */
function escapeHtml(text) {
    if (text === null || text === undefined) return '';

    var map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };

    return String(text).replace(/[&<>"']/g, function(m) { return map[m]; });
}

/**
 * Highlights the selected tree node and removes previous selection
 * @param {string} packageName - The package name
 * @param {string} typeName - The type name (null for package nodes)
 */
function highlightTreeNode(packageName, typeName) {
    // Remove previous selection
    if (currentSelectedElement) {
        currentSelectedElement.classList.remove('selected');
        currentSelectedElement = null;
    }

    // Find and highlight the new selection
    if (typeName) {
        // Highlighting a type node (class, interface, etc.)
        var elements = document.querySelectorAll('.element');
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            var onclick = element.getAttribute('onclick');
            if (onclick && onclick.indexOf("showType('" + packageName + "', '" + typeName + "')") !== -1) {
                element.classList.add('selected');
                currentSelectedElement = element;
                // Ensure parent package is expanded and visible
                expandParent(element.parentElement);
                break;
            }
        }
    } else {
        // Highlighting a package node
        var carets = document.querySelectorAll('.caret');
        for (var i = 0; i < carets.length; i++) {
            var caret = carets[i];
            var onclick = caret.getAttribute('onclick');
            if (onclick && onclick.indexOf("showPackage('" + packageName + "')") !== -1) {
                caret.classList.add('selected');
                currentSelectedElement = caret;
                break;
            }
        }
    }
}
