package com.ibm.watson.safari.xform.pattern.matching;


import com.ibm.watson.safari.xform.pattern.parser.ASTPatternParser;
import com.ibm.watson.safari.xform.pattern.parser.Ast.BoundConstraint;
import com.ibm.watson.safari.xform.pattern.parser.Ast.Child;
import com.ibm.watson.safari.xform.pattern.parser.Ast.ChildList;
import com.ibm.watson.safari.xform.pattern.parser.Ast.ConstraintList;
import com.ibm.watson.safari.xform.pattern.parser.Ast.Equals;
import com.ibm.watson.safari.xform.pattern.parser.Ast.IConstraint;
import com.ibm.watson.safari.xform.pattern.parser.Ast.IOperator;
import com.ibm.watson.safari.xform.pattern.parser.Ast.Node;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeType;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NotEquals;
import com.ibm.watson.safari.xform.pattern.parser.Ast.OperatorConstraint;
import com.ibm.watson.safari.xform.pattern.parser.Ast.Pattern;
import com.ibm.watson.safari.xform.pattern.parser.Ast.optConstraintList;
import com.ibm.watson.safari.xform.pattern.parser.Ast.optTargetType;

/**
 * @author rfuhrer@watson.ibm.com
 */
public class Matcher {
    private Pattern fPattern;

    private IASTAdapter fASTAdapter= ASTPatternParser.getASTAdapter();

    public Matcher(Pattern p) {
        fPattern= p;
    }

    public MatchResult match(Object ast) {
	if (fPattern == null)
	    return null;
        try {
            MatchResult m= new MatchResult(ast);

            if (doMatch(fPattern.getNode(), ast, m))
                return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean doMatch(Node patternNode, Object astNode, MatchResult match) throws Exception {
        NodeType patNodeASTType= patternNode.gettype();
        optTargetType patNodeTargetType= patternNode.gettargetType();
        String typeName= patNodeASTType.getIDENTIFIER().toString();

        if (patNodeASTType != null && !fASTAdapter.isInstanceOfType(astNode, typeName))
            return false;
        if (patNodeTargetType != null && !patNodeTargetType.getIDENTIFIER().toString().equals(fASTAdapter.getValue(IASTAdapter.TARGET_TYPE, astNode)))
            return false;
        if (!checkConstraints(patternNode, astNode))
            return false;

        ChildList patChildren= patternNode.getChildList();

        if (patChildren.size() > 0) {
            // Don't ask the AST adapter for getChildren() unless the pattern actually has child constraints.
            Object[] astChildren= fASTAdapter.getChildren(astNode);

            for(int i= 0; i < patChildren.size(); i++) {
        	Child patChild= patChildren.getChildAt(i);

        	int j= 0;
        	for(; j < astChildren.length; j++) {
        	    if (doMatch(patChild.getNode(), astChildren[j], match))
        		break;
        	}
        	if (j == astChildren.length)
        	    return false;
            }
        }
        if (patternNode.getname() != null)
            match.addBinding(patternNode.getname().getIDENTIFIER().toString(), astNode);
        match.fMatchNode= astNode;
        return true;
    }

    private boolean checkConstraints(Node patternNode, Object astNode) throws Exception {
        optConstraintList optConstraints= patternNode.getconstraints();

        if (optConstraints != null) {
            ConstraintList constraints= optConstraints.getConstraintList();

            for(int i=0; i < constraints.size(); i++) {
                IConstraint constraint= constraints.getConstraintAt(i);

                if (constraint instanceof OperatorConstraint) {
                    OperatorConstraint cons= (OperatorConstraint) constraint;
                    IOperator op= cons.getOperator();

                    // TODO would be nice if IOperator.evaluate() existed, but there
                    // is currently no way to annotate the JikesPG grammar to do that.
                    if (op instanceof Equals) {
                        if (!((Equals) op).evaluate(cons.getlhs(), cons.getrhs(), astNode))
                            return false;
                    } else if (op instanceof NotEquals) {
                        if (!((NotEquals) op).evaluate(cons.getlhs(), cons.getrhs(), astNode))
                            return false;
                    } else {
                        throw new Exception("Unable to evaluate operator: " + op.toString());
                    }
                } else if (constraint instanceof BoundConstraint) {
//                    BoundConstraint cons= (BoundConstraint) constraint;
//                    IBound lb= cons.getlowerBound();
//                    IBound ub= cons.getupperBound();
                }
            }
        }
        return true;
    }

    public String toString() {
        return "<matcher: " + fPattern + ">";
    }
}
