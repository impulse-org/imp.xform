
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2007 IBM Corporation.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
//Contributors:
//    Philippe Charles (pcharles@us.ibm.com) - initial API and implementation

////////////////////////////////////////////////////////////////////////////////

package org.eclipse.imp.xform.pattern.parser;

import org.eclipse.imp.xform.pattern.parser.Ast.*;
import lpg.runtime.*;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import org.eclipse.imp.services.IASTAdapter;
import org.eclipse.imp.xform.pattern.matching.Matcher;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
public class ASTPatternParser implements RuleAction
{
    private PrsStream prsStream = null;
    
    private boolean unimplementedSymbolsWarning = false;

    private static ParseTable prsTable = new ASTPatternParserprs();
    public ParseTable getParseTable() { return prsTable; }

    private DeterministicParser dtParser = null;
    public DeterministicParser getParser() { return dtParser; }

    private void setResult(Object object) { dtParser.setSym1(object); }
    public Object getRhsSym(int i) { return dtParser.getSym(i); }

    public int getRhsTokenIndex(int i) { return dtParser.getToken(i); }
    public IToken getRhsIToken(int i) { return prsStream.getIToken(getRhsTokenIndex(i)); }
    
    public int getRhsFirstTokenIndex(int i) { return dtParser.getFirstToken(i); }
    public IToken getRhsFirstIToken(int i) { return prsStream.getIToken(getRhsFirstTokenIndex(i)); }

    public int getRhsLastTokenIndex(int i) { return dtParser.getLastToken(i); }
    public IToken getRhsLastIToken(int i) { return prsStream.getIToken(getRhsLastTokenIndex(i)); }

    public int getLeftSpan() { return dtParser.getFirstToken(); }
    public IToken getLeftIToken()  { return prsStream.getIToken(getLeftSpan()); }

    public int getRightSpan() { return dtParser.getLastToken(); }
    public IToken getRightIToken() { return prsStream.getIToken(getRightSpan()); }

    public int getRhsErrorTokenIndex(int i)
    {
        int index = dtParser.getToken(i);
        IToken err = prsStream.getIToken(index);
        return (err instanceof ErrorToken ? index : 0);
    }
    public ErrorToken getRhsErrorIToken(int i)
    {
        int index = dtParser.getToken(i);
        IToken err = prsStream.getIToken(index);
        return (ErrorToken) (err instanceof ErrorToken ? err : null);
    }

    public void reset(ILexStream lexStream)
    {
        prsStream = new PrsStream(lexStream);
        dtParser.reset(prsStream);

        try
        {
            prsStream.remapTerminalSymbols(orderedTerminalSymbols(), prsTable.getEoftSymbol());
        }
        catch(NullExportedSymbolsException e) {
        }
        catch(NullTerminalSymbolsException e) {
        }
        catch(UnimplementedTerminalsException e)
        {
            if (unimplementedSymbolsWarning) {
                java.util.ArrayList unimplemented_symbols = e.getSymbols();
                System.out.println("The Lexer will not scan the following token(s):");
                for (int i = 0; i < unimplemented_symbols.size(); i++)
                {
                    Integer id = (Integer) unimplemented_symbols.get(i);
                    System.out.println("    " + ASTPatternParsersym.orderedTerminalSymbols[id.intValue()]);               
                }
                System.out.println();
            }
        }
        catch(UndefinedEofSymbolException e)
        {
            throw new Error(new UndefinedEofSymbolException
                                ("The Lexer does not implement the Eof symbol " +
                                 ASTPatternParsersym.orderedTerminalSymbols[prsTable.getEoftSymbol()]));
        }
    }
    
    public ASTPatternParser()
    {
        try
        {
            dtParser = new DeterministicParser(prsStream, prsTable, (RuleAction) this);
        }
        catch (NotDeterministicParseTableException e)
        {
            throw new Error(new NotDeterministicParseTableException
                                ("Regenerate ASTPatternParserprs.java with -NOBACKTRACK option"));
        }
        catch (BadParseSymFileException e)
        {
            throw new Error(new BadParseSymFileException("Bad Parser Symbol File -- ASTPatternParsersym.java. Regenerate ASTPatternParserprs.java"));
        }
    }

    public ASTPatternParser(ILexStream lexStream)
    {
        this();
        reset(lexStream);
    }

