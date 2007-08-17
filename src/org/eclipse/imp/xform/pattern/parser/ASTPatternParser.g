%options package=org.eclipse.imp.xform.pattern.parser
%options template=dtParserTemplate.gi
%options import_terminals=ASTPatternLexer.gi
%options automatic_ast=toplevel,visitor=preorder,ast_directory=./Ast,ast_type=PatternNode

%Define
    $ast_class /.PatternNode./
%End

%Globals
    /.import java.util.Collections;
    import java.util.Set;
    import java.util.Map;
    import java.util.HashMap;
    import org.eclipse.imp.xform.pattern.matching.IASTAdapter;
    import org.eclipse.imp.xform.pattern.matching.Matcher;
    import org.eclipse.imp.xform.pattern.matching.MatchResult;./
%End

%Headers
    /.  private static IASTAdapter fASTAdapter= new ASTAdapterBase() { };
        public static void setASTAdapter(IASTAdapter a) { fASTAdapter= a; }
        public static IASTAdapter getASTAdapter() { return fASTAdapter; }

        public static class SymbolTable {
            private final Map<String,FunctionDef> fDefinitions= new HashMap<String,FunctionDef>();

            public FunctionDef lookup(String name) {
                return fDefinitions.get(name);
            }
        }

        private final static SymbolTable fSymbolTable= new SymbolTable();

        public static SymbolTable getSymbolTable() { return fSymbolTable; }
     ./
%End

%Identifier
     IDENT
%End

%Terminals
     IDENT
     NUMBER
     STRING
     DEFINE
     SEMICOLON    ::= ';'
     COLON        ::= ':'
     COMMA        ::= ','
     DOT          ::= '.'
     ELLIPSIS     ::= '...'
     PLUS         ::= '+'
     MINUS        ::= '-'
     TIMES        ::= '*'
     EQUALS       ::= '=='
     ARROW        ::= '=>'
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
     SHARP        ::= '#'
     UNDERSCORE   ::= '_'
%End

%Start
    TopLevel
%End

