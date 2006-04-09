%options package=com.ibm.watson.safari.xform.pattern.parser
%options template=UIDE/dtParserTemplate.gi
%options import_terminals=ASTPatternLexer.gi
%options automatic_ast=toplevel,visitor=preorder,ast_directory=./Ast,ast_type=ASTNode

$Define
    $ast_class /.Object./
--    $additional_interfaces /., IParser./
$End

$Terminals
     IDENTIFIER 
     NUMBER
     STRING
     SEMICOLON ::= ';'
     COLON ::= ':'
     ELLIPSIS ::= '...'
     PLUS ::= '+'
     MINUS ::= '-'
     EQUALS ::= '=='
     NOTEQUALS ::= '!='
     LEFTPAREN ::= '('
     RIGHTPAREN ::= ')'
     LEFTBRACE ::= '{'
     RIGHTBRACE ::= '}'
     LEFTBRACKET ::= '['
     RIGHTBRACKET ::= ']'
$End

$Rules
    pattern ::= nodeSpec

    nodeSpec ::= '[' nodeType optNodeName optTargetType optConstraintList childList ']'

    nodeType ::= IDENTIFIER

    optNodeName ::= IDENTIFIER | $empty

    optTargetType ::= COLON IDENTIFIER | $empty

    optConstraintList ::= $empty
        | '{' constraintList '}'

    constraintList$$constraint ::= $empty
        | constraintList constraint

    constraint ::= attribute op attribute

    attribute ::= nodeAttribute | literal

    nodeAttribute ::= IDENTIFIER optNodeIdent

    optNodeIdent ::= '(' IDENTIFIER ')' | $empty

    literal ::= NUMBER | STRING

    op ::= '==' | '!='

    childList$$child ::= $empty
        | childList child

    child ::= nodeSpec
$End
