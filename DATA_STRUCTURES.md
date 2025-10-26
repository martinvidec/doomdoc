# Data Structures for DoomDoc

This document defines the JSON data structures used to represent Java source code and JavaDoc comments in DoomDoc.

## Overview

The data structure captures all relevant information from Java classes, interfaces, enums, and annotations, including their JavaDoc comments. This JSON representation serves as the intermediate format that feeds the DoomDoclet HTML generation.

All JSON structures defined in this document are mapped to Java DTO (Data Transfer Object) classes in the package `at.videc.bomblet.dto`.

## Root Structure: Documentation Model

**Java DTO:** `at.videc.bomblet.dto.DocumentationModel`

```json
{
  "packages": [
    {
      "name": "at.videc",
      "javadoc": { /* JavaDocComment */ },
      "types": [ /* ClassInfo | InterfaceInfo | EnumInfo | AnnotationInfo */ ]
    }
  ]
}
```

## Package Structure

**Java DTO:** `at.videc.bomblet.dto.PackageInfo`

```json
{
  "name": "at.videc.dummy",
  "javadoc": {
    "description": "Package description from package-info.java",
    "tags": [ /* JavaDocTag */ ]
  },
  "types": [ /* Array of type definitions */ ]
}
```

## Type Definitions

### ClassInfo

**Java DTO:** `at.videc.bomblet.dto.ClassInfo` (extends `TypeInfo`)

```json
{
  "kind": "class",
  "name": "TestClass1",
  "qualifiedName": "at.videc.dummy.TestClass1",
  "modifiers": ["public"],
  "javadoc": { /* JavaDocComment */ },
  "annotations": [ /* AnnotationUsage */ ],
  "typeParameters": [ /* TypeParameter */ ],
  "superClass": "java.lang.Object",
  "interfaces": ["java.io.Serializable"],
  "fields": [ /* FieldInfo */ ],
  "constructors": [ /* ConstructorInfo */ ],
  "methods": [ /* MethodInfo */ ],
  "innerTypes": [ /* Nested ClassInfo/InterfaceInfo/etc */ ]
}
```

### InterfaceInfo

**Java DTO:** `at.videc.bomblet.dto.InterfaceInfo` (extends `TypeInfo`)

```json
{
  "kind": "interface",
  "name": "MyInterface",
  "qualifiedName": "at.videc.MyInterface",
  "modifiers": ["public"],
  "javadoc": { /* JavaDocComment */ },
  "annotations": [ /* AnnotationUsage */ ],
  "typeParameters": [ /* TypeParameter */ ],
  "superInterfaces": ["java.io.Closeable"],
  "methods": [ /* MethodInfo */ ],
  "fields": [ /* FieldInfo - only constants */ ],
  "innerTypes": [ /* Nested types */ ]
}
```

### EnumInfo

**Java DTO:** `at.videc.bomblet.dto.EnumInfo` (extends `TypeInfo`)

```json
{
  "kind": "enum",
  "name": "Status",
  "qualifiedName": "at.videc.Status",
  "modifiers": ["public"],
  "javadoc": { /* JavaDocComment */ },
  "annotations": [ /* AnnotationUsage */ ],
  "interfaces": [],
  "constants": [
    /* EnumConstantInfo - see at.videc.bomblet.dto.EnumConstantInfo */
    {
      "name": "ACTIVE",
      "javadoc": { /* JavaDocComment */ },
      "annotations": [ /* AnnotationUsage */ ]
    },
    {
      "name": "INACTIVE",
      "javadoc": { /* JavaDocComment */ },
      "annotations": [ /* AnnotationUsage */ ]
    }
  ],
  "fields": [ /* FieldInfo */ ],
  "constructors": [ /* ConstructorInfo */ ],
  "methods": [ /* MethodInfo */ ]
}
```

### AnnotationInfo

**Java DTO:** `at.videc.bomblet.dto.AnnotationInfo` (extends `TypeInfo`)

