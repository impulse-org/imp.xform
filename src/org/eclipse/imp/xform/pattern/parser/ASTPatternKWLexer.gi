--
-- The AST Pattern KeyWord Lexer
--
%options package=org.eclipse.imp.xform.pattern.parser
%options template=KeywordTemplate.gi

%Include
    KWLexerLowerCaseMap.gi
%End

%Export
    -- List all the keywords the kwlexer will export to the lexer and parser
    typeOf
    targetTypeOf
    DEFINE
%End

%Terminals
    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z
    O    T
%End

%Start
    Keyword
%End

%Rules
    -- The Goal for the parser is a single Keyword

    Keyword ::= d e f i n e
        /.$BeginAction
            $setResult($_DEFINE);
          $EndAction
        ./
    Keyword ::= t y p e O f
        /.$BeginAction
            $setResult($_typeOf);
          $EndAction
        ./
    Keyword ::= t a r g e t T y p e O f
        /.$BeginAction
            $setResult($_targetTypeOf);
          $EndAction
        ./
%End
