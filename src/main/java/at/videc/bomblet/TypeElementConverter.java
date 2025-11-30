package at.videc.bomblet;

import at.videc.bomblet.dto.*;
import jdk.javadoc.doclet.DocletEnvironment;
import com.sun.source.doctree.*;
import com.sun.source.util.DocTrees;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converts Java Doclet API elements (TypeElement, etc.) into DTO objects.
 * This is a basic converter that extracts essential information.
 */
public class TypeElementConverter {

    private final DocletEnvironment environment;

    public TypeElementConverter(DocletEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Converts a TypeElement to the appropriate TypeInfo subclass.
     *
     * @param typeElement the type element to convert
     * @return the corresponding TypeInfo DTO
     */
    public TypeInfo convert(TypeElement typeElement) {
        ElementKind kind = typeElement.getKind();

        switch (kind) {
            case CLASS:
                return convertToClassInfo(typeElement);
            case INTERFACE:
                return convertToInterfaceInfo(typeElement);
            case ENUM:
                return convertToEnumInfo(typeElement);
            case ANNOTATION_TYPE:
                return convertToAnnotationInfo(typeElement);
            default:
                throw new IllegalArgumentException("Unsupported element kind: " + kind);
        }
    }

    /**
     * Converts a TypeElement to ClassInfo.
     */
    private ClassInfo convertToClassInfo(TypeElement typeElement) {
        ClassInfo classInfo = new ClassInfo();
        fillCommonTypeInfo(classInfo, typeElement);

        // Superclass
        TypeMirror superclass = typeElement.getSuperclass();
        if (superclass != null) {
            classInfo.setSuperClass(superclass.toString());
        }

        // Interfaces
        List<String> interfaces = new ArrayList<>();
        for (TypeMirror iface : typeElement.getInterfaces()) {
            interfaces.add(iface.toString());
        }
        classInfo.setInterfaces(interfaces);

        // Extract fields
        classInfo.setFields(extractFields(typeElement));

        // Extract constructors
        classInfo.setConstructors(extractConstructors(typeElement));

        // Extract methods
        classInfo.setMethods(extractMethods(typeElement));

        return classInfo;
    }

    /**
     * Converts a TypeElement to InterfaceInfo.
     */
    private InterfaceInfo convertToInterfaceInfo(TypeElement typeElement) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        fillCommonTypeInfo(interfaceInfo, typeElement);

        // Super interfaces
        List<String> superInterfaces = new ArrayList<>();
        for (TypeMirror iface : typeElement.getInterfaces()) {
            superInterfaces.add(iface.toString());
        }
        interfaceInfo.setSuperInterfaces(superInterfaces);

        // Extract fields (constants in interfaces)
        interfaceInfo.setFields(extractFields(typeElement));

        // Extract methods
        interfaceInfo.setMethods(extractMethods(typeElement));

        return interfaceInfo;
    }

    /**
     * Converts a TypeElement to EnumInfo.
     */
    private EnumInfo convertToEnumInfo(TypeElement typeElement) {
        EnumInfo enumInfo = new EnumInfo();
        fillCommonTypeInfo(enumInfo, typeElement);

        // Interfaces
        List<String> interfaces = new ArrayList<>();
        for (TypeMirror iface : typeElement.getInterfaces()) {
            interfaces.add(iface.toString());
        }
        enumInfo.setInterfaces(interfaces);

        // Extract enum constants
        enumInfo.setConstants(extractEnumConstants(typeElement));

        // Extract fields
        enumInfo.setFields(extractFields(typeElement));

        // Extract constructors
        enumInfo.setConstructors(extractConstructors(typeElement));

        // Extract methods
        enumInfo.setMethods(extractMethods(typeElement));

        return enumInfo;
    }

    /**
     * Converts a TypeElement to AnnotationInfo.
     */
    private AnnotationInfo convertToAnnotationInfo(TypeElement typeElement) {
        AnnotationInfo annotationInfo = new AnnotationInfo();
        fillCommonTypeInfo(annotationInfo, typeElement);

        // Extract annotation elements
        annotationInfo.setElements(extractAnnotationElements(typeElement));

        return annotationInfo;
    }