```json
{
  "kind": "annotation",
  "name": "BOMB",
  "qualifiedName": "at.videc.BOMB",
  "modifiers": ["public"],
  "javadoc": { /* JavaDocComment */ },
  "annotations": [ /* AnnotationUsage - meta-annotations */ ],
  "elements": [
    /* AnnotationElementInfo - see at.videc.bomblet.dto.AnnotationElementInfo */
    {
      "name": "value",
      "type": "java.lang.String",
      "javadoc": { /* JavaDocComment */ },
      "defaultValue": null
    }
  ]
}
```

## Member Definitions

### FieldInfo

**Java DTO:** `at.videc.bomblet.dto.FieldInfo`

```json
{
  "name": "memberVariable",
  "type": "java.lang.String",
  "modifiers": ["private"],
  "javadoc": { /* JavaDocComment */ },
  "annotations": [ /* AnnotationUsage */ ],
  "constantValue": null
}
```

### ConstructorInfo

**Java DTO:** `at.videc.bomblet.dto.ConstructorInfo`

```json
{
  "name": "TestClass1",
  "modifiers": ["public"],
  "javadoc": { /* JavaDocComment */ },
  "annotations": [ /* AnnotationUsage */ ],
  "parameters": [ /* ParameterInfo */ ],
  "exceptions": ["java.io.IOException"],
  "typeParameters": [ /* TypeParameter */ ]
}
```

### MethodInfo

**Java DTO:** `at.videc.bomblet.dto.MethodInfo`

```json
{
  "name": "testMethod1",
  "modifiers": ["public"],
  "javadoc": { /* JavaDocComment */ },
  "annotations": [ /* AnnotationUsage */ ],
  "typeParameters": [ /* TypeParameter */ ],
  "returnType": "void",
  "parameters": [ /* ParameterInfo */ ],
  "exceptions": ["java.lang.IllegalArgumentException"],
  "isDefault": false
}
```

### ParameterInfo

**Java DTO:** `at.videc.bomblet.dto.ParameterInfo`

```json
{
  "name": "userName",
  "type": "java.lang.String",
  "annotations": [ /* AnnotationUsage */ ],
  "isVarArgs": false
}
```

## Generic Type Support

### TypeParameter

**Java DTO:** `at.videc.bomblet.dto.TypeParameter`

```json
{
  "name": "T",
  "bounds": ["java.lang.Number", "java.io.Serializable"]
}
```

## Annotation Support

### AnnotationUsage

**Java DTO:** `at.videc.bomblet.dto.AnnotationUsage`

```json
{
  "type": "at.videc.BOMB",
  "values": {
    "value": "AnalyseTestKlasse"
  }
}
```

For complex annotation values:

```json
{
  "type": "org.springframework.web.bind.annotation.RequestMapping",
  "values": {
    "value": ["/api/users", "/api/accounts"],
    "method": "RequestMethod.GET",
    "produces": "application/json"
  }
}
```

## JavaDoc Representation

### JavaDocComment

**Java DTO:** `at.videc.bomblet.dto.JavaDocComment`

```json
{
  "description": "This is TestClass1.\n\nDetailed description with multiple paragraphs.",
  "tags": [
    {
      "kind": "param",
      "name": "userName",
      "description": "the name of the user"
    },
    {
      "kind": "return",
      "description": "the user object if found, null otherwise"
    },
    {
      "kind": "throws",
      "exception": "java.io.IOException",
      "description": "if the file cannot be read"
    },
    {
      "kind": "see",
      "reference": "java.util.List"
    },
    {
      "kind": "since",
      "description": "1.0"
    },
    {
      "kind": "deprecated",
      "description": "Use newMethod() instead"
    },
    {
      "kind": "author",
      "description": "John Doe"
    }
  ]
}
```

### JavaDocTag

**Java DTO:** `at.videc.bomblet.dto.JavaDocTag`

Different tag structures based on kind:

**@param tag:**
```json
{
  "kind": "param",
  "name": "userName",
  "description": "the name of the user to search for"
}
```

**@return tag:**
```json
{
  "kind": "return",
  "description": "the calculated result"
}
```

