package com.ibm.watson.safari.xform.pattern.matching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.watson.safari.xform.pattern.AccessorAdapter;
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

    private AccessorAdapter fAccessorAdapter= ASTPatternParser.getAccessorAdapter();

    public static class MatchContext {
        private Object fRoot;
        private Object fMatchNode;
        private Map/*<String, Object astNode>*/ fBindings= new HashMap();

        public MatchContext(Object root) {
            fRoot= root;
        }
        public Object getRoot() {
            return fRoot;
        }
        public void setMatchRoot(Object node) {
            fMatchNode= node;
        }
        public Object getMatchNode() {
            return fMatchNode;
        }
        public void addBinding(String varName, Object astNode) {
            fBindings.put(varName, astNode);
        }
        public String toString() {
            StringBuffer buff= new StringBuffer();

            buff.append("node = '");
            buff.append(fMatchNode);
            buff.append("'; bindings = { ");
            for(Iterator iter= fBindings.keySet().iterator(); iter.hasNext(); ) {
                String metaVarName= (String) iter.next();

                buff.append(metaVarName);
                buff.append(" => '");
                buff.append(fBindings.get(metaVarName));
                buff.append("'");
                if (iter.hasNext()) buff.append(", ");
            }
            buff.append(" }");
            return buff.toString();
        }
    }

    public Matcher(Pattern p) {
        fPattern= p;
    }

    public MatchContext match(Object ast) {
        try {
            MatchContext m= new MatchContext(ast);

            if (doMatch(fPattern.getNode(), ast, m))
                return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean doMatch(Node patternNode, Object astNode, MatchContext match) throws Exception {
        NodeType patNodeASTType= patternNode.gettype();
        optTargetType patNodeTargetType= patternNode.gettargetType();
        String typeName= patNodeASTType.getIDENTIFIER().toString();

        if (patNodeASTType != null && !fAccessorAdapter.isInstanceOfType(astNode, typeName))
            return false;
        if (patNodeTargetType != null && !patNodeTargetType.getIDENTIFIER().toString().equals(fAccessorAdapter.getValue(AccessorAdapter.TARGET_TYPE, astNode)))
            return false;
        if (!checkConstraints(patternNode))
            return false;
        ChildList patChildren= patternNode.getChildList();
        Object[] astChildren= fAccessorAdapter.getChildren(astNode);
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
        if (patternNode.getname() != null)
            match.addBinding(patternNode.getname().getIDENTIFIER().toString(), astNode);
        match.fMatchNode= astNode;
        return true;
    }

    private boolean checkConstraints(Node patternNode) throws Exception {
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
                        if (!((Equals) op).evaluate(cons.getlhs(), cons.getrhs()))
                            return false;
                    } else if (op instanceof NotEquals) {
                        if (!((NotEquals) op).evaluate(cons.getlhs(), cons.getrhs()))
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
