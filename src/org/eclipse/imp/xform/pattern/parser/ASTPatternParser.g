%options package=com.ibm.watson.safari.xform.pattern.parser
%options template=UIDE/dtParserTemplate.gi
%options import_terminals=ASTPatternLexer.gi
%options automatic_ast=toplevel,visitor=preorder,ast_directory=./Ast,ast_type=ASTNode

$Define
    $ast_class /.Pattern./
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

    Constraint ::= Attribute Operator Attribute
                 | '<' Bound ':' Bound '>'

    Bound        ::= NumericBound | Unbounded
    NumericBound ::= NUMBER
    Unbounded    ::= '*'

    Attribute     ::= NodeAttribute | Literal
    NodeAttribute ::= IDENTIFIER optNodeIdent
    optNodeIdent  ::= '('$ IDENTIFIER ')'$ | $empty

    Literal ::= NumberLiteral | StringLiteral
    NumberLiteral ::= NUMBER
    StringLiteral ::= STRING

    Operator  ::= Equals | NotEquals
    Equals    ::= '=='$
    NotEquals ::= '!='$

    ChildList$$Child ::= $empty
                       | ChildList Child

    Child ::= LinkType Node

    LinkType    ::= DirectLink | ClosureLink
    DirectLink  ::= '|-'$ | '\-'$
    ClosureLink ::= DirectLink '...' '-'
$End
