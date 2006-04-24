package com.ibm.watson.safari.xform.pattern.matching;

import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import polyglot.ast.Local;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;

import com.ibm.watson.safari.xform.pattern.AccessorAdapter;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

public class PolyglotAccessorAdapter implements AccessorAdapter {
    private final TypeSystem fTypeSystem;

    public PolyglotAccessorAdapter(TypeSystem typeSystem) {
        fTypeSystem= typeSystem;
    }

    public Object getValue(NodeAttribute attribute, Object astNode) {
        Node node= (Node) astNode;
        if (attribute.getIDENTIFIER().toString().equals("targetType"))
            return getTargetType(node);
        return null;
    }

    public Object getValue(String attributeName, Object astNode) {
        Node node= (Node) astNode;
        if (attributeName.equals("targetType"))
            return getTargetType(node);
        return null;
    }

    public Object getTypeByName(String typeName) {
        try {
            return fTypeSystem.typeForName(typeName);
        } catch (SemanticException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getTargetType(Node node) {
        if (node instanceof Expr) {
            Expr expr= (Expr) node;

            return typeNameOf(expr.type());
        } else if (node instanceof Local) {
            return typeNameOf(((Local) node).type());
        } else if (node instanceof FieldDecl) {
            return typeNameOf(((FieldDecl) node).type().type());
        } else if (node instanceof MethodDecl) {
            return typeNameOf(((MethodDecl) node).returnType().type());
        }
        return "<unknown>";
    }

    private String typeNameOf(Type type) {
        // Would be better to turn the type name in the pattern to a Polyglot Type
        // object *ONCE*, store it in the pattern, and then compare that to the types
        // in the various concrete AST nodes as matching proceeds...
        if (type == null) return "<unknown>";
        if (type.isPrimitive()) {
            if (type.isBoolean()) return "boolean";
            if (type.isByte()) return "byte";
            if (type.isChar()) return "char";
            if (type.isDouble()) return "double";
            if (type.isFloat()) return "float";
            if (type.isInt()) return "int";
            if (type.isLong()) return "long";
            if (type.isShort()) return "short";
            if (type.isVoid()) return "void";
            return "<unknown>";
        } else if (type.isArray()) {
            ArrayType arrayType= (ArrayType) type;
            return typeNameOf(arrayType.base()) + "[]";
        } else if (type.isClass()) {
            ClassType classType= (ClassType) type;
            return classType.fullName();
        } else
            return "<unknown>";
    }

    public Object[] getChildren(Object astNode) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isInstanceOfType(Object astNode, String typeName) {
        try {
            return Class.forName("polyglot.ast." + typeName).isInstance(astNode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
