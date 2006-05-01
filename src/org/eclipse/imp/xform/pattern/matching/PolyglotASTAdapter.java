package com.ibm.watson.safari.xform.pattern.matching;

import java.util.HashSet;
import java.util.Set;
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
import polyglot.visit.HaltingVisitor;
import polyglot.visit.NodeVisitor;

import com.ibm.watson.safari.xform.pattern.ASTAdapter;
import com.ibm.watson.safari.xform.pattern.matching.Matcher.MatchContext;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

public class PolyglotASTAdapter implements ASTAdapter {
    private final TypeSystem fTypeSystem;

    public PolyglotASTAdapter(TypeSystem typeSystem) {
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

    public MatchContext findNextMatch(final Matcher matcher, Object astRoot, int startPos) {
        final MatchContext[] result= new MatchContext[1];
        Node root= (Node) astRoot;

        root.visit(new HaltingVisitor() {
            /* (non-Javadoc)
             * @see polyglot.visit.NodeVisitor#enter(polyglot.ast.Node)
             */
            public NodeVisitor enter(Node n) {
                if (result[0] != null)
                    bypass(n);
                else {
                    MatchContext m= matcher.match(n);

                    if (m != null) {
                        result[0]= m;
                        bypass(n);
                    }
                }
                return this;
            }
        });
        return result[0];
    }

    public Set/*<MatchContext>*/ findAllMatches(final Matcher matcher, Object astRoot) {
        final Set/*<MatchContext>*/ result= new HashSet();

        Node root= (Node) astRoot;

        root.visit(new NodeVisitor() {
            /* (non-Javadoc)
             * @see polyglot.visit.NodeVisitor#enter(polyglot.ast.Node)
             */
            public NodeVisitor enter(Node n) {
                MatchContext m= matcher.match(n);

                if (m != null)
                    result.add(m);
                return this;
            }
        });
        return result;
    }

    public int getPosition(Object astNode) {
	Node node= (Node) astNode;
	return node.position().column(); // BUG bogus - wants character offset, not column within line
    }

    public int getLength(Object astNode) {
	Node node= (Node) astNode;
	return node.position().endColumn() - node.position().column(); // BUG bogus - ignores multiple lines
    }
}
