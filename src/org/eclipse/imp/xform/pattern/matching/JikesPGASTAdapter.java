package com.ibm.watson.safari.xform.pattern.matching;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.uide.core.ILanguageService;
import org.jikespg.uide.parser.GetChildrenVisitor;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import org.jikespg.uide.parser.JikesPGParser.nonTerm;
import org.jikespg.uide.parser.JikesPGParser.terminal;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

public class JikesPGASTAdapter implements IASTAdapter, ILanguageService {
    public Object getValue(NodeAttribute attribute, Object astNode) {
        ASTNode node= (ASTNode) astNode;
        // The "targetType" concept probably won't make sense in JikesPG grammars
        // until we have a more principled treatment of macro variables.
//      if (attribute.getIDENTIFIER().toString().equals("targetType"))
//          return getTargetType(node);
        if (attribute.getIDENTIFIER().toString().equals("name"))
            return getName(node);
        return null;
    }

    public Object getValue(String attributeName, Object astNode) {
        ASTNode node= (ASTNode) astNode;
        // The "targetType" concept probably won't make sense in JikesPG grammars
        // until we have a more principled treatment of macro variables.
//      if (attributeName.equals("targetType"))
//          return getTargetType(node);
        if (attributeName.equals("name"))
            return getName(node);
        return null;
    }

    private String getName(ASTNode node) {
        if (node instanceof nonTerm)
            return ((nonTerm) node).getSYMBOL().toString();
        if (node instanceof terminal)
            return ((terminal) node).getterminal_symbol().toString();
        throw new IllegalArgumentException("AST node type " + node.getClass().getName() + " has no name!");
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
                MatchResult m= matcher.match(n);

                if (m != null)
                    result.add(m);
            }

	    public void unimplementedVisitor(String s) { }
        });
        return result;
    }

    public MatchResult findNextMatch(final Matcher matcher, Object astRoot, final int matchStartPos) {
        final MatchResult[] result= new MatchResult[1];
        ASTNode root= (ASTNode) astRoot;

        root.accept(new JikesPGParser.AbstractVisitor() {
            public void preVisit(ASTNode n) {
                if (result[0] != null)
//                    bypass(n)
                    ;
                else {
                    int nodePos= n.getLeftIToken().getStartOffset();

                    if (matchStartPos < nodePos) {
                	MatchResult m= matcher.match(n);

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
