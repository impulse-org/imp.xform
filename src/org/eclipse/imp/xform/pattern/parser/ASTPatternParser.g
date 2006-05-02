%options package=com.ibm.watson.safari.xform.pattern.parser
%options template=UIDE/dtParserTemplate.gi
%options import_terminals=ASTPatternLexer.gi
%options automatic_ast=toplevel,visitor=preorder,ast_directory=./Ast,ast_type=PatternNode

$Define
    $ast_class /.Pattern./
$End

$Globals
    /.import java.util.Collections;
    import java.util.Set;
    import com.ibm.watson.safari.xform.pattern.ASTAdapter;
    import com.ibm.watson.safari.xform.pattern.matching.Matcher;
    import com.ibm.watson.safari.xform.pattern.matching.Matcher.MatchContext;./
$End

$Headers
    /.        private static Object[] EMPTY= new Object[0];
        private static ASTAdapter fASTAdapter= new ASTAdapter() { // default do-nothing impl
            public Object getValue(NodeAttribute attribute, Object astNode) { return null; }
            public Object getValue(String attributeName, Object astNode) { return null; }
            public Object[] getChildren(Object astNode) { return EMPTY; }
            public boolean isInstanceOfType(Object astNode, String typeName) { return false; }
            public Set findAllMatches(Matcher matcher, Object astRoot) {
                return Collections.EMPTY_SET;
            }
            public MatchContext findNextMatch(Matcher matcher, Object astRoot, int offset) {
                return null;
            }
            public int getPosition(Object astNode) {
                return 0;
            }
            public int getLength(Object astNode) {
                return 0;
            }
        };
        public static void setASTAdapter(ASTAdapter a) { fASTAdapter= a; }
        public static ASTAdapter getASTAdapter() { return fASTAdapter; }
     ./
$End

$Terminals
     IDENTIFIER 
     NUMBER
     STRING
     SEMICOLON    ::= ';'
     COLON        ::= ':'
     COMMA        ::= ','
     ELLIPSIS     ::= '...'
     PLUS         ::= '+'
     MINUS        ::= '-'
     TIMES        ::= '*'
     EQUALS       ::= '=='
     NOTEQUALS    ::= '!='
     LEFTPAREN    ::= '('
     RIGHTPAREN   ::= ')'
     LEFTBRACE    ::= '{'
     RIGHTBRACE   ::= '}'
     LEFTBRACKET  ::= '['
     RIGHTBRACKET ::= ']'
     DIRECT       ::= '|-'
     DIRECTEND    ::= '\-'
     LESSTHAN     ::= '<'
     GREATERTHAN  ::= '>'
$End

$Start
    Pattern
$End

$Rules
    Pattern$Pattern ::= Node

    Node ::= '['$ NodeType$type optNodeName$name optTargetType$targetType optConstraintList$constraints ChildList ']'$

    NodeType      ::= IDENTIFIER
    optNodeName   ::= IDENTIFIER | $empty
    optTargetType ::= COLON IDENTIFIER | $empty

    optConstraintList ::= $empty
                        | '{'$ ConstraintList '}'$

    ConstraintList$$Constraint ::= Constraint
                                 | ConstraintList ',' Constraint

    Constraint ::= OperatorConstraint
                 | BoundConstraint

    OperatorConstraint ::= Attribute$lhs Operator Attribute$rhs

    BoundConstraint ::= '<'$ Bound$lowerBound ':'$ Bound$upperBound '>'$
    Bound           ::= NumericBound | Unbounded
    NumericBound    ::= NUMBER
    Unbounded       ::= '*'

    Attribute     ::= NodeAttribute
                    | Literal
    NodeAttribute ::= IDENTIFIER optNodeIdent
        /. public Object getValue(Object targetNode) { return ASTPatternParser.getASTAdapter().getValue(this, targetNode); } ./
    optNodeIdent  ::= '('$ IDENTIFIER ')'$ | $empty

    Literal ::= NumberLiteral | StringLiteral
    NumberLiteral ::= NUMBER$valueStr
        /. public Object getValue() { return new Integer(getvalueStr().toString()); } ./
    StringLiteral ::= STRING$valueStr
        /. public Object getValue() { String ret= getvalueStr().toString(); return ret.substring(1, ret.length() - 1); } ./

    Operator  ::= Equals | NotEquals
    Equals    ::= '=='$
        /. public boolean evaluate(Object lhs, Object rhs, Object node) {
               // Oh well, can't put a method on a non-terminal interface, so fake the polymorphism here
               Object lhsValue= lhs, rhsValue= rhs;
               if (lhs instanceof NodeAttribute)
                 lhsValue= ((NodeAttribute) lhs).getValue(node);
               else if (lhs instanceof StringLiteral)
                 lhsValue= ((StringLiteral) lhs).getValue();
               else if (lhs instanceof NumberLiteral)
                 lhsValue= ((NumberLiteral) lhs).getValue();
               if (rhs instanceof NodeAttribute)
                 rhsValue= ((NodeAttribute) rhs).getValue(node);
               else if (rhs instanceof StringLiteral)
                 rhsValue= ((StringLiteral) rhs).getValue();
               else if (rhs instanceof NumberLiteral)
                 rhsValue= ((NumberLiteral) rhs).getValue();
               // If either side has no value, let the comparison fail, since the user may write
               // a node type constraint that is somewhat loose (e.g. "[Expr e { name == 'x' }]"),
               // and so attributes may be requested that don't actually exist for a given node.
               if (lhsValue == null || rhsValue == null)
                 return false;
               return lhsValue.equals(rhsValue);
           }
        ./
    NotEquals ::= '!='$
        /. public boolean evaluate(Object lhs, Object rhs, Object node) {
               // Oh well, can't put a method on a non-terminal interface, so fake the polymorphism here
               Object lhsValue= lhs, rhsValue= rhs;
               if (lhs instanceof NodeAttribute)
                 lhsValue= ((NodeAttribute) lhs).getValue(node);
               else if (lhs instanceof StringLiteral)
                 lhsValue= ((StringLiteral) lhs).getValue();
               else if (lhs instanceof NumberLiteral)
                 lhsValue= ((NumberLiteral) lhs).getValue();
               if (rhs instanceof NodeAttribute)
                 rhsValue= ((NodeAttribute) rhs).getValue(node);
               else if (rhs instanceof StringLiteral)
                 rhsValue= ((StringLiteral) rhs).getValue();
               else if (rhs instanceof NumberLiteral)
                 rhsValue= ((NumberLiteral) rhs).getValue();
               // If either side has no value, let the comparison fail, since the user may write
               // a node type constraint that is somewhat loose (e.g. "[Expr e { name == 'x' }]"),
               // and so attributes may be requested that don't actually exist for a given node.
               if (lhsValue == null || rhsValue == null)
                 return false;
               return !lhsValue.equals(rhsValue);
           }
        ./

    ChildList$$Child ::= $empty
                       | ChildList Child

    Child ::= LinkType Node

    LinkType    ::= DirectLink | ClosureLink
    DirectLink  ::= '|-'$ | '\-'$ | $empty
    ClosureLink ::= DirectLink '...' '-'
$End