**@throws/@exception tag:**
```json
{
  "kind": "throws",
  "exception": "java.io.IOException",
  "description": "if the file cannot be read"
}
```

**@see tag:**
```json
{
  "kind": "see",
  "reference": "java.util.List#add(Object)"
}
```

**@since tag:**
```json
{
  "kind": "since",
  "description": "1.0.0"
}
```

**@deprecated tag:**
```json
{
  "kind": "deprecated",
  "description": "Use the new API instead"
}
```

**@author tag:**
```json
{
  "kind": "author",
  "description": "Jane Smith"
}
```

**@version tag:**
```json
{
  "kind": "version",
  "description": "2.1.0"
}
```

**@link/@linkplain tag:**
```json
{
  "kind": "link",
  "reference": "java.util.Map",
  "label": "Map interface"
}
```

**@code/@literal tag:**
```json
{
  "kind": "code",
  "content": "int x = 42;"
}
```

## Modifiers

Modifiers are represented as an array of strings. Valid values:

- Visibility: `"public"`, `"protected"`, `"private"`, `"package-private"`
- Class/Method: `"static"`, `"final"`, `"abstract"`, `"synchronized"`, `"native"`, `"strictfp"`
- Field: `"volatile"`, `"transient"`

Example:
```json
"modifiers": ["public", "static", "final"]
```

## Complete Example

Here's a complete example for a simple class:

```json
{
  "packages": [
    {
      "name": "at.videc.dummy",
      "javadoc": null,
      "types": [
        {
          "kind": "class",
          "name": "TestClass1",
          "qualifiedName": "at.videc.dummy.TestClass1",
          "modifiers": ["public"],
          "javadoc": {
            "description": "This is TestClass1.",
            "tags": []
          },
          "annotations": [
            {
              "type": "at.videc.BOMB",
              "values": {
                "value": "AnalyseTestKlasse"
              }
            }
          ],
          "typeParameters": [],
          "superClass": "java.lang.Object",
          "interfaces": [],
          "fields": [],
          "constructors": [],
          "methods": [
            {
              "name": "testMethod1",
              "modifiers": ["public"],
              "javadoc": {
                "description": "This is a test method.",
                "tags": []
              },
              "annotations": [],
              "typeParameters": [],
              "returnType": "void",
              "parameters": [],
              "exceptions": [],
              "isDefault": false
            }
          ],
          "innerTypes": []
        }
      ]
    }
  ]
}
```

## Usage in DoomDoclet

The DoomDoclet will:

1. **Parse** Java source files using the Doclet API
2. **Transform** the Doclet API elements into this JSON structure
3. **Serialize** the structure using Gson
4. **Embed** the JSON data into the generated HTML
5. **Render** the tree view and detail panels from the JSON data using JavaScript

### Benefits of This Structure

- **Complete Information**: Captures all relevant Java and JavaDoc information
- **Hierarchical**: Maintains package → type → member hierarchy
- **Type-Safe**: Clear distinction between classes, interfaces, enums, and annotations
- **Searchable**: All text content is easily searchable in the generated HTML
- **Extensible**: Easy to add new fields without breaking existing functionality
- **Standard Format**: Uses familiar JSON structure that JavaScript can easily consume

## Implementation Notes

### Type References

Type names should use:
- **Simple names** for primitive types: `"int"`, `"boolean"`, `"char"`, etc.
- **Qualified names** for reference types: `"java.lang.String"`, `"java.util.List"`
- **Generic notation** for parameterized types: `"java.util.List<java.lang.String>"`
- **Array notation**: `"java.lang.String[]"`, `"int[][]"`

### Null Values

- Fields that are not applicable should be `null` (e.g., `superClass` for interfaces)
- Empty collections should be represented as empty arrays `[]`, not `null`
- Optional JavaDoc should be `null` if not present

### Ordering

- Types within a package: alphabetical by name
- Members within a type: order as they appear in source code
- Modifiers: standard order (visibility, static, final, etc.)