    public int numTokenKinds() { return ASTPatternParsersym.numTokenKinds; }
    public String[] orderedTerminalSymbols() { return ASTPatternParsersym.orderedTerminalSymbols; }
    public String getTokenKindName(int kind) { return ASTPatternParsersym.orderedTerminalSymbols[kind]; }            
    public int getEOFTokenKind() { return prsTable.getEoftSymbol(); }
    public IPrsStream getIPrsStream() { return prsStream; }

    /**
     * @deprecated replaced by {@link #getIPrsStream()}
     *
     */
    public PrsStream getPrsStream() { return prsStream; }

    /**
     * @deprecated replaced by {@link #getIPrsStream()}
     *
     */
    public PrsStream getParseStream() { return prsStream; }

    public PatternNode parser()
    {
        return parser(null, 0);
    }
        
    public PatternNode parser(Monitor monitor)
    {
        return parser(monitor, 0);
    }
        
    public PatternNode parser(int error_repair_count)
    {
        return parser(null, error_repair_count);
    }
        
    public PatternNode parser(Monitor monitor, int error_repair_count)
    {
        dtParser.setMonitor(monitor);

        try
        {
            return (PatternNode) dtParser.parse();
        }
        catch (BadParseException e)
        {
            prsStream.reset(e.error_token); // point to error token

            DiagnoseParser diagnoseParser = new DiagnoseParser(prsStream, prsTable);
            diagnoseParser.diagnose(e.error_token);
        }

        return null;
    }

    //
    // Additional entry points, if any
    //
    
  private static IASTAdapter fASTAdapter= new ASTAdapterBase() { };
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
 
