package com.ibm.watson.safari.xform.pattern.matching;


import java.util.HashSet;
import java.util.Set;
import org.jikespg.uide.parser.GetChildrenVisitor;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import com.ibm.watson.safari.xform.pattern.ASTAdapter;
import com.ibm.watson.safari.xform.pattern.matching.Matcher.MatchContext;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

public class JikesPGASTAdapter implements ASTAdapter {
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

    public Set findAllMatches(final Matcher matcher, Object astRoot) {
        final Set/*<MatchContext>*/ result= new HashSet();

        ASTNode root= (ASTNode) astRoot;

        root.accept(new JikesPGParser.AbstractVisitor() {
            public void preVisit(ASTNode n) {
                MatchContext m= matcher.match(n);

                if (m != null)
                    result.add(m);
            }

	    public void unimplementedVisitor(String s) { }
        });
        return result;
    }

    public MatchContext findNextMatch(final Matcher matcher, Object astRoot, final int matchStartPos) {
        final MatchContext[] result= new MatchContext[1];
        ASTNode root= (ASTNode) astRoot;

        root.accept(new JikesPGParser.AbstractVisitor() {
            public void preVisit(ASTNode n) {
                if (result[0] != null)
//                    bypass(n)
                    ;
                else {
                    int nodePos= n.getLeftIToken().getStartOffset();

                    if (matchStartPos < nodePos) {
                	MatchContext m= matcher.match(n);

                	if (m != null) {
                	    result[0]= m;
//                          bypass(n);
                	}
                    }
                }
//                return this;
            }

	    public void unimplementedVisitor(String s) { }
        });
        return result[0];
    }

    public int getPosition(Object astNode) {
	ASTNode node= (ASTNode) astNode;

	return node.getLeftIToken().getStartOffset();
    }

    public int getLength(Object astNode) {
	ASTNode node= (ASTNode) astNode;

	return node.getRightIToken().getEndOffset() - node.getLeftIToken().getStartOffset() + 1;
    }
}
