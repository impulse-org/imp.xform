package com.ibm.watson.safari.xform.pattern.matching;


import org.jikespg.uide.parser.GetChildrenVisitor;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import com.ibm.watson.safari.xform.pattern.AccessorAdapter;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

public class JikesPGAccessorAdapter implements AccessorAdapter {
    public Object getValue(NodeAttribute attribute, Object astNode) {
        ASTNode node= (ASTNode) astNode;
        // The "targetType" concept probably won't make sense in JikesPG grammars
        // until we have a more principled treatment of macro variables.
//      if (attribute.getIDENTIFIER().toString().equals("targetType"))
//          return getTargetType(node);
        return null;
    }

    public Object getValue(String attributeName, Object astNode) {
        ASTNode node= (ASTNode) astNode;
        // The "targetType" concept probably won't make sense in JikesPG grammars
        // until we have a more principled treatment of macro variables.
//      if (attributeName.equals("targetType"))
//          return getTargetType(node);
        return null;
    }

    public Object[] getChildren(Object astNode) {
	GetChildrenVisitor v= new GetChildrenVisitor();
	((ASTNode) astNode).accept(v);
        return v.getChildren();
    }

    public boolean isInstanceOfType(Object astNode, String typeName) {
        try {
            return Class.forName("org.jikespg.uide.parser.JikesPGParser$" + typeName).isInstance(astNode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