    /**
     * Fills common fields for all TypeInfo subclasses.
     */
    private void fillCommonTypeInfo(TypeInfo typeInfo, TypeElement typeElement) {
        // Name
        typeInfo.setName(typeElement.getSimpleName().toString());

        // Qualified name
        typeInfo.setQualifiedName(typeElement.getQualifiedName().toString());

        // Modifiers
        List<String> modifiers = new ArrayList<>();
        for (Modifier modifier : typeElement.getModifiers()) {
            modifiers.add(modifier.toString().toLowerCase());
        }
        typeInfo.setModifiers(modifiers);

        // Extract JavaDoc with full tag parsing
        typeInfo.setJavadoc(extractJavaDoc(typeElement));

        // Extract annotations
        typeInfo.setAnnotations(extractAnnotations(typeElement));

        // Extract type parameters
        typeInfo.setTypeParameters(extractTypeParameters(typeElement.getTypeParameters()));

        // Inner types would be extracted here (not implemented yet)
    }

    /**
     * Extracts fields from a type element.
     */
    private List<FieldInfo> extractFields(TypeElement typeElement) {
        List<FieldInfo> fields = new ArrayList<>();

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.FIELD) {
                VariableElement fieldElement = (VariableElement) enclosedElement;
                FieldInfo fieldInfo = new FieldInfo();

                fieldInfo.setName(fieldElement.getSimpleName().toString());
                fieldInfo.setType(fieldElement.asType().toString());
                fieldInfo.setModifiers(extractModifiers(fieldElement));
                fieldInfo.setAnnotations(extractAnnotations(fieldElement));

                // Extract JavaDoc
                fieldInfo.setJavadoc(extractJavaDoc(fieldElement));

                // Constant value (for static final fields)
                Object constantValue = fieldElement.getConstantValue();
                if (constantValue != null) {
                    fieldInfo.setConstantValue(constantValue.toString());
                }

                fields.add(fieldInfo);
            }
        }

        return fields;
    }

    /**
     * Extracts constructors from a type element.
     */
    private List<ConstructorInfo> extractConstructors(TypeElement typeElement) {
        List<ConstructorInfo> constructors = new ArrayList<>();

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosedElement;
                ConstructorInfo constructorInfo = new ConstructorInfo();

                constructorInfo.setName(typeElement.getSimpleName().toString());
                constructorInfo.setModifiers(extractModifiers(constructorElement));
                constructorInfo.setAnnotations(extractAnnotations(constructorElement));
                constructorInfo.setParameters(extractParameters(constructorElement));
                constructorInfo.setExceptions(extractExceptions(constructorElement));
                constructorInfo.setTypeParameters(extractTypeParameters(constructorElement.getTypeParameters()));

                // Extract JavaDoc
                constructorInfo.setJavadoc(extractJavaDoc(constructorElement));

                constructors.add(constructorInfo);
            }
        }

        return constructors;
    }

    /**
     * Extracts methods from a type element.
     */
    private List<MethodInfo> extractMethods(TypeElement typeElement) {
        List<MethodInfo> methods = new ArrayList<>();

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElement = (ExecutableElement) enclosedElement;
                MethodInfo methodInfo = new MethodInfo();

                methodInfo.setName(methodElement.getSimpleName().toString());
                methodInfo.setReturnType(methodElement.getReturnType().toString());
                methodInfo.setModifiers(extractModifiers(methodElement));
                methodInfo.setAnnotations(extractAnnotations(methodElement));
                methodInfo.setParameters(extractParameters(methodElement));
                methodInfo.setExceptions(extractExceptions(methodElement));
                methodInfo.setTypeParameters(extractTypeParameters(methodElement.getTypeParameters()));
                methodInfo.setDefault(methodElement.isDefault());

                // Extract JavaDoc
                methodInfo.setJavadoc(extractJavaDoc(methodElement));

                methods.add(methodInfo);
            }
        }

        return methods;
    }

    /**
     * Extracts enum constants from an enum type element.
     */
    private List<EnumConstantInfo> extractEnumConstants(TypeElement typeElement) {
        List<EnumConstantInfo> constants = new ArrayList<>();

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.ENUM_CONSTANT) {
                VariableElement constantElement = (VariableElement) enclosedElement;
                EnumConstantInfo constantInfo = new EnumConstantInfo();

                constantInfo.setName(constantElement.getSimpleName().toString());
                constantInfo.setAnnotations(extractAnnotations(constantElement));

                // Extract JavaDoc
                constantInfo.setJavadoc(extractJavaDoc(constantElement));

                constants.add(constantInfo);
            }
        }

        return constants;
    }

    /**
     * Extracts annotation elements from an annotation type element.
     */
    private List<AnnotationElementInfo> extractAnnotationElements(TypeElement typeElement) {
        List<AnnotationElementInfo> elements = new ArrayList<>();

        for (Element enclosedElement : typeElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                ExecutableElement elementMethod = (ExecutableElement) enclosedElement;
                AnnotationElementInfo elementInfo = new AnnotationElementInfo();

                elementInfo.setName(elementMethod.getSimpleName().toString());
                elementInfo.setType(elementMethod.getReturnType().toString());

                // Extract default value
                AnnotationValue defaultValue = elementMethod.getDefaultValue();
                if (defaultValue != null) {
                    elementInfo.setDefaultValue(convertAnnotationValue(defaultValue.getValue()));
                }

                // Extract JavaDoc
                elementInfo.setJavadoc(extractJavaDoc(elementMethod));

                elements.add(elementInfo);
            }
        }

        return elements;
    }

    /**
     * Extracts JavaDoc comment with full tag parsing.
     */
    private JavaDocComment extractJavaDoc(Element element) {
        DocTrees docTrees = environment.getDocTrees();
        DocCommentTree docCommentTree = docTrees.getDocCommentTree(element);

        if (docCommentTree == null) {
            return null;
        }

        JavaDocComment javadoc = new JavaDocComment();

        // Extract description (full body) with HTML and inline tag processing
        StringBuilder description = new StringBuilder();
        for (DocTree tree : docCommentTree.getFullBody()) {
            description.append(processDocTree(tree));
        }
        javadoc.setDescription(description.toString().trim());

        // Extract tags
        List<JavaDocTag> tags = new ArrayList<>();
        for (DocTree blockTag : docCommentTree.getBlockTags()) {
            JavaDocTag tag = parseDocTag(blockTag);
            if (tag != null) {
                tags.add(tag);
            }
        }
        javadoc.setTags(tags);

        return javadoc;
    }

    /**
     * Parses a single JavaDoc tag.
     */
    private JavaDocTag parseDocTag(DocTree docTree) {
        if (!(docTree instanceof BlockTagTree)) {
            return null;
        }

        BlockTagTree blockTag = (BlockTagTree) docTree;
        JavaDocTag tag = new JavaDocTag();
        String tagName = blockTag.getTagName();

        tag.setKind(tagName);

        switch (tagName) {
            case "param":
                if (blockTag instanceof ParamTree) {
                    ParamTree paramTree = (ParamTree) blockTag;
                    tag.setName(paramTree.getName().getName().toString());
                    tag.setDescription(extractText(paramTree.getDescription()));
                }
                break;

            case "return":
                if (blockTag instanceof ReturnTree) {
                    ReturnTree returnTree = (ReturnTree) blockTag;
                    tag.setDescription(extractText(returnTree.getDescription()));
                }
                break;

            case "throws":
            case "exception":
                if (blockTag instanceof ThrowsTree) {
                    ThrowsTree throwsTree = (ThrowsTree) blockTag;
                    tag.setException(throwsTree.getExceptionName().toString());
                    tag.setDescription(extractText(throwsTree.getDescription()));
                }
                break;

            case "see":
                if (blockTag instanceof SeeTree) {
                    SeeTree seeTree = (SeeTree) blockTag;
                    tag.setReference(extractText(seeTree.getReference()));
                }
                break;

            case "since":
                if (blockTag instanceof SinceTree) {
                    SinceTree sinceTree = (SinceTree) blockTag;
                    tag.setDescription(extractText(sinceTree.getBody()));
                }
                break;

            case "author":
                if (blockTag instanceof AuthorTree) {
                    AuthorTree authorTree = (AuthorTree) blockTag;
                    tag.setDescription(extractText(authorTree.getName()));
                }
                break;

            case "version":
                if (blockTag instanceof VersionTree) {
                    VersionTree versionTree = (VersionTree) blockTag;
                    tag.setDescription(extractText(versionTree.getBody()));
                }
                break;

            case "deprecated":
                if (blockTag instanceof DeprecatedTree) {
                    DeprecatedTree deprecatedTree = (DeprecatedTree) blockTag;
                    tag.setDescription(extractText(deprecatedTree.getBody()));
                }
                break;

            default:
                // For any other tags, extract as description
                tag.setDescription(blockTag.toString());
                break;
        }

        return tag;
    }

    /**
     * Extracts text from a list of DocTree elements, processing HTML and inline tags.
     */
    private String extractText(List<? extends DocTree> trees) {
        if (trees == null || trees.isEmpty()) {
            return null;
        }

        StringBuilder text = new StringBuilder();
        for (DocTree tree : trees) {
            text.append(processDocTree(tree));
        }
        return text.toString().trim();
    }

    /**
     * Processes a single DocTree element, handling inline tags while preserving HTML.
     */
    private String processDocTree(DocTree tree) {
        if (tree == null) {
            return "";
        }

        // Handle inline tags - process these to make them more readable
        if (tree instanceof LinkTree) {
            LinkTree linkTree = (LinkTree) tree;
            // For {@link}, extract the label or reference
            List<? extends DocTree> label = linkTree.getLabel();
            if (label != null && !label.isEmpty()) {
                return extractText(label);
            }
            return linkTree.getReference().getSignature();
        }

        if (tree instanceof LiteralTree) {
            LiteralTree literalTree = (LiteralTree) tree;
            // For {@code} and {@literal}, return the body text
            return literalTree.getBody().getBody();
        }

        // For all other elements (including HTML tags), preserve them as-is
        return tree.toString();
    }

    /**
     * Extracts parameters from an executable element (method or constructor).
     */
    private List<ParameterInfo> extractParameters(ExecutableElement executableElement) {
        List<ParameterInfo> parameters = new ArrayList<>();

        for (VariableElement paramElement : executableElement.getParameters()) {
            ParameterInfo paramInfo = new ParameterInfo();

            paramInfo.setName(paramElement.getSimpleName().toString());
            paramInfo.setType(paramElement.asType().toString());
            paramInfo.setAnnotations(extractAnnotations(paramElement));
            paramInfo.setVarArgs(executableElement.isVarArgs() &&
                                 executableElement.getParameters().indexOf(paramElement) ==
                                 executableElement.getParameters().size() - 1);

            parameters.add(paramInfo);
        }

        return parameters;
    }

    /**
     * Extracts exception types from an executable element.
     */
    private List<String> extractExceptions(ExecutableElement executableElement) {
        return executableElement.getThrownTypes().stream()
                .map(TypeMirror::toString)
                .collect(Collectors.toList());
    }

    /**
     * Extracts modifiers from an element.
     */
    private List<String> extractModifiers(Element element) {
        return element.getModifiers().stream()
                .map(modifier -> modifier.toString().toLowerCase())
                .collect(Collectors.toList());
    }

    /**
     * Extracts annotations from an element.
     */
    private List<AnnotationUsage> extractAnnotations(Element element) {
        List<AnnotationUsage> annotations = new ArrayList<>();

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            AnnotationUsage annotationUsage = new AnnotationUsage();

            annotationUsage.setType(annotationMirror.getAnnotationType().toString());

            // Extract annotation values
            Map<String, Object> values = new HashMap<>();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
                    annotationMirror.getElementValues().entrySet()) {
                String key = entry.getKey().getSimpleName().toString();
                Object value = convertAnnotationValue(entry.getValue().getValue());
                values.put(key, value);
            }
            annotationUsage.setValues(values);

            annotations.add(annotationUsage);
        }

        return annotations;
    }

    /**
     * Converts annotation values to serializable objects.
     * This ensures that TypeMirror and other compiler types are converted to strings.
     */
    private Object convertAnnotationValue(Object value) {
        if (value == null) {
            return null;
        }

        // If it's a list, convert each element
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            return list.stream()
                    .map(this::convertAnnotationValue)
                    .collect(Collectors.toList());
        }

        // If it's an AnnotationValue, unwrap it
        if (value instanceof AnnotationValue) {
            return convertAnnotationValue(((AnnotationValue) value).getValue());
        }

        // If it's a VariableElement (enum constant), get its name
        if (value instanceof VariableElement) {
            VariableElement varElement = (VariableElement) value;
            return varElement.getEnclosingElement().toString() + "." + varElement.getSimpleName();
        }

        // If it's a TypeMirror (class literal), convert to string
        if (value instanceof TypeMirror) {
            return value.toString();
        }

        // If it's an AnnotationMirror (nested annotation), convert to string representation
        if (value instanceof AnnotationMirror) {
            return value.toString();
        }

        // For primitive types and strings, return as-is
        return value;
    }

    /**
     * Extracts type parameters.
     */
    private List<TypeParameter> extractTypeParameters(List<? extends TypeParameterElement> typeParams) {
        List<TypeParameter> parameters = new ArrayList<>();

        for (TypeParameterElement typeParam : typeParams) {
            TypeParameter parameter = new TypeParameter();

            parameter.setName(typeParam.getSimpleName().toString());

            // Extract bounds
            List<String> bounds = typeParam.getBounds().stream()
                    .filter(bound -> !bound.toString().equals("java.lang.Object"))
                    .map(TypeMirror::toString)
                    .collect(Collectors.toList());
            parameter.setBounds(bounds);

            parameters.add(parameter);
        }

        return parameters;
    }
}