%Rules
    TopLevel ::= RewriteRule | Pattern | FunctionDef

    FunctionDef ::= DEFINE$ IDENT '('$ FormalArgList ')'$ '{'$ Pattern$Body '}'$

    FormalArgList$$FormalArg ::= FormalArg | FormalArgList ','$ FormalArg

    FormalArg ::= IDENT

    RewriteRule ::= Pattern$lhs '=>'$ Pattern$rhs

    Pattern$Pattern ::= Node
                    |   Node ScopeBlock
	/.
	    public Pattern betaSubst(Map bindings) {
	        if (bindings.isEmpty())
	            return this;
	        INode node= ((Node) _Node).betaSubst(bindings);
	        ScopeBlock scope= (_ScopeBlock == null) ? null : _ScopeBlock.betaSubst(bindings);

	        return new Pattern(environment, leftIToken, rightIToken, node, scope);
	    }
	 ./

    ScopeBlock ::= '{'$ PatternList '}'$
        /.
            public ScopeBlock betaSubst(Map bindings) {
                PatternList mappedPatterns= _PatternList.betaSubst(bindings);
                return new ScopeBlock(environment, leftIToken, rightIToken, mappedPatterns);
            }
         ./

    PatternList$$Pattern ::= Pattern | PatternList Pattern
        /.
            public PatternList betaSubst(Map bindings) {
                PatternList mappedPatterns= new PatternList(environment, leftIToken, rightIToken, true);
                for(int i=0; i < size(); i++) {
                    IPattern pattern= getPatternAt(i);
                    // Following instanceof's wouldn't be necessary if JikesPG could promote common production methods to the non-terminal interface.
                    IPattern mappedPattern= null;
                    if (pattern instanceof Pattern)
                        mappedPattern= ((Pattern) pattern).betaSubst(bindings);
                    else if (pattern instanceof Node)
                        mappedPattern= ((Node) pattern).betaSubst(bindings);
                    else if (pattern instanceof FunctionCall)
                        mappedPattern= ((FunctionCall) pattern).betaSubst(bindings);
                    mappedPatterns.add(mappedPattern);
                }
                return mappedPatterns;
            }
         ./

    Node ::= '['$ NodeType$type optNodeName$name optSharp optTargetType$targetType optConstraintList$constraints ChildList ']'$
         /.
             public Node betaSubst(Map bindings) {
                 optNodeName name= _name;
                 if (name != null) {
                     IToken mappedName= bindings.containsKey(_name) ? (IToken) bindings.get(_name) : _name.getIDENT();

                     name= new optNodeName(mappedName);
                 }
                 return new Node(environment, leftIToken, rightIToken, _type, name,
		                _optSharp, _targetType,
		                _constraints.betaSubst(bindings),
		                _ChildList.betaSubst(bindings));
             }
          ./
         |   FunctionCall

    FunctionCall ::= IDENT '('$ ActualArgList ')'$
        /.
             public FunctionCall betaSubst(Map bindings) {
                 return new FunctionCall(environment, leftIToken, rightIToken, _IDENT,
		                _ActualArgList.betaSubst(bindings));
             }
         ./

    ActualArgList$$ActualArg ::= ActualArg | ActualArgList ','$ ActualArg
        /.
            public ActualArgList betaSubst(Map bindings) {
                 ActualArgList mappedArgList= new ActualArgList(environment, leftIToken, rightIToken, true);

                 for(int i=0; i < size(); i++) {
                     ActualArg arg= getActualArgAt(i);

                     mappedArgList.add(arg.betaSubst(bindings));
                 }
                 return mappedArgList;
             }
         ./

    ActualArg ::= IDENT
        /.
            public ActualArg betaSubst(Map bindings) {
                return this;
            }
         ./

    optSharp ::= %empty | '#'

    NodeType      ::= IDENT
    optNodeName   ::= IDENT | %empty
    optTargetType ::= COLON$ IDENT | %empty

    optConstraintList ::= %empty
                        | '{'$ ConstraintList '}'$
        /.
            public optConstraintList betaSubst(Map bindings) {
                return new optConstraintList(environment, leftIToken, rightIToken,
                                             _ConstraintList.betaSubst(bindings));
            }
         ./

    ConstraintList$$Constraint ::= Constraint
                                 | ConstraintList ','$ Constraint
        /.
            public ConstraintList betaSubst(Map bindings) {
                ConstraintList mappedConstraints= new ConstraintList(environment, leftIToken, rightIToken, true);

                for(int i=0; i < size(); i++) {
                    IConstraint cons= getConstraintAt(i);
                    IConstraint mappedCons= null;
                    if (cons instanceof OperatorConstraint)
                        mappedCons= ((OperatorConstraint) cons).betaSubst(bindings);
                    else if (cons instanceof BoundConstraint)
                        mappedCons= ((BoundConstraint) cons).betaSubst(bindings);
                    mappedConstraints.add(mappedCons);
                }
                return mappedConstraints;
            }
         ./

    Constraint ::= OperatorConstraint
                 | BoundConstraint

    OperatorConstraint ::= NodeAttribute$lhs Operator Value$rhs
        /.
            public OperatorConstraint betaSubst(Map bindings) {
                IValue newRHS= null;
                if (_rhs instanceof NodeAttribute)
                    newRHS= ((NodeAttribute) _rhs).betaSubst(bindings);
                else if (_rhs instanceof ILiteral)
                    newRHS= _rhs;
                else if (_rhs instanceof Node)
                    newRHS= ((Node) _rhs).betaSubst(bindings);
                return new OperatorConstraint(environment, leftIToken, rightIToken,
                                              _lhs.betaSubst(bindings),
                                              _Operator,
                                              newRHS);
            }
         ./

    BoundConstraint ::= '<'$ Bound$lowerBound ':'$ Bound$upperBound '>'$
        /.
            public BoundConstraint betaSubst(Map bindings) {
                return this;
            }
         ./

    Bound           ::= NumericBound | Unbounded
    NumericBound    ::= NUMBER
    Unbounded       ::= '*'

    Value     ::= NodeAttribute
                    | Literal
                    | Node

    NodeAttribute ::= optAttrList IDENT
        /. public Object getValue(Object targetNode) {
               for(int i=0; i < _optAttrList.size(); i++)
                   targetNode= environment.getASTAdapter().getValue(_optAttrList.getElementAt(i).toString(), targetNode);
               return environment.getASTAdapter().getValue(_IDENT.toString(), targetNode);
           }
           public NodeAttribute betaSubst(Map bindings) {
               return new NodeAttribute(environment, leftIToken, rightIToken,
                                        _optAttrList.betaSubst(bindings),
                                        _IDENT);
           }
         ./
    optAttrList$$ident ::= %empty | optAttrList ident '.'$
        /.
            public identList betaSubst(Map bindings) {
                // Is it right to map each component individually? Probably not...
                identList mappedIdents= new identList(environment, leftIToken, rightIToken, true);
                for(int i=0; i < size(); i++) {
                    ident id= getidentAt(i);
                    mappedIdents.add(bindings.containsKey(id) ? (ident) bindings.get(id) : id);
                }
                return mappedIdents;
            }
         ./

    ident ::= IDENT

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

    ChildList$$Child ::= %empty
                       | ChildList Child
        /.
            public ChildList betaSubst(Map bindings) {
                ChildList newList= new ChildList(environment, leftIToken, rightIToken, true);
                for(int i=0; i < size(); i++) {
                    newList.add(getChildAt(i).betaSubst(bindings));
                }
                return newList;
            }
        ./

    Child ::= LinkType Node
        /.
            public Child betaSubst(Map bindings) {
                INode mappedNode= null;
                if (_Node instanceof Node)
                    mappedNode= ((Node) _Node).betaSubst(bindings);
                else if (_Node instanceof FunctionCall)
                    mappedNode= ((FunctionCall) _Node).betaSubst(bindings);
                return new Child(environment, leftIToken, rightIToken, _LinkType, mappedNode);
            }
        ./

    LinkType    ::= DirectLink | ClosureLink
    DirectLink  ::= '|-'$ | '\-'$ | %empty
    ClosureLink ::= DirectLink '...' '-'
%End
