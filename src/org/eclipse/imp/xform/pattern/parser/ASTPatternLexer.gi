%options package=com.ibm.watson.safari.xform.pattern.parser
%options template=UIDE/LexerTemplate.gi
%options filter=ASTPatternKWLexer.gi

$Globals
    /.import java.util.*;
    ./
$End

$Define
--    $additional_interfaces /., ILexer./
    $kw_lexer_class /.$ASTPatternKWLexer./
$End

$Include
    uide/LexerBasicMap.gi
$End

$Export
    SINGLE_LINE_COMMENT
    IDENTIFIER
    NUMBER
    STRING
    SEMICOLON
    COLON
    COMMA
    ELLIPSIS
    LEFTPAREN
    RIGHTPAREN
    LEFTBRACE
    RIGHTBRACE
    LEFTBRACKET
    RIGHTBRACKET
    PLUS
    MINUS
    TIMES
    EQUALS
    ARROW
    NOTEQUALS
    DIRECT
    DIRECTEND
$End

$Terminals
    CtlCharNotWS

    LF   CR   HT   FF

    a    b    c    d    e    f    g    h    i    j    k    l    m
    n    o    p    q    r    s    t    u    v    w    x    y    z

    A    B    C    D    E    F    G    H    I    J    K    L    M
    N    O    P    Q    R    S    T    U    V    W    X    Y    Z

    0    1    2    3    4    5    6    7    8    9

    AfterASCII   ::= '\u0080..\ufffe'
    Space        ::= ' '
    LF           ::= NewLine
    CR           ::= Return
    HT           ::= HorizontalTab
    FF           ::= FormFeed
    DoubleQuote  ::= '"'
    SingleQuote  ::= "'"
    Percent      ::= '%'
    VerticalBar  ::= '|'
    Exclamation  ::= '!'
    AtSign       ::= '@'
    BackQuote    ::= '`'
    Tilde        ::= '~'
    Sharp        ::= '#'
    DollarSign   ::= '$'
    Ampersand    ::= '&'
    Caret        ::= '^'
--  Underscore   ::= '_'
    Colon        ::= ':'
    SemiColon    ::= ';'
    BackSlash    ::= '\'
    LeftBrace    ::= '{'
    RightBrace   ::= '}'
    LeftBracket  ::= '['
    RightBracket ::= ']'
    QuestionMark ::= '?'
    Comma        ::= ','
    Dot          ::= '.'
    LessThan     ::= '<'
    GreaterThan  ::= '>'
    Plus         ::= '+'
    Minus        ::= '-'
    Slash        ::= '/'
    Star         ::= '*'
    LeftParen    ::= '('
    RightParen   ::= ')'
    Equal        ::= '='
--    Ellipsis     ::= '...'
--    NotEquals    ::= '!='
--    Equals       ::= '=='
--    Direct       ::= '|-'
--    DirectEnd    ::= '\-'
$End

$Start
    Token
$End

$Rules
    Token ::= identifier
        /.$BeginJava
                    checkForKeyWord();
          $EndJava
        ./
    Token ::= number
        /.$BeginJava
                    makeToken($_NUMBER);
          $EndJava
        ./
    Token ::= white
        /.$BeginJava
                    skipToken();
          $EndJava
        ./
    Token ::= slc
        /.$BeginJava
                    makeComment($_SINGLE_LINE_COMMENT);
          $EndJava
        ./

    Token ::= ','
        /.$BeginJava
                    makeToken($_COMMA);
          $EndJava
        ./

    Token ::= ':'
        /.$BeginJava
                    makeToken($_COLON);
          $EndJava
        ./

    Token ::= ';'
        /.$BeginJava
                    makeToken($_SEMICOLON);
          $EndJava
        ./

    Token ::= '+'
        /.$BeginJava
                    makeToken($_PLUS);
          $EndJava
        ./

    Token ::= '-'
        /.$BeginJava
                    makeToken($_MINUS);
          $EndJava
        ./

    Token ::= '*'
        /.$BeginJava
                    makeToken($_TIMES);
          $EndJava
        ./

    Token ::= '{'
        /.$BeginJava
                    makeToken($_LEFTBRACE);
          $EndJava
        ./

    Token ::= '}'
        /.$BeginJava
                    makeToken($_RIGHTBRACE);
          $EndJava
        ./

    Token ::= '['
        /.$BeginJava
                    makeToken($_LEFTBRACKET);
          $EndJava
        ./

    Token ::= ']'
        /.$BeginJava
                    makeToken($_RIGHTBRACKET);
          $EndJava
        ./

    Token ::= '('
        /.$BeginJava
                    makeToken($_LEFTPAREN);
          $EndJava
        ./

    Token ::= ')'
        /.$BeginJava
                    makeToken($_RIGHTPAREN);
          $EndJava
        ./

    Token ::= '=' '='
        /.$BeginJava
                    makeToken($_EQUALS);
          $EndJava
        ./

    Token ::= '=' '>'
        /.$BeginJava
                    makeToken($_ARROW);
          $EndJava
        ./

    Token ::= '!' '='
        /.$BeginJava
                    makeToken($_NOTEQUALS);
          $EndJava
        ./

    Token ::= '|' '-'
        /.$BeginJava
                    makeToken($_DIRECT);
          $EndJava
        ./

    Token ::= '\' '-'
        /.$BeginJava
                    makeToken($_DIRECTEND);
          $EndJava
        ./

    Token ::= '.' '.' '.'
        /.$BeginJava
                    makeToken($_ELLIPSIS);
          $EndJava
        ./

    Token ::= string
        /.$BeginJava
                    makeToken($_STRING);
          $EndJava
         ./

    identifier -> letter
                | identifier letter
                | identifier digit
                | identifier '_'

    number ::= digit
             | number digit

    string ::= stringPrefix "'"

    stringPrefix ::= "'"
                 | stringPrefix notQuote

    white ::= whiteChar
          | white whiteChar

    slc ::= '/' '/'
          | slc notEOL

    digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

    aA ::= a | A
    bB ::= b | B
    cC ::= c | C
    dD ::= d | D
    eE ::= e | E
    fF ::= f | F
    gG ::= g | G
    hH ::= h | H
    iI ::= i | I
    jJ ::= j | J
    kK ::= k | K
    lL ::= l | L
    mM ::= m | M
    nN ::= n | N
    oO ::= o | O
    pP ::= p | P
    qQ ::= q | Q
    rR ::= r | R
    sS ::= s | S
    tT ::= t | T
    uU ::= u | U
    vV ::= v | V
    wW ::= w | W
    xX ::= x | X
    yY ::= y | Y
    zZ ::= z | Z

    letter ::= aA | bB | cC | dD | eE | fF | gG | hH | iI | jJ | kK | lL | mM | nN | oO | pP | qQ | rR | sS | tT | uU | vV | wW | xX | yY | zZ

    any ::= letter | digit | special | white

    whiteChar ::= Space | LF | CR | HT | FF

    special ::= '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '.' |
                '%' | '&' | '^' | ':' | ';' | "'" | '\' | '|' | '{' | '}' |
                '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '_' |
                '/' | '$'

    notEOL ::= letter | digit | special | Space | HT | FF

    specialNoQuote ::= '+' | '-' | '(' | ')' | '"' | '!' | '@' | '`' | '~' | '.' |
                       '%' | '&' | '^' | ':' | ';' |       '\' | '|' | '{' | '}' |
                       '[' | ']' | '?' | ',' | '<' | '>' | '=' | '#' | '*' | '_' |
                       '/' | '$'

    notQuote ::= letter | digit | specialNoQuote | Space | HT
$End