    public void ruleAction(int ruleNumber)
    {
        switch (ruleNumber)
        {

            //
            // Rule 1:  TopLevel ::= RewriteRule
            //
            case 1:
                break;
            //
            // Rule 2:  TopLevel ::= Pattern
            //
            case 2:
                break;
            //
            // Rule 3:  TopLevel ::= FunctionDef
            //
            case 3:
                break;
            //
            // Rule 4:  FunctionDef ::= DEFINE$ IDENT ($ FormalArgList )$ {$ Pattern$Body }$
            //
            case 4: {
                setResult(
                    new FunctionDef(getLeftIToken(), getRightIToken(),
                                    new PatternNodeToken(getRhsIToken(2)),
                                    (FormalArgList)getRhsSym(4),
                                    (IPattern)getRhsSym(7))
                );
                break;
            }
            //
            // Rule 5:  FormalArgList ::= FormalArg
            //
            case 5: {
                setResult(
                    new FormalArgList((FormalArg)getRhsSym(1), true /* left recursive */)
                );
                break;
            }
            //
            // Rule 6:  FormalArgList ::= FormalArgList ,$ FormalArg
            //
            case 6: {
                ((FormalArgList)getRhsSym(1)).add((FormalArg)getRhsSym(3));
                break;
            }
            //
            // Rule 7:  FormalArg ::= IDENT
            //
            case 7: {
                setResult(
                    new FormalArg(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 8:  RewriteRule ::= Pattern$lhs =>$ Pattern$rhs
            //
            case 8: {
                setResult(
                    new RewriteRule(getLeftIToken(), getRightIToken(),
                                    (IPattern)getRhsSym(1),
                                    (IPattern)getRhsSym(3))
                );
                break;
            }
            //
            // Rule 9:  Pattern ::= Node
            //
            case 9: {
                setResult(
                    new Pattern(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                (INode)getRhsSym(1),
                                (ScopeBlock)null)
                );
                break;
            }
            //
            // Rule 10:  Pattern ::= Node ScopeBlock
            //
            case 10: {
                setResult(
                    new Pattern(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                (INode)getRhsSym(1),
                                (ScopeBlock)getRhsSym(2))
                );
                break;
            }
            //
            // Rule 11:  ScopeBlock ::= {$ PatternList }$
            //
            case 11: {
                setResult(
                    new ScopeBlock(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                   (PatternList_PatternList)getRhsSym(2))
                );
                break;
            }
            //
            // Rule 12:  PatternList ::= Pattern
            //
            case 12: {
                setResult(
                    new PatternList_PatternList(ASTPatternParser.this, (IPattern)getRhsSym(1), true /* left recursive */)
                );
                break;
            }
            //
            // Rule 13:  PatternList ::= PatternList Pattern
            //
            case 13: {
                ((PatternList_PatternList)getRhsSym(1)).add((IPattern)getRhsSym(2));
                break;
            }
            //
            // Rule 14:  Node ::= [$ NodeType$type optNodeName$name optSharp optTargetType$targetType optConstraintList$constraints ChildList ]$
            //
            case 14: {
                setResult(
                    new Node(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                             (NodeType)getRhsSym(2),
                             (optNodeName)getRhsSym(3),
                             (optSharp)getRhsSym(4),
                             (optTargetType)getRhsSym(5),
                             (optConstraintList)getRhsSym(6),
                             (ChildList_ChildList)getRhsSym(7))
                );
                break;
            }
            //
            // Rule 15:  Node ::= FunctionCall
            //
            case 15:
                break;
            //
            // Rule 16:  FunctionCall ::= IDENT ($ ActualArgList )$
            //
            case 16: {
                setResult(
                    new FunctionCall(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                     new PatternNodeToken(getRhsIToken(1)),
                                     (ActualArgList_ActualArgList)getRhsSym(3))
                );
                break;
            }
            //
            // Rule 17:  ActualArgList ::= ActualArg
            //
            case 17: {
                setResult(
                    new ActualArgList_ActualArgList(ASTPatternParser.this, (ActualArg)getRhsSym(1), true /* left recursive */)
                );
                break;
            }
            //
            // Rule 18:  ActualArgList ::= ActualArgList ,$ ActualArg
            //
            case 18: {
                ((ActualArgList_ActualArgList)getRhsSym(1)).add((ActualArg)getRhsSym(3));
                break;
            }
            //
            // Rule 19:  ActualArg ::= IDENT
            //
            case 19: {
                setResult(
                    new ActualArg(ASTPatternParser.this, getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 20:  optSharp ::= $Empty
            //
            case 20: {
                setResult(null);
                break;
            }
            //
            // Rule 21:  optSharp ::= #
            //
            case 21: {
                setResult(
                    new optSharp(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 22:  NodeType ::= IDENT
            //
            case 22: {
                setResult(
                    new NodeType(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 23:  optNodeName ::= IDENT
            //
            case 23: {
                setResult(
                    new optNodeName(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 24:  optNodeName ::= $Empty
            //
            case 24: {
                setResult(null);
                break;
            }
            //
            // Rule 25:  optTargetType ::= COLON$ IDENT
            //
            case 25: {
                setResult(
                    new optTargetType(getLeftIToken(), getRightIToken(),
                                      new PatternNodeToken(getRhsIToken(2)))
                );
                break;
            }
            //
            // Rule 26:  optTargetType ::= $Empty
            //
            case 26: {
                setResult(null);
                break;
            }
            //
            // Rule 27:  optConstraintList ::= $Empty
            //
            case 27: {
                setResult(null);
                break;
            }
            //
            // Rule 28:  optConstraintList ::= {$ ConstraintList }$
            //
            case 28: {
                setResult(
                    new optConstraintList(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                          (ConstraintList_ConstraintList)getRhsSym(2))
                );
                break;
            }
            //
            // Rule 29:  ConstraintList ::= Constraint
            //
            case 29: {
                setResult(
                    new ConstraintList_ConstraintList(ASTPatternParser.this, (IConstraint)getRhsSym(1), true /* left recursive */)
                );
                break;
            }
            //
            // Rule 30:  ConstraintList ::= ConstraintList ,$ Constraint
            //
            case 30: {
                ((ConstraintList_ConstraintList)getRhsSym(1)).add((IConstraint)getRhsSym(3));
                break;
            }
            //
            // Rule 31:  Constraint ::= OperatorConstraint
            //
            case 31:
                break;
            //
            // Rule 32:  Constraint ::= BoundConstraint
            //
            case 32:
                break;
            //
            // Rule 33:  OperatorConstraint ::= NodeAttribute$lhs Operator Value$rhs
            //
            case 33: {
                setResult(
                    new OperatorConstraint(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                           (NodeAttribute)getRhsSym(1),
                                           (IOperator)getRhsSym(2),
                                           (IValue)getRhsSym(3))
                );
                break;
            }
            //
            // Rule 34:  BoundConstraint ::= <$ Bound$lowerBound :$ Bound$upperBound >$
            //
            case 34: {
                setResult(
                    new BoundConstraint(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                        (IBound)getRhsSym(2),
                                        (IBound)getRhsSym(4))
                );
                break;
            }
            //
            // Rule 35:  Bound ::= NumericBound
            //
            case 35:
                break;
            //
            // Rule 36:  Bound ::= Unbounded
            //
            case 36:
                break;
            //
            // Rule 37:  NumericBound ::= NUMBER
            //
            case 37: {
                setResult(
                    new NumericBound(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 38:  Unbounded ::= *
            //
            case 38: {
                setResult(
                    new Unbounded(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 39:  Value ::= NodeAttribute
            //
            case 39:
                break;
            //
            // Rule 40:  Value ::= Literal
            //
            case 40:
                break;
            //
            // Rule 41:  Value ::= Node
            //
            case 41:
                break;
            //
            // Rule 42:  NodeAttribute ::= optAttrList IDENT
            //
            case 42: {
                setResult(
                    new NodeAttribute(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                                      (optAttrList_identList)getRhsSym(1),
                                      new PatternNodeToken(getRhsIToken(2)))
                );
                break;
            }
            //
            // Rule 43:  optAttrList ::= $Empty
            //
            case 43: {
                setResult(
                    new optAttrList_identList(ASTPatternParser.this, getLeftIToken(), getRightIToken(), true /* left recursive */)
                );
                break;
            }
            //
            // Rule 44:  optAttrList ::= optAttrList ident .$
            //
            case 44: {
                ((optAttrList_identList)getRhsSym(1)).add((ident)getRhsSym(2));
                break;
            }
            //
            // Rule 45:  ident ::= IDENT
            //
            case 45: {
                setResult(
                    new ident(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 46:  Literal ::= NumberLiteral
            //
            case 46:
                break;
            //
            // Rule 47:  Literal ::= StringLiteral
            //
            case 47:
                break;
            //
            // Rule 48:  NumberLiteral ::= NUMBER$valueStr
            //
            case 48: {
                setResult(
                    new NumberLiteral(ASTPatternParser.this, getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 49:  StringLiteral ::= STRING$valueStr
            //
            case 49: {
                setResult(
                    new StringLiteral(ASTPatternParser.this, getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 50:  Operator ::= Equals
            //
            case 50:
                break;
            //
            // Rule 51:  Operator ::= NotEquals
            //
            case 51:
                break;
            //
            // Rule 52:  Equals ::= ==$
            //
            case 52: {
                setResult(
                    new Equals(ASTPatternParser.this, getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 53:  NotEquals ::= !=$
            //
            case 53: {
                setResult(
                    new NotEquals(ASTPatternParser.this, getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 54:  ChildList ::= $Empty
            //
            case 54: {
                setResult(
                    new ChildList_ChildList(ASTPatternParser.this, getLeftIToken(), getRightIToken(), true /* left recursive */)
                );
                break;
            }
            //
            // Rule 55:  ChildList ::= ChildList Child
            //
            case 55: {
                ((ChildList_ChildList)getRhsSym(1)).add((Child)getRhsSym(2));
                break;
            }
            //
            // Rule 56:  Child ::= LinkType Node
            //
            case 56: {
                setResult(
                    new Child(ASTPatternParser.this, getLeftIToken(), getRightIToken(),
                              (ILinkType)getRhsSym(1),
                              (INode)getRhsSym(2))
                );
                break;
            }
            //
            // Rule 57:  LinkType ::= DirectLink
            //
            case 57:
                break;
            //
            // Rule 58:  LinkType ::= ClosureLink
            //
            case 58:
                break;
            //
            // Rule 59:  DirectLink ::= |-$
            //
            case 59: {
                setResult(
                    new DirectLink0(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 60:  DirectLink ::= \-$
            //
            case 60: {
                setResult(
                    new DirectLink1(getRhsIToken(1))
                );
                break;
            }
            //
            // Rule 61:  DirectLink ::= $Empty
            //
            case 61: {
                setResult(null);
                break;
            }
            //
            // Rule 62:  ClosureLink ::= DirectLink ... -
            //
            case 62: {
                setResult(
                    new ClosureLink(getLeftIToken(), getRightIToken(),
                                    (IDirectLink)getRhsSym(1),
                                    new PatternNodeToken(getRhsIToken(2)),
                                    new PatternNodeToken(getRhsIToken(3)))
                );
                break;
            }
    
            default:
                break;
        }
        return;
    }
}

