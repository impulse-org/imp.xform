--
-- The AST Pattern KeyWord Lexer
--
%options package=com.ibm.watson.safari.xform.pattern.parser
%options template=UIDE/KeywordTemplate.gi

$Include
    UIDE/KWLexerLowerCaseMap.gi
$End

$Export
    -- List all the keywords the kwlexer will export to the lexer and parser
    int
    short
$End

$Terminals
    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z
$End

$Start
    Keyword
$End

$Rules
    -- The Goal for the parser is a single Keyword

    Keyword ::= i n t
        /.$BeginAction
            $setResult($_int);
          $EndAction
        ./
    Keyword ::= s h o r t
        /.$BeginAction
            $setResult($_short);
          $EndAction
        ./
$End
