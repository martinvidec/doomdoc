package at.videc.bomblet;

import at.videc.bomblet.dto.*;
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

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

        // Fields, constructors, methods would be added here
        // For now, we keep them empty

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

        // Methods and fields would be added here
        // For now, we keep them empty

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

        // Enum constants, fields, constructors, methods would be added here
        // For now, we keep them empty

        return enumInfo;
    }

    /**
     * Converts a TypeElement to AnnotationInfo.
     */
    private AnnotationInfo convertToAnnotationInfo(TypeElement typeElement) {
        AnnotationInfo annotationInfo = new AnnotationInfo();
        fillCommonTypeInfo(annotationInfo, typeElement);

        // Annotation elements would be added here
        // For now, we keep them empty

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

        // JavaDoc - would be extracted here
        // For now, we create a basic JavaDoc comment
        String docComment = environment.getElementUtils().getDocComment(typeElement);
        if (docComment != null && !docComment.trim().isEmpty()) {
            JavaDocComment javadoc = new JavaDocComment();
            javadoc.setDescription(docComment.trim());
            typeInfo.setJavadoc(javadoc);
        }

        // Annotations would be extracted here
        // Type parameters would be extracted here
        // Inner types would be extracted here
    }
}
