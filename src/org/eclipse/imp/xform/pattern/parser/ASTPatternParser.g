%options package=com.ibm.watson.safari.xform.pattern.parser
%options template=UIDE/dtParserTemplate.gi
%options import_terminals=ASTPatternLexer.gi
%options automatic_ast=toplevel,visitor=preorder,ast_directory=./Ast,ast_type=PatternNode

$Define
    $ast_class /.Pattern./
$End

$Globals
    /.import com.ibm.watson.safari.xform.pattern.AccessorAdapter;./
$End

$Headers
    /.private static AccessorAdapter fAccessorAdapter;
      public static AccessorAdapter getAccessorAdapter() { return fAccessorAdapter; }
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

    ConstraintList$$Constraint ::= $empty
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
        /. public Object getValue() { return ASTPatternParser.getAccessorAdapter().getValue(this); } ./
    optNodeIdent  ::= '('$ IDENTIFIER ')'$ | $empty

    Literal ::= NumberLiteral | StringLiteral
    NumberLiteral ::= NUMBER$valueStr
        /. public Object getValue() { return new Integer(getvalueStr().toString()); } ./
    StringLiteral ::= STRING$valueStr
        /. public Object getValue() { return getvalueStr().toString(); } ./

    Operator  ::= Equals | NotEquals
    Equals    ::= '=='$
        /. public boolean evaluate(Object lhs, Object rhs) { return lhs.equals(rhs); } ./
    NotEquals ::= '!='$
        /. public boolean evaluate(Object lhs, Object rhs) { return !lhs.equals(rhs); } ./

    ChildList$$Child ::= $empty
                       | ChildList Child

    Child ::= LinkType Node

    LinkType    ::= DirectLink | ClosureLink
    DirectLink  ::= '|-'$ | '\-'$ | $empty
    ClosureLink ::= DirectLink '...' '-'
$End
